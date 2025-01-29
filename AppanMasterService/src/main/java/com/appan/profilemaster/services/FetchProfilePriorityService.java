package com.appan.profilemaster.services;

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
import com.appan.entity.ProfilePriorityMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.FetchProfilePriorityRequest;
import com.appan.profilemaster.model.FetchProfilePriorityResponse;
import com.appan.profilemaster.model.ProfilePriorityMasterModel;
import com.appan.repositories.Repositories.ProfilePriorityMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchProfilePriorityService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private ProfilePriorityMasterRepository profilePriorityMasterRepository;

	public static final Logger logger = LoggerFactory.getLogger(FetchProfilePriorityService.class);

	public FetchProfilePriorityResponse fetch(FetchProfilePriorityRequest req, Integer pageNo, Integer pageSize) {
		logger.info(
				"\r\n\r\n**************************** FETCH PROFILE PRIORITY DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<ProfilePriorityMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					logger.error("Error parsing date filters", e);
					return null;
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<ProfilePriorityMaster> pageResult = profilePriorityMasterRepository.findAll(specification, paging);
			if (pageResult.isEmpty()) {
				return buildErrorResponse(false, "Profile Priority details not found.", "01", null, null);
			}

			List<ProfilePriorityMasterModel> profilePriorityModels = pageResult.stream()
					.map(this::convertToProfilePriorityMasterModel).collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResult.getNumber() + 1);
			pageDetails.setTotalRecords(pageResult.getTotalElements());
			pageDetails.setNoOfPages(pageResult.getTotalPages());
			pageDetails.setPageSize(pageResult.getSize());

			return buildErrorResponse(true, "SUCCESS", "00", profilePriorityModels, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ProfilePriorityMaster> root,
			FetchProfilePriorityRequest req) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (!Strings.isNullOrEmpty(req.getUserName())) {
			predicates.add(cb.like(cb.lower(root.get("userName")), "%" + req.getUserName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.like(cb.lower(root.get("status")), "%" + req.getStatus().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getUserType()) && !"all".equalsIgnoreCase(req.getUserType())) {
			predicates.add(cb.like(cb.lower(root.get("userType")), "%" + req.getUserType().toLowerCase() + "%"));
		}

		return predicates;
	}

	private ProfilePriorityMasterModel convertToProfilePriorityMasterModel(ProfilePriorityMaster model) {
		ProfilePriorityMasterModel profilePriorityModel = new ProfilePriorityMasterModel();
		profilePriorityModel.setId(model.getId());
		profilePriorityModel.setUserType(model.getUserType());
		profilePriorityModel.setUserName(model.getUserName());
		profilePriorityModel.setAllowedProfile1(model.getAllowedProfile1());
		profilePriorityModel.setAllowedProfile2(model.getAllowedProfile2());
		profilePriorityModel.setAllowedProfile3(model.getAllowedProfile3());
		profilePriorityModel.setAllowedProfile4(model.getAllowedProfile4());
		profilePriorityModel.setAllowedProfile5(model.getAllowedProfile5());
		profilePriorityModel.setStatus(model.getStatus());
		profilePriorityModel.setCreatedBy(model.getCreatedBy());
		profilePriorityModel.setCreatedDt(model.getCreatedDt());
		profilePriorityModel.setModifyBy(model.getModifyBy());
		profilePriorityModel.setModifyDt(model.getModifyDt());
		profilePriorityModel.setAuthBy(model.getAuthBy());
		profilePriorityModel.setAuthDate(model.getAuthDate());

		if (Strings.isNullOrEmpty(model.getAuthStatus())) {
			profilePriorityModel.setAuthStatus("UNKNOWN");
		} else if (model.getAuthStatus().equalsIgnoreCase("1")) {
			profilePriorityModel.setAuthStatus("ACTIVE");
		} else if (model.getAuthStatus().equalsIgnoreCase("3")) {
			profilePriorityModel.setAuthStatus("BLOCK");
		} else if (model.getAuthStatus().equalsIgnoreCase("4")) {
			profilePriorityModel.setAuthStatus("IN-ACTIVE");
		} else {
			profilePriorityModel.setAuthStatus(model.getAuthStatus());
		}

		return profilePriorityModel;
	}

	private FetchProfilePriorityResponse buildErrorResponse(boolean status, String message, String respCode,
			List<ProfilePriorityMasterModel> data, PageDetails pageDetails) {
		FetchProfilePriorityResponse response = new FetchProfilePriorityResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchProfilePriorityResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** GET ALL PROFILE PRIORITY DETAILS *************************************");
		FetchProfilePriorityResponse response = new FetchProfilePriorityResponse();
		try {
			List<ProfilePriorityMaster> profilePriorityList = profilePriorityMasterRepository.findByStatus("Active");
			if (profilePriorityList.isEmpty()) {
				return buildErrorResponse(false, "Profile Priority details not found.", "01", null, null);
			}

			List<ProfilePriorityMasterModel> profilePriorityModels = profilePriorityList.stream()
					.map(this::convertToProfilePriorityMasterModel).collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", profilePriorityModels, null);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
