package com.appan.apimaster.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.appan.apimaster.models.FetchTransactionApiResponse;
import com.appan.apimaster.models.TransactionApiFetchRequest;
import com.appan.apimaster.models.TransactionApiMasterModel;
import com.appan.apimaster.repositories.ApiRepositories.TransactionApiMasterRepository;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.TransactionApiMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class TransactionApiFetchService {

	private static final Logger Logger = LoggerFactory.getLogger(TransactionApiFetchService.class);

	@Autowired
	private TransactionApiMasterRepository transactionApiMasterRepository;

	@Autowired
	private UserMasterRepository masterRepository;

	public FetchTransactionApiResponse fetchTransactionApi(TransactionApiFetchRequest req, Integer pageNo,
			Integer pageSize) {
		Logger.info("**************************** FETCH TRANSACTION API *************************************");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}
			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<TransactionApiMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<TransactionApiMaster> pageResults = transactionApiMasterRepository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No data found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<TransactionApiMasterModel> apiData = pageResults.stream().map(this::convertToTransactionApiMasterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", apiData, pageDetails);
		} catch (Exception e) {
			Logger.error("Exception: " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<TransactionApiMaster> root,
			TransactionApiFetchRequest req) {
		List<Predicate> predicates = new ArrayList<>();

		if (req.getApiName() != null && !req.getApiName().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("apiName")), req.getApiName().toLowerCase()));
		}
		if (req.getBalance() != null) {
			predicates.add(cb.equal(root.get("balance"), req.getBalance()));
		}
		if (req.getCode() != null && !req.getCode().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("code")), req.getCode().toLowerCase()));
		}
		if (req.getIsActive() != null) {
			predicates.add(cb.equal(root.get("isActive"), req.getIsActive()));
		}
		if (req.getIsUserwise() != null) {
			predicates.add(cb.equal(root.get("isUserwise"), req.getIsUserwise()));
		}
		if (req.getType() != null && !req.getType().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("type")), req.getType().toLowerCase()));
		}
		if (req.getStatus() != null && !req.getStatus().isEmpty()) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}
		if (req.getFromDate() != null && !req.getFromDate().isEmpty() && req.getToDate() != null
				&& !req.getToDate().isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date fromDate = dateFormat.parse(req.getFromDate());
				Date toDate = dateFormat.parse(req.getToDate());

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				toDate = cal.getTime();

				predicates.add(cb.between(root.get("createdDt"), fromDate, toDate));
			} catch (ParseException e) {
				Logger.error("Error parsing date range: ", e);
			}
		}

		return predicates;
	}

	private TransactionApiMasterModel convertToTransactionApiMasterModel(TransactionApiMaster transactionApiMaster) {
		TransactionApiMasterModel model = new TransactionApiMasterModel();
		model.setId(transactionApiMaster.getId());
		model.setApiName(transactionApiMaster.getApiName());
		model.setBalance(transactionApiMaster.getBalance());
		model.setCode(transactionApiMaster.getCode());
		model.setIsActive(transactionApiMaster.getIsActive());
		model.setIsUserwise(transactionApiMaster.getIsUserwise());
		model.setType(transactionApiMaster.getType());
		model.setStatus(transactionApiMaster.getStatus());
		model.setCreatedBy(transactionApiMaster.getCreatedBy());
		model.setCreatedDt(transactionApiMaster.getCreatedDt());
		model.setModifyBy(transactionApiMaster.getModifyBy());
		model.setModifyDt(transactionApiMaster.getModifyDt());
		model.setAuthBy(transactionApiMaster.getAuthBy());
		model.setAuthDate(transactionApiMaster.getAuthDate());

		if (Strings.isNullOrEmpty(transactionApiMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (transactionApiMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (transactionApiMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (transactionApiMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(transactionApiMaster.getAuthStatus());
		}

		if (transactionApiMaster.getParametersType() != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				Map<String, Object> parameters = objectMapper.readValue(transactionApiMaster.getParametersType(),
						Map.class);
				model.setParametersType(parameters);
			} catch (Exception e) {
				Logger.error("Error deserializing parametersType: ", e);
			}
		}

		return model;
	}

	private FetchTransactionApiResponse buildErrorResponse(boolean status, String message, String respCode,
			List<TransactionApiMasterModel> data, PageDetails pageDetails) {
		FetchTransactionApiResponse response = new FetchTransactionApiResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchTransactionApiResponse getAllData() {
		Logger.info("\r\n\r\n**************************** GET ALL *************************************");

		FetchTransactionApiResponse response = new FetchTransactionApiResponse();
		try {
			List<TransactionApiMaster> all = transactionApiMasterRepository.findByStatus("Active");
			if (all.isEmpty()) {
				return buildErrorResponse(false, "No transaction api found.", "01", null, null);
			}

			List<TransactionApiMasterModel> city = all.stream().map(this::convertToTransactionApiMasterModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", city, null);

		} catch (Exception e) {
			Logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
