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
import com.appan.kyc.document.model.DocumentMasterModel;
import com.appan.kyc.document.model.FetchDocumentRequest;
import com.appan.kyc.document.model.FetchDocumentResponse;
import com.appan.kycmaster.entity.DocumentMaster;
import com.appan.kycmaster.repositories.KycRepositories.DocumentMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchDocumentService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchDocumentService.class);

	@Autowired
	private DocumentMasterRepository documentMasterRepository;

	public FetchDocumentResponse fetch(FetchDocumentRequest req, Integer pageNo, Integer pageSize) {
		Logger.info(
				"\r\n\r\n**************************** FETCH DOCUMENT DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<DocumentMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getDocumentName(),
						req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt"))); // Sorting by createdDt
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<DocumentMaster> pageResults = documentMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No documents found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<DocumentMasterModel> documentData = pageResults.stream().map(this::convertToDocumentMasterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", documentData, pageDetails);

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<DocumentMaster> root, String documentName,
			String status, String fromDate, String toDate) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
			try {
				Date startDate = dateFormat.parse(fromDate);
				Date endDate = dateFormat.parse(toDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				Logger.error("Error parsing date filters", e);
			}
		}

		if (!Strings.isNullOrEmpty(documentName)) {
			predicates.add(cb.equal(cb.lower(root.get("documentName")), documentName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private DocumentMasterModel convertToDocumentMasterModel(DocumentMaster document) {
		DocumentMasterModel model = new DocumentMasterModel();
		model.setId(document.getId());
		model.setDocumentName(document.getDocumentName());
		model.setHasExpiry(document.getHasExpiry());
		model.setIsEkyc(document.getIsEkyc());
		model.setPriority(document.getPriority());
		model.setIsMandatory(document.getIsMandatory());
		model.setStatus(document.getStatus());
		model.setCreatedBy(document.getCreatedBy());
		model.setCreatedDt(document.getCreatedDt());
		model.setModifyBy(document.getModifyBy());
		model.setModifyDt(document.getModifyDt());
		model.setAuthBy(document.getAuthBy());
		model.setAuthDate(document.getAuthDate());
		
		if (Strings.isNullOrEmpty(document.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (document.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (document.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (document.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(document.getAuthStatus());
		}
		return model;
	}

	private FetchDocumentResponse buildErrorResponse(boolean status, String message, String respCode,
			List<DocumentMasterModel> data, PageDetails pageDetails) {
		FetchDocumentResponse response = new FetchDocumentResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchDocumentResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL DOCUMENTS *************************************");

		FetchDocumentResponse response = new FetchDocumentResponse();
		try {
			List<DocumentMaster> allDocuments = documentMasterRepository.findByStatus("Active");
			if (allDocuments.isEmpty()) {
				return buildErrorResponse(false, "No documents found.", "01", null, null);
			}

			List<DocumentMasterModel> documentData = allDocuments.stream().map(this::convertToDocumentMasterModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", documentData, null);

		} catch (Exception e) {
			Logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
