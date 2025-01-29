package com.appan.bankmaster.userbank.services;

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
import com.appan.bankmaster.entity.UserBank;
import com.appan.bankmaster.userbank.model.FetchUserBankResponse;
import com.appan.bankmaster.userbank.model.UserBankFetchRequest;
import com.appan.bankmaster.userbank.model.UserBankModel;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserBankRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UserBankFetchService {

	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserBankFetchService.class);

	@Autowired
	private UserBankRepository userBankRepository;

	public FetchUserBankResponse fetchUserBank(UserBankFetchRequest req, Integer pageNo, Integer pageSize) {
		logger.info("Fetching user bank details with request: {}", req);

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<UserBank> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = new ArrayList<>();
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					logger.error("Error parsing date filters", e);
					return null;
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				logger.info("Generated predicates: {}", predicates);
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<UserBank> pageResult = userBankRepository.findAll(specification, paging);
			logger.info("Total records found: {}", pageResult.getTotalElements());

			if (pageResult.isEmpty()) {
				return buildErrorResponse(false, "User bank details not found.", "01", null, null);
			}

			List<UserBankModel> userBankModels = pageResult.stream().map(this::convertToUserBankModel)
					.collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResult.getNumber() + 1);
			pageDetails.setTotalRecords(pageResult.getTotalElements());
			pageDetails.setNoOfPages(pageResult.getTotalPages());
			pageDetails.setPageSize(pageResult.getSize());

			return buildErrorResponse(true, "SUCCESS", "00", userBankModels, pageDetails);

		} catch (Exception e) {
			logger.error("Exception occurred while fetching user bank details: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<UserBank> root, UserBankFetchRequest req)
			throws ParseException {
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

		if (!Strings.isNullOrEmpty(req.getBankName())) {
			predicates.add(cb.like(cb.lower(root.get("bankName")), "%" + req.getBankName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getBankCode())) {
			predicates.add(cb.like(cb.lower(root.get("bankCode")), "%" + req.getBankCode().toLowerCase() + "%"));
		}

		if (req.getIsActive() != null) {
			predicates.add(cb.equal(root.get("isActive"), req.getIsActive()));
		}

		return predicates;
	}

	private UserBankModel convertToUserBankModel(UserBank userBank) {
		UserBankModel model = new UserBankModel();
		model.setId(userBank.getId());
		model.setBankName(userBank.getBankName());
		model.setBankCode(userBank.getBankCode());
		model.setAccName(userBank.getAccName());
		model.setAccNumber(userBank.getAccNumber());
		model.setIfscCode(userBank.getIfscCode());
		model.setBranch(userBank.getBranch());
		model.setUpiId(userBank.getUpiId());
		model.setType(userBank.getType());
		model.setAccType(userBank.getAccType());
		model.setUsername(userBank.getUsername());
		model.setFile(userBank.getFile());
		model.setPriority(userBank.getPriority());
		model.setIsActive(userBank.getIsActive());
		model.setCreatedBy(userBank.getCreatedBy());
		model.setCreatedDt(userBank.getCreatedDt());
		model.setModifyBy(userBank.getModifyBy());
		model.setModifyDt(userBank.getModifyDt());
		model.setAuthBy(userBank.getAuthBy());
		model.setAuthDate(userBank.getAuthDate());
		if (Strings.isNullOrEmpty(userBank.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (userBank.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (userBank.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (userBank.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(userBank.getAuthStatus());
		}
		return model;
	}

	private FetchUserBankResponse buildErrorResponse(boolean status, String message, String respCode,
			List<UserBankModel> data, PageDetails pageDetails) {
		FetchUserBankResponse response = new FetchUserBankResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchUserBankResponse getAllData() {
		logger.info(
				"\r\n\r\n**************************** FETCH ALL USER BANK DETAILS *************************************");

		FetchUserBankResponse response = new FetchUserBankResponse();
		try {
			List<UserBank> list = userBankRepository.findAll();
			if (list.isEmpty()) {
				return buildErrorResponse(false, "Pincode details not found.", "01", null, null);
			}

			List<UserBankModel> models = list.stream().map(this::convertToUserBankModel).collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", models, null);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}
}
