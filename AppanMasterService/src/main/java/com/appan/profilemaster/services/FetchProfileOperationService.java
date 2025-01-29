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
import com.appan.entity.ProfileOperationMaster;
import com.appan.entity.UserMaster;
import com.appan.profilemaster.model.FetchProfileOperationRequest;
import com.appan.profilemaster.model.FetchProfileOperationsResponse;
import com.appan.profilemaster.model.ProfileOperationsMasterModel;
import com.appan.repositories.Repositories.ProfileOperationMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchProfileOperationService {

	@Autowired
	private ProfileOperationMasterRepository profileOperationsRepository;

	@Autowired
	private UserMasterRepository masterRepository;

	public FetchProfileOperationsResponse fetch(FetchProfileOperationRequest req, Integer pageNo, Integer pageSize) {
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<ProfileOperationMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = null;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<ProfileOperationMaster> pageResults = profileOperationsRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildResponse(false, "No profiles found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<ProfileOperationsMasterModel> profileData = pageResults.stream().map(this::convertToProfileModel)
					.collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", profileData, pageDetails);

		} catch (Exception e) {
			e.printStackTrace();
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ProfileOperationMaster> root,
			FetchProfileOperationRequest req) throws ParseException {
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

		if (!Strings.isNullOrEmpty(req.getUsername())) {
			predicates.add(cb.equal(root.get("createdBy"), req.getUsername()));
		}

		if (!Strings.isNullOrEmpty(req.getOperationName())) {
			predicates.add(
					cb.like(cb.lower(root.get("operationName")), "%" + req.getOperationName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getProfileName())) {
			predicates.add(cb.like(cb.lower(root.get("profileName")), "%" + req.getProfileName().toLowerCase() + "%"));
		}

		if (req.getIsOnlyUi() != null) {
			predicates.add(cb.equal(root.get("isOnlyUi"), req.getIsOnlyUi()));
		}

		if (req.getIsDateValidity() != null) {
			predicates.add(cb.equal(root.get("isDateValidity"), req.getIsDateValidity()));
		}

		if (!Strings.isNullOrEmpty(req.getStatus())) {
			predicates.add(cb.equal(root.get("status"), req.getStatus()));
		}

		return predicates;
	}

	private ProfileOperationsMasterModel convertToProfileModel(ProfileOperationMaster entity) {
		ProfileOperationsMasterModel model = new ProfileOperationsMasterModel();
		model.setId(entity.getId());
		model.setOperationName(entity.getOperationName());
		model.setProfileName(entity.getProfileName());
		model.setStartDate(entity.getStartDate());
		model.setEndDate(entity.getEndDate());
		model.setIsDateValidity(entity.getIsDateValidity());
		model.setIsOnlyUi(entity.getIsOnlyUi());
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

	private FetchProfileOperationsResponse buildResponse(boolean status, String message, String respCode,
			List<ProfileOperationsMasterModel> data, PageDetails pageDetails) {
		FetchProfileOperationsResponse response = new FetchProfileOperationsResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchProfileOperationsResponse getAllData() {

		try {
			List<ProfileOperationMaster> allProfiles = profileOperationsRepository.findByStatus("Active");
			if (allProfiles.isEmpty()) {
				return buildResponse(false, "No profile operations found.", "01", null, null);
			}

			List<ProfileOperationsMasterModel> profileData = allProfiles.stream().map(this::convertToProfileModel)
					.collect(Collectors.toList());
			return buildResponse(true, "SUCCESS", "00", profileData, null);

		} catch (Exception e) {
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}
