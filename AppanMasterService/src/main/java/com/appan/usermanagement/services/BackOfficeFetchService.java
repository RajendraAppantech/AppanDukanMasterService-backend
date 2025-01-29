package com.appan.usermanagement.services;

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

import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.BackOfficeManagement;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.usermanagement.models.FetchBackOfficeRequest;
import com.appan.usermanagement.models.FetchBackOfficeResponse;
import com.appan.usermanagement.repo.ManageUserRepositories.BackOfficeManagementRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class BackOfficeFetchService {

	private static final Logger logger = LoggerFactory.getLogger(BackOfficeFetchService.class);

	@Autowired
	private BackOfficeManagementRepository repository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	public FetchBackOfficeResponse fetch(FetchBackOfficeRequest req, Integer pageNo, Integer pageSize) {
		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, "Invalid Username", "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<BackOfficeManagement> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req);
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<BackOfficeManagement> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "No records found", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<BackOfficeManagement> filteredData = pageResults.stream().map(this::convertToStepsModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", filteredData, pageDetails);

		} catch (Exception e) {
			logger.error("Exception occurred while fetching data: ", e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<BackOfficeManagement> root,
			FetchBackOfficeRequest req) {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (req.getFromDate() != null && req.getToDate() != null) {
			try {
				Date startDate = dateFormat.parse(req.getFromDate());
				Date endDate = dateFormat.parse(req.getToDate());
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				endDate = cal.getTime();
				predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
			} catch (ParseException e) {
				logger.error("Error parsing date filters", e);
			}
		}

		if (req.getUserId() != null && !req.getUserId().isEmpty() && !"all".equalsIgnoreCase(req.getUserId())) {
			predicates.add(cb.equal(cb.lower(root.get("userId")), req.getUserId().toLowerCase()));
		}

		if (req.getUserType() != null && !req.getUserType().isEmpty() && !"all".equalsIgnoreCase(req.getUserType())) {
			predicates.add(cb.equal(cb.lower(root.get("userType")), req.getUserType().toLowerCase()));
		}

		if (req.getName() != null && !req.getName().isEmpty() && !"all".equalsIgnoreCase(req.getName())) {
			predicates.add(cb.like(cb.lower(root.get("name")), "%" + req.getName().toLowerCase() + "%"));
		}

		if (req.getMobile() != null && !req.getMobile().isEmpty() && !"all".equalsIgnoreCase(req.getMobile())) {
			predicates.add(cb.equal(root.get("mobile"), req.getMobile()));
		}

		if (req.getEmail() != null && !req.getEmail().isEmpty() && !"all".equalsIgnoreCase(req.getEmail())) {
			predicates.add(cb.like(cb.lower(root.get("email")), "%" + req.getEmail().toLowerCase() + "%"));
		}

		if (req.getCompanyName() != null && !req.getCompanyName().isEmpty()
				&& !"all".equalsIgnoreCase(req.getCompanyName())) {
			predicates.add(cb.like(cb.lower(root.get("companyName")), "%" + req.getCompanyName().toLowerCase() + "%"));
		}

		if (req.getStatus() != null && !req.getStatus().isEmpty() && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.equal(cb.lower(root.get("status")), req.getStatus().toLowerCase()));
		}

		return predicates;
	}

	private BackOfficeManagement convertToStepsModel(BackOfficeManagement BackOfficeManagement) {
		BackOfficeManagement model = BackOfficeManagement;
		if (Strings.isNullOrEmpty(BackOfficeManagement.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (BackOfficeManagement.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (BackOfficeManagement.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (BackOfficeManagement.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(BackOfficeManagement.getAuthStatus());
		}

		return model;
	}

	private FetchBackOfficeResponse buildErrorResponse(boolean status, String message, String respCode,
			List<BackOfficeManagement> data, PageDetails pageDetails) {
		FetchBackOfficeResponse response = new FetchBackOfficeResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchBackOfficeResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL Data  *************************************");

		FetchBackOfficeResponse response = new FetchBackOfficeResponse();
		try {
			List<BackOfficeManagement> allCategory = repository.findByStatus("Active");
			if (allCategory.isEmpty()) {
				return buildErrorResponse(false, "No Data found.", "01", null, null);
			}

			List<BackOfficeManagement> CategoryData = allCategory.stream().map(this::convertToStepsModel)
					.collect(Collectors.toList());
			return buildErrorResponse(true, "SUCCESS", "00", CategoryData, null);

		} catch (Exception e) {
			logger.info("EXCEPTION : " + e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("03");
			return response;
		}
	}

}
