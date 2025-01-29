package com.appan.profilemaster.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.ProfilesMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.FetchProfilesMasterRequest;
import com.appan.profilemaster.model.FetchProfilesResponse;
import com.appan.profilemaster.model.ProfilesMasterModel;
import com.appan.repositories.Repositories.ProfilesMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchProfilesMasterService {

	@Autowired
	private ProfilesMasterRepository profilesMasterRepository;

	@Autowired
	private UserMasterRepository masterRepository;

	public FetchProfilesResponse fetch(FetchProfilesMasterRequest req, Integer pageNo, Integer pageSize) {
		try {
			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<ProfilesMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					return null;
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<ProfilesMaster> pageResult = profilesMasterRepository.findAll(specification, paging);
			if (pageResult.isEmpty()) {
				return buildErrorResponse(false, "Profiles not found.", "01", null, null);
			}

			List<ProfilesMasterModel> profilesModels = pageResult.stream().map(this::convertToProfilesMasterModel)
					.collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResult.getNumber() + 1);
			pageDetails.setTotalRecords(pageResult.getTotalElements());
			pageDetails.setNoOfPages(pageResult.getTotalPages());
			pageDetails.setPageSize(pageResult.getSize());

			return buildErrorResponse(true, "SUCCESS", "00", profilesModels, pageDetails);

		} catch (Exception e) {
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ProfilesMaster> root,
			FetchProfilesMasterRequest req) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (req.getFromDate() != null && req.getToDate() != null) {
			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (req.getProfileName() != null && !"all".equalsIgnoreCase(req.getProfileName())) {
			predicates.add(cb.like(cb.lower(root.get("profileName")), "%" + req.getProfileName().toLowerCase() + "%"));
		}

		if (req.getCode() != null && !"all".equalsIgnoreCase(req.getCode())) {
			predicates.add(cb.like(cb.lower(root.get("code")), "%" + req.getCode().toLowerCase() + "%"));
		}

		if (req.getUserType() != null && !"all".equalsIgnoreCase(req.getUserType())) {
			predicates.add(cb.like(cb.lower(root.get("userType")), "%" + req.getUserType().toLowerCase() + "%"));
		}

		if (req.getStatus() != null && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.like(cb.lower(root.get("status")), "%" + req.getStatus().toLowerCase() + "%"));
		}

		return predicates;
	}

	private ProfilesMasterModel convertToProfilesMasterModel(ProfilesMaster model) {
		ProfilesMasterModel profilesModel = new ProfilesMasterModel();
		profilesModel.setId(model.getId());
		profilesModel.setProfileName(model.getProfileName());
		profilesModel.setCode(model.getCode());
		profilesModel.setUserType(model.getUserType());
		profilesModel.setSignupProfile(model.getSignupProfile());
		profilesModel.setStatus(model.getStatus());
		profilesModel.setCreatedBy(model.getCreatedBy());
		profilesModel.setCreatedDt(model.getCreatedDt());
		profilesModel.setModifyBy(model.getModifyBy());
		profilesModel.setModifyDt(model.getModifyDt());
		profilesModel.setAuthBy(model.getAuthBy());
		profilesModel.setAuthDate(model.getAuthDate());
		if (Strings.isNullOrEmpty(model.getAuthStatus())) {
			profilesModel.setAuthStatus("UNKNOWN");
		} else if (model.getAuthStatus().equalsIgnoreCase("1")) {
			profilesModel.setAuthStatus("ACTIVE");
		} else if (model.getAuthStatus().equalsIgnoreCase("3")) {
			profilesModel.setAuthStatus("BLOCK");
		} else if (model.getAuthStatus().equalsIgnoreCase("4")) {
			profilesModel.setAuthStatus("IN-ACTIVE");
		} else {
			profilesModel.setAuthStatus(model.getAuthStatus());
		}

		return profilesModel;
	}

	private FetchProfilesResponse buildErrorResponse(boolean status, String message, String respCode,
			List<ProfilesMasterModel> data, PageDetails pageDetails) {
		FetchProfilesResponse response = new FetchProfilesResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchProfilesResponse getAllData() {
		FetchProfilesResponse response = new FetchProfilesResponse();
		try {
			List<ProfilesMaster> profilesList = profilesMasterRepository.findByStatus("Active");
			if (profilesList.isEmpty()) {
				return buildErrorResponse(false, "Profiles not found.", "01", null, null);
			}

			List<ProfilesMasterModel> profilesModels = profilesList.stream().map(this::convertToProfilesMasterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", profilesModels, null);

		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
