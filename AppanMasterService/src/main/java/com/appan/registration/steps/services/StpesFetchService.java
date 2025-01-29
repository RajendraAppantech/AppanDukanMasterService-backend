package com.appan.registration.steps.services;

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
import com.appan.entity.RegistrationSteps;
import com.appan.entity.UserMaster;
import com.appan.registration.steps.model.StepsFetchRequest;
import com.appan.registration.steps.model.StepsFetchResponse;
import com.appan.repositories.Repositories.StepsMasterRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class StpesFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private StepsMasterRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(StpesFetchService.class);

	public StepsFetchResponse fetch(StepsFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<RegistrationSteps> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getStepsName(), req.getLabel(),
						req.getUserType(), req.getEntityType(), req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<RegistrationSteps> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Steps not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<RegistrationSteps> StepsData = pageResults.stream().map(this::convertToStepsModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", StepsData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<RegistrationSteps> root, String stepsName, String label, String userType, String entityType,
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

		if (!Strings.isNullOrEmpty(stepsName)  && !"all".equalsIgnoreCase(stepsName)) {
			predicates.add(cb.equal(cb.lower(root.get("stepsName")), stepsName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(label) && !"all".equalsIgnoreCase(label)) {
			predicates.add(cb.equal(cb.lower(root.get("label")), label.toLowerCase()));
		}
		if (userType != null && !userType.isEmpty() && !"all".equalsIgnoreCase(userType)) {
			predicates.add(cb.equal(cb.lower(root.get("userType")), userType.toLowerCase()));
		}
		if (entityType != null && !entityType.isEmpty() && !"all".equalsIgnoreCase(entityType)) {
			predicates.add(cb.equal(cb.lower(root.get("entityType")), entityType.toLowerCase()));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private RegistrationSteps convertToStepsModel(RegistrationSteps RegistrationSteps) {
		RegistrationSteps model = RegistrationSteps;
		if (Strings.isNullOrEmpty(RegistrationSteps.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (RegistrationSteps.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (RegistrationSteps.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (RegistrationSteps.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(RegistrationSteps.getAuthStatus());
		}

		return model;
	}

	private StepsFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<RegistrationSteps> data, PageDetails pageDetails) {
		StepsFetchResponse response = new StepsFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public StepsFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL Steps  *************************************");

		StepsFetchResponse response = new StepsFetchResponse();
		try {
			List<RegistrationSteps> allSteps = repository.findAll();
			if (allSteps.isEmpty()) {
				return buildErrorResponse(false, "No Steps found.", "01", null, null);
			}

			List<RegistrationSteps> StepsData = allSteps.stream().map(this::convertToStepsModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", StepsData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
