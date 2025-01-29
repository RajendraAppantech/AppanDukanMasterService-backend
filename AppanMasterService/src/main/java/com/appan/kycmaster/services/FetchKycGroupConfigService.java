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
import com.appan.kyc.kycgroupconfig.model.FetchKycGroupConfigRequest;
import com.appan.kyc.kycgroupconfig.model.FetchKycGroupConfigResponse;
import com.appan.kyc.kycgroupconfig.model.KycGroupConfigMasterModel;
import com.appan.kycmaster.entity.KycGroupConfigMaster;
import com.appan.kycmaster.repositories.KycRepositories.KycGroupConfigMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchKycGroupConfigService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchKycGroupConfigService.class);

	@Autowired
	private KycGroupConfigMasterRepository kycGroupConfigMasterRepository;

	public FetchKycGroupConfigResponse fetch(FetchKycGroupConfigRequest req, Integer pageNo, Integer pageSize) {
		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<KycGroupConfigMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getDocumentName(),
						req.getDocumentGroupName(), req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<KycGroupConfigMaster> pageResults = kycGroupConfigMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<KycGroupConfigMasterModel> data = pageResults.stream().map(this::convertToKycGroupConfigModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", data, pageDetails);
		} catch (Exception e) {
			logger.error("Error fetching KYC group config", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<KycGroupConfigMaster> root, String documentName,
			String documentGroupName, String status, String fromDate, String toDate) {
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
				logger.error("Error parsing date filters", e);
			}
		}

		if (!Strings.isNullOrEmpty(documentName) && !"all".equalsIgnoreCase(documentName)) {
			predicates.add(cb.equal(cb.lower(root.get("documentName")), documentName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(documentGroupName) && !"all".equalsIgnoreCase(documentGroupName)) {
			predicates.add(cb.equal(cb.lower(root.get("documentGroupName")), documentGroupName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(status) && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private KycGroupConfigMasterModel convertToKycGroupConfigModel(KycGroupConfigMaster entity) {
		KycGroupConfigMasterModel model = new KycGroupConfigMasterModel();
		model.setId(entity.getId());
		model.setDocumentName(entity.getDocumentName());
		model.setDocumentGroupName(entity.getDocumentGroupName());
		model.setStatus(entity.getStatus());
		model.setCreatedBy(entity.getCreatedBy());
		model.setCreatedDt(entity.getCreatedDt());
		model.setModifyBy(entity.getModifyBy());
		model.setModifyDt(entity.getModifyDt());
		model.setAuthBy(entity.getAuthBy());
		model.setAuthDate(entity.getAuthDate());
		
		if (Strings.isNullOrEmpty(entity.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (entity.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (entity.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (entity.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(entity.getAuthStatus());
		}

		
		return model;
	}

	private FetchKycGroupConfigResponse buildErrorResponse(boolean status, String message, String respCode,
			List<KycGroupConfigMasterModel> data, PageDetails pageDetails) {
		FetchKycGroupConfigResponse response = new FetchKycGroupConfigResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchKycGroupConfigResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** GET ALL KYC GROUP CONFIG *************************************");

		FetchKycGroupConfigResponse response = new FetchKycGroupConfigResponse();
		try {
			List<KycGroupConfigMaster> all = kycGroupConfigMasterRepository.findByStatus("Active");
			if (all.isEmpty()) {
				return buildErrorResponse(false, "No cities found.", "01", null, null);
			}

			List<KycGroupConfigMasterModel> data = all.stream().map(this::convertToKycGroupConfigModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", data, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
