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
import com.appan.apimaster.models.AndroidSettingMasterModel;
import com.appan.apimaster.models.FetchAndroidSettingMasterReq;
import com.appan.apimaster.models.FetchAndroidSettingResponse;
import com.appan.apimaster.repositories.ApiRepositories.AndroidSettingMasterRepository;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.AndroidSettingMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class AndroidSettingMasterFetchService {

	private static final Logger Logger = LoggerFactory.getLogger(AndroidSettingMasterFetchService.class);

	@Autowired
	private AndroidSettingMasterRepository androidSettingMasterRepository;

	@Autowired
	private UserMasterRepository masterRepository;

	public FetchAndroidSettingResponse fetchAndroidSetting(FetchAndroidSettingMasterReq req, Integer pageNo,
			Integer pageSize) {
		Logger.info("**************************** FETCH ANDROID SETTINGS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<AndroidSettingMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getTenantName(),
						req.getSenderId(), req.getServerKey(), req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<AndroidSettingMaster> pageResults = androidSettingMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No Android settings found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<AndroidSettingMasterModel> settingData = pageResults.stream().map(this::convertToAndroidSettingModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", settingData, pageDetails);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<AndroidSettingMaster> root, String tenantName,
			String senderId, String serverKey, String status, String fromDate, String toDate) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (fromDate != null && toDate != null) {
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

		// Other filters based on the request fields
		if (tenantName != null && !tenantName.equalsIgnoreCase("all")) {
			predicates.add(cb.equal(cb.lower(root.get("tenantName")), tenantName.toLowerCase()));
		}
		if (senderId != null && !senderId.equalsIgnoreCase("all")) {
			predicates.add(cb.equal(cb.lower(root.get("senderId")), senderId.toLowerCase()));
		}
		if (serverKey != null && !serverKey.equalsIgnoreCase("all")) {
			predicates.add(cb.equal(cb.lower(root.get("serverKey")), serverKey.toLowerCase()));
		}
		if (status != null && !status.equalsIgnoreCase("all")) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private AndroidSettingMasterModel convertToAndroidSettingModel(AndroidSettingMaster androidSettingMaster) {
		AndroidSettingMasterModel model = new AndroidSettingMasterModel();
		model.setId(androidSettingMaster.getId());
		model.setTenantName(androidSettingMaster.getTenantName());
		model.setSenderId(androidSettingMaster.getSenderId());
		model.setServerKey(androidSettingMaster.getServerKey());
		model.setStatus(androidSettingMaster.getStatus());
		model.setCreatedBy(androidSettingMaster.getCreatedBy());
		model.setCreatedDt(androidSettingMaster.getCreatedDt());
		model.setModifyBy(androidSettingMaster.getModifyBy());
		model.setModifyDt(androidSettingMaster.getModifyDt());
		model.setAuthBy(androidSettingMaster.getAuthBy());
		model.setAuthDate(androidSettingMaster.getAuthDate());
		if (Strings.isNullOrEmpty(androidSettingMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (androidSettingMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (androidSettingMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (androidSettingMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(androidSettingMaster.getAuthStatus());
		}
		return model;
	}

	private FetchAndroidSettingResponse buildErrorResponse(boolean status, String message, String respCode,
			List<AndroidSettingMasterModel> data, PageDetails pageDetails) {
		FetchAndroidSettingResponse response = new FetchAndroidSettingResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchAndroidSettingResponse getAllData() {
		Logger.info("**************************** GET ALL ANDROID SETTINGS *************************************");

		FetchAndroidSettingResponse response = new FetchAndroidSettingResponse();
		try {
			List<AndroidSettingMaster> allSettings = androidSettingMasterRepository.findByStatus("Active");
			if (allSettings.isEmpty()) {
				return buildErrorResponse(false, "No Android settings found.", "01", null, null);
			}

			List<AndroidSettingMasterModel> settingData = allSettings.stream().map(this::convertToAndroidSettingModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", settingData, null);

		} catch (Exception e) {
			Logger.error("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
