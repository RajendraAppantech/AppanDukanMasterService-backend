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

import com.appan.apimaster.models.FetchSmsApiMaster;
import com.appan.apimaster.models.FetchSmsApiResponse;
import com.appan.apimaster.models.SmsApiMasterModel;
import com.appan.apimaster.repositories.ApiRepositories.SmsApiMasterRepository;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.SmsApiMaster;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class SmsApiMasterFetchService {

	private static final Logger Logger = LoggerFactory.getLogger(SmsApiMasterFetchService.class);

	@Autowired
	private SmsApiMasterRepository smsApiMasterRepository;

	public FetchSmsApiResponse fetch(FetchSmsApiMaster req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH SMS API DETAILS *************************************");

		try {
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<SmsApiMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<SmsApiMaster> pageResults = smsApiMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "SMS API not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			// Mapping entities to models
			List<SmsApiMasterModel> apiData = pageResults.stream().map(this::convertToSmsApiModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", apiData, pageDetails);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EXCEPTION: " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<SmsApiMaster> root, FetchSmsApiMaster req) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		// Filter by tenant name
		if (!Strings.isNullOrEmpty(req.getTenantName())) {
			predicates.add(cb.equal(cb.lower(root.get("tenantName")), req.getTenantName().toLowerCase()));
		}

		// Filter by status
		if (!Strings.isNullOrEmpty(req.getStatus())) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		// Filter by date range
		if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
			try {
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());

				// Adjust endDate to include the entire day
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				Logger.error("Error parsing date filters", e);
			}
		}

		return predicates;
	}

	private SmsApiMasterModel convertToSmsApiModel(SmsApiMaster smsApiMaster) {
		SmsApiMasterModel model = new SmsApiMasterModel();
		model.setId(smsApiMaster.getId());
		model.setTenantName(smsApiMaster.getTenantName());
		model.setBalApiUrl(smsApiMaster.getBalApiUrl());
		model.setApiUrl(smsApiMaster.getApiUrl());
		model.setStatus(smsApiMaster.getStatus());
		model.setCreatedBy(smsApiMaster.getCreatedBy());
		model.setCreatedDt(smsApiMaster.getCreatedDt());
		model.setModifyBy(smsApiMaster.getModifyBy());
		model.setModifyDt(smsApiMaster.getModifyDt());
		model.setAuthBy(smsApiMaster.getAuthBy());
		model.setAuthDate(smsApiMaster.getAuthDate());
		if (Strings.isNullOrEmpty(smsApiMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (smsApiMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (smsApiMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (smsApiMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(smsApiMaster.getAuthStatus());
		}

		return model;
	}

	private FetchSmsApiResponse buildErrorResponse(boolean status, String message, String respCode,
			List<SmsApiMasterModel> data, PageDetails pageDetails) {
		FetchSmsApiResponse response = new FetchSmsApiResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchSmsApiResponse getAllData() {
		Logger.info(
				"\r\n\r\n**************************** GET ALL SMS API DETAILS *************************************");

		FetchSmsApiResponse response = new FetchSmsApiResponse();
		try {
			List<SmsApiMaster> allApis = smsApiMasterRepository.findByStatus("Active");
			if (allApis.isEmpty()) {
				return buildErrorResponse(false, "No SMS API found.", "01", null, null);
			}

			List<SmsApiMasterModel> apiData = allApis.stream().map(this::convertToSmsApiModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", apiData, null);

		} catch (Exception e) {
			Logger.error("EXCEPTION: " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}
}
