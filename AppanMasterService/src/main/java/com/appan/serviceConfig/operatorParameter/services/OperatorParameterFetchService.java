package com.appan.serviceConfig.operatorParameter.services;

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
import com.appan.entity.ServiceConfigOperatorParameterMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorParameterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operatorParameter.model.OperatorParameterFetchRequest;
import com.appan.serviceConfig.operatorParameter.model.OperatorParameterFetchResponse;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class OperatorParameterFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorParameterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(OperatorParameterFetchService.class);

	public OperatorParameterFetchResponse fetch(OperatorParameterFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<ServiceConfigOperatorParameterMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getOperatorName(), req.getParameterName(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<ServiceConfigOperatorParameterMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "OperatorParameter not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<ServiceConfigOperatorParameterMaster> OperatorParameterData = pageResults.stream().map(this::convertToOperatorParameterModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", OperatorParameterData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ServiceConfigOperatorParameterMaster> root, String operatorName, String parameterName, 
			 String fromDate, String toDate) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (fromDate != null && toDate != null) {
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

		if (!Strings.isNullOrEmpty(operatorName)  && !"all".equalsIgnoreCase(operatorName)) {
			predicates.add(cb.equal(cb.lower(root.get("operatorName")), operatorName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(parameterName) && !"all".equalsIgnoreCase(parameterName)) {
			predicates.add(cb.equal(cb.lower(root.get("parameterName")), parameterName.toLowerCase()));
		}
		

		return predicates;
	}

	private ServiceConfigOperatorParameterMaster convertToOperatorParameterModel(ServiceConfigOperatorParameterMaster ServiceConfigOperatorParameterMaster) {
		ServiceConfigOperatorParameterMaster model = ServiceConfigOperatorParameterMaster;
		if (Strings.isNullOrEmpty(ServiceConfigOperatorParameterMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (ServiceConfigOperatorParameterMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (ServiceConfigOperatorParameterMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (ServiceConfigOperatorParameterMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(ServiceConfigOperatorParameterMaster.getAuthStatus());
		}
		return model;
	}

	private OperatorParameterFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<ServiceConfigOperatorParameterMaster> data, PageDetails pageDetails) {
		OperatorParameterFetchResponse response = new OperatorParameterFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public OperatorParameterFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL OperatorParameter  *************************************");

		OperatorParameterFetchResponse response = new OperatorParameterFetchResponse();
		try {
			List<ServiceConfigOperatorParameterMaster> allOperatorParameter = repository.findAll();
			if (allOperatorParameter.isEmpty()) {
				return buildErrorResponse(false, "No OperatorParameter found.", "01", null, null);
			}

			List<ServiceConfigOperatorParameterMaster> OperatorParameterData = allOperatorParameter.stream().map(this::convertToOperatorParameterModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", OperatorParameterData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
