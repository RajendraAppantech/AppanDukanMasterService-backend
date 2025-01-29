package com.appan.kycmaster.services;

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
import com.appan.kyc.kycgroup.model.FetchKycGroupRequest;
import com.appan.kyc.kycgroup.model.FetchKycGroupResponse;
import com.appan.kyc.kycgroup.model.KycGroupMasterModel;
import com.appan.kycmaster.entity.KycGroupMaster;
import com.appan.kycmaster.repositories.KycRepositories.KycGroupMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchKycGroupService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchKycGroupService.class);

	@Autowired
	private KycGroupMasterRepository kycGroupMasterRepository;

	public FetchKycGroupResponse fetch(FetchKycGroupRequest req, Integer pageNo, Integer pageSize) {
		logger.info(
				"\r\n\r\n**************************** FETCH KYC GROUP DETAILS *************************************");

		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<KycGroupMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getKycGroupName(),
						req.getDescription(), req.getCode(), req.getPriority(), req.getStatus(), req.getFromDate(),
						req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<KycGroupMaster> pageResults = kycGroupMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No data found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<KycGroupMasterModel> kycGroupData = pageResults.stream().map(this::convertToKycGroupModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", kycGroupData, pageDetails);
		} catch (Exception e) {
			logger.error("Error occurred: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<KycGroupMaster> root, String kycGroupName,
			String description, String code, Integer priority, String status, String fromDate, String toDate) {
		List<Predicate> predicates = new ArrayList<>();

		if (!Strings.isNullOrEmpty(kycGroupName)) {
			predicates.add(cb.equal(cb.lower(root.get("kycGroupName")), kycGroupName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(description)) {
			predicates.add(cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(code)) {
			predicates.add(cb.equal(cb.lower(root.get("code")), code.toLowerCase()));
		}
		if (priority != null) {
			predicates.add(cb.equal(root.get("priority"), priority));
		}
		if (!Strings.isNullOrEmpty(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}
		// Date range filter
		if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date startDate = dateFormat.parse(fromDate);
				Date endDate = dateFormat.parse(toDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();
				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (Exception e) {
				logger.error("Error parsing date filters", e);
			}
		}
		return predicates;
	}

	private KycGroupMasterModel convertToKycGroupModel(KycGroupMaster kycGroupMaster) {
		KycGroupMasterModel model = new KycGroupMasterModel();
		model.setId(kycGroupMaster.getId());
		model.setKycGroupName(kycGroupMaster.getKycGroupName());
		model.setDescription(kycGroupMaster.getDescription());
		model.setCode(kycGroupMaster.getCode());
		model.setPriority(kycGroupMaster.getPriority());
		model.setStatus(kycGroupMaster.getStatus());
		model.setCreatedBy(kycGroupMaster.getCreatedBy());
		model.setCreatedDt(kycGroupMaster.getCreatedDt());
		model.setModifyBy(kycGroupMaster.getModifyBy());
		model.setModifyDt(kycGroupMaster.getModifyDt());
		model.setAuthBy(kycGroupMaster.getAuthBy());
		model.setAuthDate(kycGroupMaster.getAuthDate());
		
		if (Strings.isNullOrEmpty(kycGroupMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (kycGroupMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (kycGroupMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (kycGroupMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(kycGroupMaster.getAuthStatus());
		}

		return model;
	}

	private FetchKycGroupResponse buildErrorResponse(boolean status, String message, String respCode,
			List<KycGroupMasterModel> data, PageDetails pageDetails) {
		FetchKycGroupResponse response = new FetchKycGroupResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchKycGroupResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** GET ALL KYC GROUP CODE *************************************");

		FetchKycGroupResponse response = new FetchKycGroupResponse();
		try {
			List<KycGroupMaster> allData = kycGroupMasterRepository.findByStatus("Active");
			if (allData.isEmpty()) {
				return buildErrorResponse(false, "No data found.", "01", null, null);
			}

			List<KycGroupMasterModel> kycGroupData = allData.stream().map(this::convertToKycGroupModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", kycGroupData, null);

		} catch (Exception e) {
			logger.error("Error occurred: ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
