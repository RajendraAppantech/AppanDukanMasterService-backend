package com.appan.usermanagement.services;

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
import com.appan.entity.UserManagementMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.usermanagement.models.UserManagementFetchRequest;
import com.appan.usermanagement.models.UserManagementFetchResponse;
import com.appan.usermanagement.repo.ManageUserRepositories.UserManagementMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UserManagementSearchUserService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserManagementMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(UserManagementSearchUserService.class);

	public UserManagementFetchResponse search(UserManagementFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<UserManagementMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getUserId(),
						req.getMobileNumber(), req.getEmailId(), req.getPanNo(), req.getAdhaarNo(), req.getStatus(),
						req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<UserManagementMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "user not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<UserManagementMaster> StepsData = pageResults.stream().map(this::convertToStepsModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", StepsData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<UserManagementMaster> root, String userId,
			String mobileNumber, String emailId, String panAadharCardNo, String aadharNo, String status,
			String fromDate, String toDate) {
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

		if (!Strings.isNullOrEmpty(userId) && !"all".equalsIgnoreCase(userId)) {
			predicates.add(cb.equal(cb.lower(root.get("userId")), userId.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(mobileNumber) && !"all".equalsIgnoreCase(mobileNumber)) {
			predicates.add(cb.equal(cb.lower(root.get("mobileNumber")), mobileNumber.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(emailId) && !"all".equalsIgnoreCase(emailId)) {
			predicates.add(cb.equal(cb.lower(root.get("emailId")), emailId.toLowerCase()));
		}

		if (!Strings.isNullOrEmpty(aadharNo) && !"all".equalsIgnoreCase(aadharNo)) {
			predicates.add(cb.equal(cb.lower(root.get("aadharNo")), aadharNo.toLowerCase()));
		}

		if (panAadharCardNo != null && !panAadharCardNo.isEmpty() && !"all".equalsIgnoreCase(panAadharCardNo)) {
			predicates.add(cb.equal(cb.lower(root.get("panAadharCardNo")), panAadharCardNo.toLowerCase()));
		}

		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private UserManagementMaster convertToStepsModel(UserManagementMaster UserManagementMaster) {
		UserManagementMaster model = UserManagementMaster;
		if (Strings.isNullOrEmpty(UserManagementMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (UserManagementMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (UserManagementMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (UserManagementMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(UserManagementMaster.getAuthStatus());
		}

		return model;
	}

	private UserManagementFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<UserManagementMaster> data, PageDetails pageDetails) {
		UserManagementFetchResponse response = new UserManagementFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}
}
