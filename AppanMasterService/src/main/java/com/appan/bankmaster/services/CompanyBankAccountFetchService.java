package com.appan.bankmaster.services;

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
import com.appan.bankmaster.companybankaccount.model.CompanyBankAccountFetchRequest;
import com.appan.bankmaster.companybankaccount.model.FetchCompanyBankAccountModel;
import com.appan.bankmaster.companybankaccount.model.FetchCompanyBankAccountResponse;
import com.appan.bankmaster.entity.CompanyBankAccount;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.CompanyBankAccountRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class CompanyBankAccountFetchService {
	@Autowired
	private UserMasterRepository masterRepository;

	private static final Logger Logger = LoggerFactory.getLogger(CompanyBankAccountFetchService.class);

	@Autowired
	private CompanyBankAccountRepository companyBankAccountRepository;

	public FetchCompanyBankAccountResponse fetchCompanyBankAccount(CompanyBankAccountFetchRequest req, Integer pageNo,
			Integer pageSize) {
		Logger.info(
				"\r\n\r\n**************************** FETCH COMPANY BANK ACCOUNT DETAILS *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<CompanyBankAccount> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<CompanyBankAccount> pageResults = companyBankAccountRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No records found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<FetchCompanyBankAccountModel> data = pageResults.stream()
					.map(this::convertToFetchCompanyBankAccountModel).collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", data, pageDetails);
		} catch (Exception e) {
			Logger.error("Exception: ", e);
			return buildErrorResponse(false, "Exception occurred", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<CompanyBankAccount> root,
			CompanyBankAccountFetchRequest req) {
		List<Predicate> predicates = new ArrayList<>();

		// Case-insensitive filters for fields
		if (!Strings.isNullOrEmpty(req.getBankName())) {
			predicates.add(cb.like(cb.lower(root.get("bankName")), "%" + req.getBankName().toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(req.getBankCode())) {
			predicates.add(cb.like(cb.lower(root.get("bankCode")), "%" + req.getBankCode().toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(req.getAccName())) {
			predicates.add(cb.like(cb.lower(root.get("accName")), "%" + req.getAccName().toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(req.getAccNumber())) {
			predicates.add(cb.like(cb.lower(root.get("accNumber")), "%" + req.getAccNumber().toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(req.getIfscCode())) {
			predicates.add(cb.like(cb.lower(root.get("ifscCode")), "%" + req.getIfscCode().toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(req.getBranch())) {
			predicates.add(cb.like(cb.lower(root.get("branch")), "%" + req.getBranch().toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(req.getUpiId())) {
			predicates.add(cb.like(cb.lower(root.get("upiId")), "%" + req.getUpiId().toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(req.getType())) {
			predicates.add(cb.like(cb.lower(root.get("type")), "%" + req.getType().toLowerCase() + "%"));
		}
		if (!Strings.isNullOrEmpty(req.getAccType())) {
			predicates.add(cb.like(cb.lower(root.get("accType")), "%" + req.getAccType().toLowerCase() + "%"));
		}
		if (req.getIsActive() != null) {
			predicates.add(cb.equal(root.get("isActive"), req.getIsActive()));
		}
		if (req.getPriority() != null) {
			predicates.add(cb.equal(root.get("priority"), req.getPriority()));
		}

		// Date range filter
		if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date fromDate = dateFormat.parse(req.getFromDate());
				Date toDate = dateFormat.parse(req.getToDate());

				// Adjusting the toDate to include the entire day
				Calendar cal = Calendar.getInstance();
				cal.setTime(toDate);
				cal.add(Calendar.DAY_OF_MONTH, 1); // Add one day to include the entire day
				toDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), fromDate, toDate));
			} catch (ParseException e) {
				Logger.error("Date parsing error: ", e);
			}
		}

		return predicates;
	}

	private FetchCompanyBankAccountModel convertToFetchCompanyBankAccountModel(CompanyBankAccount model) {

		FetchCompanyBankAccountModel response = new FetchCompanyBankAccountModel();
		response.setId(model.getId());
		response.setBankName(model.getBankName());
		response.setBankCode(model.getBankCode());
		response.setAccName(model.getAccName());
		response.setAccNumber(model.getAccNumber());
		response.setIfscCode(model.getIfscCode());
		response.setBranch(model.getBranch());
		response.setUpiId(model.getUpiId());
		response.setType(model.getType());
		response.setAccType(model.getAccType());
		response.setUsername(model.getUsername());
		response.setPriority(model.getPriority());
		response.setIsActive(model.getIsActive());
		response.setCreatedBy(model.getCreatedBy());
		response.setCreatedDt(model.getCreatedDt());
		response.setModifyBy(model.getModifyBy());
		response.setModifyDt(model.getModifyDt());

		response.setAuthBy(model.getAuthBy());
		response.setAuthDate(model.getAuthDate());

		if (Strings.isNullOrEmpty(model.getAuthStatus())) {
			response.setAuthStatus("UNKNOWN");
		} else if (model.getAuthStatus().equalsIgnoreCase("1")) {
			response.setAuthStatus("ACTIVE");
		} else if (model.getAuthStatus().equalsIgnoreCase("3")) {
			response.setAuthStatus("BLOCK");
		} else if (model.getAuthStatus().equalsIgnoreCase("4")) {
			response.setAuthStatus("IN-ACTIVE");
		} else {
			response.setAuthStatus(model.getAuthStatus());
		}
		return response;
	}

	private FetchCompanyBankAccountResponse buildErrorResponse(boolean status, String message, String respCode,
			List<FetchCompanyBankAccountModel> data, PageDetails pageDetails) {
		FetchCompanyBankAccountResponse response = new FetchCompanyBankAccountResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchCompanyBankAccountResponse getAllCompanyBankAccounts() {
		Logger.info(
				"\r\n\r\n**************************** GET ALL COMPANY BANK ACCOUNTS *************************************");
		FetchCompanyBankAccountResponse response = new FetchCompanyBankAccountResponse();
		try {
			List<CompanyBankAccount> allData = companyBankAccountRepository.findAll();
			if (allData.isEmpty()) {
				return buildErrorResponse(false, "No data found.", "01", null, null);
			}

			List<FetchCompanyBankAccountModel> data = allData.stream().map(this::convertToFetchCompanyBankAccountModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", data, null);
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage("Exception occurred.");
			response.setRespCode("EX");
			return response;
		}
	}

}
