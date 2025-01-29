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

import com.appan.ErrorMessages;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.entity.UserMenu;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.userconfig.models.FetchUserTypeRequest;
import com.appan.userconfig.models.FetchUserTypeResponse;
import com.appan.userconfig.models.UserTypeModel;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchUserTypeService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserMenuRepository userMenuRepository;

	private static final Logger logger = LoggerFactory.getLogger(FetchUserTypeService.class);

	public FetchUserTypeResponse fetch(FetchUserTypeRequest req, Integer pageNo, Integer pageSize) {
		logger.info("Fetching user type details with filters and pagination");

		try {

			UserMaster master = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<UserMenu> specification = (root, query, criteriaBuilder) -> {
				try {
					List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (Exception e) {
					logger.error("Exception while building predicates: ", e);
					return null;
				}
			};

			Page<UserMenu> userTypePage = userMenuRepository.findAll(specification, paging);

			if (userTypePage.isEmpty()) {
				return buildResponse(false, "No user type records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(userTypePage.getNumber() + 1);
			pageDetails.setTotalRecords(userTypePage.getTotalElements());
			pageDetails.setNoOfPages(userTypePage.getTotalPages());
			pageDetails.setPageSize(userTypePage.getSize());

			List<UserTypeModel> userData = userTypePage.stream().map(this::convertToUserTypeModel)
					.collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", userData, pageDetails);

		} catch (Exception e) {
			logger.error("Exception in fetch service: ", e);
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<UserMenu> root, FetchUserTypeRequest req)
			throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(req.getRoleName())) {
			predicates.add(cb.like(cb.lower(root.get("roleName")), "%" + req.getRoleName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getUserProfile())) {
			predicates.add(cb.like(cb.lower(root.get("userProfile")), "%" + req.getUserProfile().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (!Strings.isNullOrEmpty(req.getStatus())) {
			predicates.add(cb.equal(root.get("status"), req.getStatus()));
		}

		return predicates;
	}

	private UserTypeModel convertToUserTypeModel(UserMenu userType) {
		UserTypeModel model = new UserTypeModel();
		model.setMenuId(userType.getMenuId());
		model.setRoleName(userType.getRoleName());
		model.setUserProfile(userType.getUserProfile());
		model.setUserRole(userType.getUserRole());
		model.setMenu(userType.getMenu());
		model.setRank(userType.getRank());
		model.setIsUser(userType.getIsUser());
		model.setIsAllow(userType.getIsAllow());
		model.setIsCommission(userType.getIsCommission());
		model.setStatus(userType.getStatus());
		model.setCreatedBy(userType.getCreatedBy());
		model.setCreatedDt(userType.getCreatedDt());
		model.setModifyBy(userType.getModifyBy());
		model.setModifyDt(userType.getModifyDt());
		model.setAuthBy(userType.getAuthBy());
		model.setAuthDt(userType.getAuthDt());
		model.setAuthStatus(userType.getAuthStatus());
		return model;
	}

	private FetchUserTypeResponse buildResponse(boolean status, String message, String respCode,
			List<UserTypeModel> data, PageDetails pageDetails) {
		FetchUserTypeResponse response = new FetchUserTypeResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchUserTypeResponse getAllData() {
		logger.info("Fetching all user type data without filters");
		try {
			List<UserMenu> userTypeList = userMenuRepository.findByAuthStatus("1");
			if (userTypeList.isEmpty()) {
				return buildResponse(false, "No user type records found.", "01", null, null);
			}

			List<UserTypeModel> userData = userTypeList.stream().map(this::convertToUserTypeModel)
					.collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", userData, null);

		} catch (Exception e) {
			logger.error("Exception in getAllData service: ", e);
			return buildResponse(false, "EXCEPTION", "03", null, null);
		}
	}
}
