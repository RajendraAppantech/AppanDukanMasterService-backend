package com.appan.notification.services;

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
import com.appan.entity.AlertTypeMaster;
import com.appan.entity.UserMaster;
import com.appan.notification.models.AlertFetchRequest;
import com.appan.notification.models.AlertFetchResponse;
import com.appan.repositories.Repositories.AlertTypeMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class AlertFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private AlertTypeMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(AlertFetchService.class);

	public AlertFetchResponse fetch(AlertFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<AlertTypeMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getAlertName(),
						req.getParametersName(), req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<AlertTypeMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Alert not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<AlertTypeMaster> AlertData = pageResults.stream().map(this::convertToAlertModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", AlertData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<AlertTypeMaster> root, String alertName,
			String parametersName, String status, String fromDate, String toDate) {
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

		if (!Strings.isNullOrEmpty(alertName) && !"all".equalsIgnoreCase(alertName)) {
			predicates.add(cb.equal(cb.lower(root.get("alertName")), alertName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(parametersName) && !"all".equalsIgnoreCase(parametersName)) {
			predicates.add(cb.equal(cb.lower(root.get("parametersName")), parametersName.toLowerCase()));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private AlertTypeMaster convertToAlertModel(AlertTypeMaster AlertTypeMaster) {
		AlertTypeMaster model = AlertTypeMaster;
		if (Strings.isNullOrEmpty(AlertTypeMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (AlertTypeMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (AlertTypeMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (AlertTypeMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(AlertTypeMaster.getAuthStatus());
		}
		return model;
	}

	private AlertFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<AlertTypeMaster> data, PageDetails pageDetails) {
		AlertFetchResponse response = new AlertFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public AlertFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL Alert  *************************************");

		AlertFetchResponse response = new AlertFetchResponse();
		try {
			List<AlertTypeMaster> allAlert = repository.findByAuthStatus("1");
			if (allAlert.isEmpty()) {
				return buildErrorResponse(false, "No Alert found.", "01", null, null);
			}

			List<AlertTypeMaster> AlertData = allAlert.stream().map(this::convertToAlertModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", AlertData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
