package com.appan.serviceConfig.operatorUpdate.services;

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
import com.appan.entity.ServiceConfigOperatorUpdateMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorUpdateRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operatorUpdate.model.OperatorUpdateFetchRequest;
import com.appan.serviceConfig.operatorUpdate.model.OperatorUpdateFetchResponse;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class OperatorUpdateFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorUpdateRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(OperatorUpdateFetchService.class);

	public OperatorUpdateFetchResponse fetch(OperatorUpdateFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<ServiceConfigOperatorUpdateMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getOperatorName(), req.getApiOperatorCode1(),
					 req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<ServiceConfigOperatorUpdateMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "OperatorUpdate not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<ServiceConfigOperatorUpdateMaster> OperatorUpdateData = pageResults.stream().map(this::convertToOperatorUpdateModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", OperatorUpdateData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ServiceConfigOperatorUpdateMaster> root, String operatorName, String apiOperatorCode1, 
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

		if (!Strings.isNullOrEmpty(operatorName)  && !"all".equalsIgnoreCase(operatorName)) {
			predicates.add(cb.equal(cb.lower(root.get("operatorName")), operatorName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(apiOperatorCode1) && !"all".equalsIgnoreCase(apiOperatorCode1)) {
			predicates.add(cb.equal(cb.lower(root.get("apiOperatorCode1")), apiOperatorCode1.toLowerCase()));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private ServiceConfigOperatorUpdateMaster convertToOperatorUpdateModel(ServiceConfigOperatorUpdateMaster ServiceConfigOperatorUpdateMaster) {
		ServiceConfigOperatorUpdateMaster model = ServiceConfigOperatorUpdateMaster;
		if (Strings.isNullOrEmpty(ServiceConfigOperatorUpdateMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (ServiceConfigOperatorUpdateMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (ServiceConfigOperatorUpdateMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (ServiceConfigOperatorUpdateMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(ServiceConfigOperatorUpdateMaster.getAuthStatus());
		}
		return model;
	}

	private OperatorUpdateFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<ServiceConfigOperatorUpdateMaster> data, PageDetails pageDetails) {
		OperatorUpdateFetchResponse response = new OperatorUpdateFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public OperatorUpdateFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL OperatorUpdate  *************************************");

		OperatorUpdateFetchResponse response = new OperatorUpdateFetchResponse();
		try {
			List<ServiceConfigOperatorUpdateMaster> allOperatorUpdate = repository.findAll();
			if (allOperatorUpdate.isEmpty()) {
				return buildErrorResponse(false, "No OperatorUpdate found.", "01", null, null);
			}

			List<ServiceConfigOperatorUpdateMaster> OperatorUpdateData = allOperatorUpdate.stream().map(this::convertToOperatorUpdateModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", OperatorUpdateData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
