package com.appan.serviceConfig.operatorGrouping.services;

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
import com.appan.entity.ServiceConfigOperatorGroupingMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigOperatorGroupingRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.operatorGrouping.model.OperatorGroupingFetchRequest;
import com.appan.serviceConfig.operatorGrouping.model.OperatorGroupingFetchResponse;
import com.google.common.base.Strings;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class OperatorGroupingFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigOperatorGroupingRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(OperatorGroupingFetchService.class);

	public OperatorGroupingFetchResponse fetch(OperatorGroupingFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<ServiceConfigOperatorGroupingMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getGroupName(), req.getOperatorName() , req.getParameterName(),
					 req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<ServiceConfigOperatorGroupingMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "OperatorGrouping not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<ServiceConfigOperatorGroupingMaster> OperatorGroupingData = pageResults.stream().map(this::convertToOperatorGroupingModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", OperatorGroupingData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ServiceConfigOperatorGroupingMaster> root, String groupName, String operatorName, 
			String parameterName, String status, String fromDate, String toDate) {
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

		if (!Strings.isNullOrEmpty(groupName)  && !"all".equalsIgnoreCase(groupName)) {
			predicates.add(cb.equal(cb.lower(root.get("groupName")), groupName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(operatorName) && !"all".equalsIgnoreCase(operatorName)) {
			predicates.add(cb.equal(cb.lower(root.get("operatorName")), operatorName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(parameterName) && !"all".equalsIgnoreCase(parameterName)) {
			predicates.add(cb.equal(cb.lower(root.get("parameterName")), parameterName.toLowerCase()));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private ServiceConfigOperatorGroupingMaster convertToOperatorGroupingModel(ServiceConfigOperatorGroupingMaster ServiceConfigOperatorGroupingMaster) {
		ServiceConfigOperatorGroupingMaster model = ServiceConfigOperatorGroupingMaster;
		if (Strings.isNullOrEmpty(ServiceConfigOperatorGroupingMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (ServiceConfigOperatorGroupingMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (ServiceConfigOperatorGroupingMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (ServiceConfigOperatorGroupingMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(ServiceConfigOperatorGroupingMaster.getAuthStatus());
		}
		return model;
	}

	private OperatorGroupingFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<ServiceConfigOperatorGroupingMaster> data, PageDetails pageDetails) {
		OperatorGroupingFetchResponse response = new OperatorGroupingFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public OperatorGroupingFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL OperatorGrouping  *************************************");

		OperatorGroupingFetchResponse response = new OperatorGroupingFetchResponse();
		try {
			List<ServiceConfigOperatorGroupingMaster> allOperatorGrouping = repository.findByStatus("Active");
			if (allOperatorGrouping.isEmpty()) {
				return buildErrorResponse(false, "No OperatorGrouping found.", "01", null, null);
			}

			List<ServiceConfigOperatorGroupingMaster> OperatorGroupingData = allOperatorGrouping.stream().map(this::convertToOperatorGroupingModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", OperatorGroupingData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
