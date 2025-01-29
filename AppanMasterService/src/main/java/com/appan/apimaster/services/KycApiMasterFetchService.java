package com.appan.apimaster.services;

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
import com.appan.apimaster.models.FetchKycApiMaster;
import com.appan.apimaster.models.FetchKycApiResponse;
import com.appan.apimaster.models.KycApiMasterModel;
import com.appan.apimaster.repositories.ApiRepositories.KycApiMasterRepository;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.KycApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class KycApiMasterFetchService {

	@Autowired
	private KycApiMasterRepository kycApiMasterRepository;

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(KycApiMasterFetchService.class);

	public FetchKycApiResponse fetchKycApi(FetchKycApiMaster req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH KYC API DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<KycApiMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt"))); // Sort by created date
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<KycApiMaster> pageResults = kycApiMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No KYC APIs found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<KycApiMasterModel> kycApiData = pageResults.stream().map(this::convertToKycApiModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", kycApiData, pageDetails);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<KycApiMaster> root, FetchKycApiMaster req) {
		List<Predicate> predicates = new ArrayList<>();

		if (req.getApiName() != null && !req.getApiName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("apiName")), req.getApiName().toLowerCase()));
		}
		if (req.getCode() != null && !req.getCode().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("code")), req.getCode().toLowerCase()));
		}
		if (req.getIsActive() != null) {
			predicates.add(cb.equal(root.get("isActive"), req.getIsActive()));
		}
		if (req.getIsUserwise() != null) {
			predicates.add(cb.equal(root.get("isUserwise"), req.getIsUserwise()));
		}
		if (req.getStatus() != null && !req.getStatus().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		if (req.getFromDate() != null && !req.getFromDate().isEmpty() && req.getToDate() != null
				&& !req.getToDate().isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date fromDate = dateFormat.parse(req.getFromDate());
				Date toDate = dateFormat.parse(req.getToDate());

				// Add an extra day to the toDate
				Calendar cal = Calendar.getInstance();
				cal.setTime(toDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				toDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), fromDate, toDate));
			} catch (ParseException e) {
				Logger.error("Error parsing date range: ", e);
			}
		}

		return predicates;
	}

	private KycApiMasterModel convertToKycApiModel(KycApiMaster kycApiMaster) {
		KycApiMasterModel model = new KycApiMasterModel();
		model.setId(kycApiMaster.getId());
		model.setApiName(kycApiMaster.getApiName());
		model.setCode(kycApiMaster.getCode());
		model.setIsActive(kycApiMaster.getIsActive());
		model.setIsUserwise(kycApiMaster.getIsUserwise());
		model.setType(kycApiMaster.getType());
		model.setParam1(kycApiMaster.getParam1());
		model.setParam2(kycApiMaster.getParam2());
		model.setParam3(kycApiMaster.getParam3());
		model.setStatus(kycApiMaster.getStatus());
		model.setCreatedBy(kycApiMaster.getCreatedBy());
		model.setCreatedDt(kycApiMaster.getCreatedDt());
		model.setModifyBy(kycApiMaster.getModifyBy());
		model.setModifyDt(kycApiMaster.getModifyDt());
		model.setAuthBy(kycApiMaster.getAuthBy());
		model.setAuthDate(kycApiMaster.getAuthDate());
		if (Strings.isNullOrEmpty(kycApiMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (kycApiMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (kycApiMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (kycApiMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(kycApiMaster.getAuthStatus());
		}
		return model;
	}

	private FetchKycApiResponse buildErrorResponse(boolean status, String message, String respCode,
			List<KycApiMasterModel> data, PageDetails pageDetails) {
		FetchKycApiResponse response = new FetchKycApiResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchKycApiResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL KYC API CODE *************************************");

		FetchKycApiResponse response = new FetchKycApiResponse();
		try {
			List<KycApiMaster> allKycApis = kycApiMasterRepository.findByStatus("Active");
			if (allKycApis.isEmpty()) {
				return buildErrorResponse(false, "No KYC APIs found.", "01", null, null);
			}

			List<KycApiMasterModel> kycApiData = allKycApis.stream().map(this::convertToKycApiModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", kycApiData, null);

		} catch (Exception e) {
			Logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
