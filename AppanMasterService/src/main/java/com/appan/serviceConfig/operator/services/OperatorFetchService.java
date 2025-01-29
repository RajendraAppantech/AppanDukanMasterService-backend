package com.appan.serviceConfig.operator.services;

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
import com.appan.entity.ServiceConfigOperatorMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operator.model.OperatorFetchRequest;
import com.appan.serviceConfig.operator.model.OperatorFetchResponse;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class OperatorFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(OperatorFetchService.class);

	public OperatorFetchResponse fetch(OperatorFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<ServiceConfigOperatorMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getOperatorName(), req.getServiceName(),
					 req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<ServiceConfigOperatorMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Operator not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<ServiceConfigOperatorMaster> OperatorData = pageResults.stream().map(this::convertToOperatorModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", OperatorData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ServiceConfigOperatorMaster> root, String OperatorName, String serviceName, 
			String status, String fromDate, String toDate) {
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

		if (!Strings.isNullOrEmpty(OperatorName)  && !"all".equalsIgnoreCase(OperatorName)) {
			predicates.add(cb.equal(cb.lower(root.get("operatorName")), OperatorName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(serviceName) && !"all".equalsIgnoreCase(serviceName)) {
			predicates.add(cb.equal(cb.lower(root.get("serviceName")), serviceName.toLowerCase()));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private ServiceConfigOperatorMaster convertToOperatorModel(ServiceConfigOperatorMaster ServiceConfigOperatorMaster) {
		ServiceConfigOperatorMaster model = ServiceConfigOperatorMaster;
		if (Strings.isNullOrEmpty(ServiceConfigOperatorMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (ServiceConfigOperatorMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (ServiceConfigOperatorMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (ServiceConfigOperatorMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(ServiceConfigOperatorMaster.getAuthStatus());
		}
		return model;
	}

	private OperatorFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<ServiceConfigOperatorMaster> data, PageDetails pageDetails) {
		OperatorFetchResponse response = new OperatorFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public OperatorFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL Operator  *************************************");

		OperatorFetchResponse response = new OperatorFetchResponse();
		try {
			List<ServiceConfigOperatorMaster> allOperator = repository.findByStatus("Active");
			if (allOperator.isEmpty()) {
				return buildErrorResponse(false, "No Operator found.", "01", null, null);
			}

			List<ServiceConfigOperatorMaster> OperatorData = allOperator.stream().map(this::convertToOperatorModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", OperatorData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
