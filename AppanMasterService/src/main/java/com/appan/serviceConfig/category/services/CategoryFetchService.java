package com.appan.serviceConfig.category.services;

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
import com.appan.entity.ServiceConfigCategoryMaster;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.ServiceConfigCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.appan.serviceConfig.category.model.CategoryFetchRequest;
import com.appan.serviceConfig.category.model.CategoryFetchResponse;
import com.google.common.base.Strings;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class CategoryFetchService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private ServiceConfigCategoryRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(CategoryFetchService.class);

	public CategoryFetchResponse fetch(CategoryFetchRequest req, Integer pageNo, Integer pageSize) {

		try {
			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildErrorResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);

			Specification<ServiceConfigCategoryMaster> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = buildPredicates(criteriaBuilder, root, req.getCategoryName(), req.getCategoryCode(),
					 req.getStatus(), req.getFromDate(), req.getToDate());
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<ServiceConfigCategoryMaster> pageResults = repository.findAll(specification, paging);

			if (pageResults.isEmpty()) {
				return buildErrorResponse(false, "Category not found.", "01", null, null);
			}

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(pageResults.getNumber() + 1);
			pageDetails.setTotalRecords(pageResults.getTotalElements());
			pageDetails.setNoOfPages(pageResults.getTotalPages());
			pageDetails.setPageSize(pageResults.getSize());

			List<ServiceConfigCategoryMaster> CategoryData = pageResults.stream().map(this::convertToCategoryModel)
					.collect(Collectors.toList());

			return buildErrorResponse(true, "SUCCESS", "00", CategoryData, pageDetails);

		} catch (Exception e) {
			logger.error("EXCEPTION : " + e);
			return buildErrorResponse(false, "EXCEPTION", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ServiceConfigCategoryMaster> root, String categoryName, String categoryCode, 
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

		if (!Strings.isNullOrEmpty(categoryName)  && !"all".equalsIgnoreCase(categoryName)) {
			predicates.add(cb.equal(cb.lower(root.get("categoryName")), categoryName.toLowerCase()));
		}
		if (!Strings.isNullOrEmpty(categoryCode) && !"all".equalsIgnoreCase(categoryCode)) {
			predicates.add(cb.equal(cb.lower(root.get("categoryCode")), categoryCode.toLowerCase()));
		}
		if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
			predicates.add(cb.equal(cb.lower(root.get("status")), status.toLowerCase()));
		}

		return predicates;
	}

	private ServiceConfigCategoryMaster convertToCategoryModel(ServiceConfigCategoryMaster ServiceConfigCategoryMaster) {
		ServiceConfigCategoryMaster model = ServiceConfigCategoryMaster;
		if (Strings.isNullOrEmpty(ServiceConfigCategoryMaster.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (ServiceConfigCategoryMaster.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (ServiceConfigCategoryMaster.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (ServiceConfigCategoryMaster.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(ServiceConfigCategoryMaster.getAuthStatus());
		}
		return model;
	}

	private CategoryFetchResponse buildErrorResponse(boolean status, String message, String respCode,
			List<ServiceConfigCategoryMaster> data, PageDetails pageDetails) {
		CategoryFetchResponse response = new CategoryFetchResponse();
		response.setMessage(message);
		response.setStatus(status);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public CategoryFetchResponse getAllData() {
		logger.info("\r\n\r\n**************************** GET ALL Category  *************************************");

		CategoryFetchResponse response = new CategoryFetchResponse();
		try {
			List<ServiceConfigCategoryMaster> allCategory = repository.findByStatus("Active");
			if (allCategory.isEmpty()) {
				return buildErrorResponse(false, "No Category found.", "01", null, null);
			}

			List<ServiceConfigCategoryMaster> CategoryData = allCategory.stream().map(this::convertToCategoryModel)
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
