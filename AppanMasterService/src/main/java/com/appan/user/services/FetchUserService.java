package com.appan.user.services;

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
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.repositories.Repositories.UserMenuRepository;
import com.appan.user.model.FetchUserData;
import com.appan.user.model.FetchUserRequest;
import com.appan.user.model.FetchUserResponse;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FetchUserService {

	public static final GsonBuilder GSONBUILDER = new GsonBuilder();

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private UserMenuRepository menuRepository;

	private static final Logger Logger = LoggerFactory.getLogger(FetchUserService.class);

	public FetchUserResponse fetchUser(FetchUserRequest req, Integer pageNo, Integer pageSize) {
		Logger.info("\r\n\r\n**************************** FETCH USER *************************************");
		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			if (master.getUserProfile() != null && !master.getUserProfile().equalsIgnoreCase("OPERATION")) {
				req.setUserProfile(master.getUserProfile());
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<UserMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req.getUserId(), req.getMobileNo(),
							req.getUserProfile(), req.getStatus(), req.getFromDate(), req.getToDate());
					query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
					return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				} catch (ParseException e) {
					e.printStackTrace();
					Logger.info("EXCEPTION : " + e);
					return null;
				}
			};

			Page<UserMaster> ums = masterRepository.findAll(specification, paging);
			if (ums.isEmpty()) {
				return buildErrorResponse(false, "User " + ErrorMessages.NOT_FOUND, "01", null, null);
			}

			PageDetails details = new PageDetails();
			details.setPageNo(ums.getNumber() + 1);
			details.setTotalRecords(ums.getTotalElements());
			details.setNoOfPages(ums.getTotalPages());
			details.setPageSize(ums.getSize());

			List<FetchUserData> fetchUserDatas = ums.stream().map(user -> {
				FetchUserData data = convertToBankModel(user);

				String roleName = menuRepository.findRoleNameByUserRoleAndUserProfile(user.getUserRole(),
						user.getUserProfile());
				data.setRoleName(roleName);

				return data;
			}).collect(Collectors.toList());

			return buildErrorResponse(true, ErrorMessages.SUCCESS, "00", fetchUserDatas, details);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("EXCEPTION : " + e);
			return buildErrorResponse(false, ErrorMessages.EXCEPTION, "03", null, null);
		}
	}

	private FetchUserData convertToBankModel(UserMaster model) {
		FetchUserData bankModel = new FetchUserData();
		bankModel.setUserId(model.getUserId());
		bankModel.setUserCode(model.getUserCode());
		bankModel.setName(model.getName());
		bankModel.setMobileNo(model.getMobileNo());
		bankModel.setEmailId(model.getEmailId());
		bankModel.setUserRole(model.getUserRole());
		bankModel.setUserProfile(model.getUserProfile());
		bankModel.setCreatedBy(model.getCreatedBy());
		bankModel.setCreatedDt(model.getCreatedDt());
		bankModel.setModifyBy(model.getModifyBy());
		bankModel.setModifyDt(model.getModifyDt());
		bankModel.setAuthBy(model.getAuthBy());
		bankModel.setAuthDt(model.getAuthDt());
		if (Strings.isNullOrEmpty(model.getAuthStatus())) {
			bankModel.setAuthStatus("UNKNOWN");
		} else if (model.getAuthStatus().equalsIgnoreCase("1")) {
			bankModel.setAuthStatus("ACTIVE");
		} else if (model.getAuthStatus().equalsIgnoreCase("2")) {
			bankModel.setAuthStatus("NEW-USER");
		} else if (model.getAuthStatus().equalsIgnoreCase("3")) {
			bankModel.setAuthStatus("BLOCK");
		} else if (model.getAuthStatus().equalsIgnoreCase("4")) {
			bankModel.setAuthStatus("IN-ACTIVE");
		} else {
			bankModel.setAuthStatus(model.getAuthStatus());
		}

		return bankModel;
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<UserMaster> root, String userName, String mobileNo,
			String userProfile, String authStatus, String fromDt, String toDt) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();

		if (!Strings.isNullOrEmpty(fromDt) && !Strings.isNullOrEmpty(toDt)) {
			SimpleDateFormat stDf = new SimpleDateFormat("dd-MM-yyyy");
			Date startDate = stDf.parse(fromDt);
			Date endDate = stDf.parse(toDt);

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), cb.literal(startDate), cb.literal(endDate)));
		}

		if (!Strings.isNullOrEmpty(userName) && (!"all".equalsIgnoreCase(userName))) {
			predicates.add(cb.equal(root.get("userId"), userName.toUpperCase()));
		}

		if (!Strings.isNullOrEmpty(mobileNo) && (!"all".equalsIgnoreCase(mobileNo))) {
			predicates.add(cb.equal(root.get("mobileNo"), mobileNo));
		}

		if (!Strings.isNullOrEmpty(userProfile) && (!"all".equalsIgnoreCase(userProfile))) {
			predicates.add(cb.equal(root.get("userProfile"), userProfile.toUpperCase()));
		}

		if (!Strings.isNullOrEmpty(authStatus) && (!"all".equalsIgnoreCase(authStatus))) {
			predicates.add(cb.equal(root.get("authStatus"), authStatus.trim()));
		}
		return predicates;
	}

	private FetchUserResponse buildErrorResponse(boolean status, String message, String respCode,
			List<FetchUserData> data, PageDetails pageDetails) {
		FetchUserResponse response = new FetchUserResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

}
