package com.appan.kycmaster.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.kyc.documentattribute.model.DocumentAttributeMasterModel;
import com.appan.kyc.documentattribute.model.FetchDocumentAttributeRequest;
import com.appan.kyc.documentattribute.model.FetchDocumentAttributeResponse;
import com.appan.kycmaster.entity.DocumentAttributeMaster;
import com.appan.kycmaster.repositories.KycRepositories.DocumentAttributeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchDocumentAttributeService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private DocumentAttributeMasterRepository documentAttributeMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchDocumentAttributeService.class);

	public FetchDocumentAttributeResponse fetch(FetchDocumentAttributeRequest req, Integer pageNo, Integer pageSize) {
		logger.info(
				"\r\n\r\n**************************** FETCH DOCUMENT ATTRIBUTE DETAILS *************************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<DocumentAttributeMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<DocumentAttributeMaster> pageResults = documentAttributeMasterRepository.findAll(specification,
					paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No document attributes found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<DocumentAttributeMasterModel> documentAttributes = pageResults.stream()
					.map(this::convertToDocumentAttributeModel).collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", documentAttributes, pageDetails);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occurred: " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<DocumentAttributeMaster> root,
			FetchDocumentAttributeRequest req) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		// Date range filter
		if (req.getFromDate() != null && req.getToDate() != null) {
			try {
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();
				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				logger.error("Error parsing date filters", e);
			}
		}

		if (req.getUsername() != null && !req.getUsername().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("createdBy")), req.getUsername().toLowerCase()));
		}
		if (req.getAttributeName() != null && !req.getAttributeName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("attributeName")), req.getAttributeName().toLowerCase()));
		}
		if (req.getDocumentName() != null && !req.getDocumentName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("documentName")), req.getDocumentName().toLowerCase()));
		}

		return predicates;
	}

	private DocumentAttributeMasterModel convertToDocumentAttributeModel(
			DocumentAttributeMaster documentAttributeMaster) {
		DocumentAttributeMasterModel model = new DocumentAttributeMasterModel();
		model.setId(documentAttributeMaster.getId());
		model.setAttributeName(documentAttributeMaster.getAttributeName());
		model.setDocumentName(documentAttributeMaster.getDocumentName());
		model.setFieldType(documentAttributeMaster.getFieldType());
		model.setIsEkyc(documentAttributeMaster.getIsEkyc());
		model.setEkycCode(documentAttributeMaster.getEkycCode());
		model.setLabel(documentAttributeMaster.getLabel());
		model.setMaxSize(documentAttributeMaster.getMaxSize());
		model.setMaxWidth(documentAttributeMaster.getMaxWidth());
		model.setPriority(documentAttributeMaster.getPriority());
		model.setMinWidth(documentAttributeMaster.getMinWidth());
		model.setWebRegex(documentAttributeMaster.getWebRegex());
		model.setMobileRegex(documentAttributeMaster.getMobileRegex());
		model.setIsCameraAllowed(documentAttributeMaster.getIsCameraAllowed());
		model.setIsUploadAllowed(documentAttributeMaster.getIsUploadAllowed());
		model.setStatus(documentAttributeMaster.getStatus());
		model.setSupportedFileType(documentAttributeMaster.getSupportedFileType());
		model.setAttributeName(documentAttributeMaster.getAttributeName());
		model.setCreatedBy(documentAttributeMaster.getCreatedBy());
		model.setCreatedDt(documentAttributeMaster.getCreatedDt());
		model.setModifyBy(documentAttributeMaster.getModifyBy());
		model.setModifyDt(documentAttributeMaster.getModifyDt());
		model.setAuthBy(documentAttributeMaster.getAuthBy());
		model.setAuthDate(documentAttributeMaster.getAuthDate());
		
		if (Strings.isNullOrEmpty(documentAttributeMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (documentAttributeMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (documentAttributeMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (documentAttributeMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(documentAttributeMaster.getAuthStatus());
		}

		return model;
	}

	private FetchDocumentAttributeResponse buildErrorResponse(boolean status, String message, String respCode,
			List<DocumentAttributeMasterModel> data, PageDetails pageDetails) {
		FetchDocumentAttributeResponse response = new FetchDocumentAttributeResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchDocumentAttributeResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** GET ALL DOCUMENT ATTRIBUTE CODE *************************************");

		FetchDocumentAttributeResponse response = new FetchDocumentAttributeResponse();
		try {
			List<DocumentAttributeMaster> allAttributes = documentAttributeMasterRepository.findByStatus("Active");
			if (allAttributes.isEmpty()) {
				return buildErrorResponse(false, "No document attributes found.", "01", null, null);
			}

			List<DocumentAttributeMasterModel> documentAttributes = allAttributes.stream()
					.map(this::convertToDocumentAttributeModel).collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", documentAttributes, null);

		} catch (Exception e) {
			logger.error("EXCEPTION: " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
