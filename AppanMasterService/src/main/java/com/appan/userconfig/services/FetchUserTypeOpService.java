package com.appan.userconfig.services;

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

import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.entity.UserTypeOperation;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserTypeOperationRepository;
import com.appan.userconfig.models.FetchUserTypeOpRequest;
import com.appan.userconfig.models.FetchUserTypeOpResponse;
import com.appan.userconfig.models.UserTypeOpModel;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchUserTypeOpService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private UserTypeOperationRepository userTypeOpRepository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchUserTypeOpService.class);

	public FetchUserTypeOpResponse fetch(FetchUserTypeOpRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("Fetching user type operations with filters...");
		try {
			UserMaster user = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, "Invalid Username", "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<UserTypeOperation> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (Exception e) {
					Logger.error("Error building predicates", e);
					return null;
				}
			};

			Page<UserTypeOperation> userPage = userTypeOpRepository.findAll(specification, paging);

			if (userPage.isEmpty()) {
				return buildErrorResponse(false, "No records found", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(userPage.getNumber() + 1);
			pageDetails.setTotalRecords(userPage.getTotalElements());
			pageDetails.setNoOfPages(userPage.getTotalPages());
			pageDetails.setPageSize(userPage.getSize());

			List<UserTypeOpModel> userData = userPage.stream().map(this::convertToUserTypeOpModel)
					.collect(Collectors.toList());

			return buildSuccessResponse(true, "SUCCESS", "00", userData, pageDetails);

		} catch (Exception e) {
			Logger.error("Error fetching data", e);
			return buildErrorResponse(false, "Exception", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<UserTypeOperation> root,
			FetchUserTypeOpRequest req) throws ParseException {
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

		if (!Strings.isNullOrEmpty(req.getOperationName())) {
			predicates.add(
					cb.like(cb.lower(root.get("operationName")), "%" + req.getOperationName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getUserTypeName())) {
			predicates
					.add(cb.like(cb.lower(root.get("userTypeName")), "%" + req.getUserTypeName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getStatus())) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		return predicates;
	}

	private UserTypeOpModel convertToUserTypeOpModel(UserTypeOperation userTypeOp) {
		UserTypeOpModel model = new UserTypeOpModel();
		model.setId(userTypeOp.getId());
		model.setOperationName(userTypeOp.getOperationName());
		model.setUserTypeName(userTypeOp.getUserTypeName());
		model.setStartDate(userTypeOp.getStartDate());
		model.setEndDate(userTypeOp.getEndDate());
		model.setIsDateValidity(userTypeOp.getIsDateValidity());
		model.setStatus(userTypeOp.getStatus());
		model.setCreatedBy(userTypeOp.getCreatedBy());
		model.setCreatedDt(userTypeOp.getCreatedDt());
		model.setModifyBy(userTypeOp.getModifyBy());
		model.setModifyDt(userTypeOp.getModifyDt());
		model.setAuthBy(userTypeOp.getAuthBy());
		model.setAuthDt(userTypeOp.getAuthDt());
		model.setAuthStatus(userTypeOp.getAuthStatus());
		return model;
	}

	private FetchUserTypeOpResponse buildErrorResponse(boolean status, String message, String respCode,
			List<UserTypeOpModel> data, PageDetails pageDetails) {
		FetchUserTypeOpResponse response = new FetchUserTypeOpResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	private FetchUserTypeOpResponse buildSuccessResponse(boolean status, String message, String respCode,
			List<UserTypeOpModel> data, PageDetails pageDetails) {
		return buildErrorResponse(status, message, respCode, data, pageDetails);
	}

	public FetchUserTypeOpResponse getAllData() {
		Logger.info("Fetching all user type operations...");
		try {
			List<UserTypeOperation> userList = userTypeOpRepository.findByAuthStatus("1");
			if (userList.isEmpty()) {
				return buildErrorResponse(false, "No records found", "01", null, null);
			}

			List<UserTypeOpModel> userData = userList.stream().map(this::convertToUserTypeOpModel)
					.collect(Collectors.toList());

			return buildSuccessResponse(true, "SUCCESS", "00", userData, null);
		} catch (Exception e) {
			Logger.error("Error fetching all data", e);
			return buildErrorResponse(false, "Exception", "03", null, null);
		}
	}
}
