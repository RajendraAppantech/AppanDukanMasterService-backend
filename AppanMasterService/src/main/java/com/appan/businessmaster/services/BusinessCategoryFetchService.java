package com.appan.businessmaster.services;

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
import com.appan.BusinessCategory.model.BusinessCategoryFetchRequest;
import com.appan.BusinessCategory.model.FetchBusinessCategoryModel;
import com.appan.BusinessCategory.model.FetchBusinessCategoryResponse;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.BusinessCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class BusinessCategoryFetchService {

	@Autowired
	private UserMasterRepository masterRepository;

	@Autowired
	private BusinessCategoryRepository businessCategoryRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessCategoryFetchService.class);

	public FetchBusinessCategoryResponse fetchBusinessCategory(BusinessCategoryFetchRequest req, Integer pageNo,
			Integer pageSize) {
		logger.info("Fetching business categories with filters");

		try {

			UserMaster master = masterRepository.findByUserId(req.getUsername().toUpperCase());
			if (master == null) {
				return buildResponse(false, ErrorMessages.INVALID_USERNAME, "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<BusinessCategory> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					logger.error("Error parsing date filters", e);
					return null;
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<BusinessCategory> businessCategoryPage = businessCategoryRepository.findAll(specification, paging);
			if (businessCategoryPage.isEmpty()) {
				return buildResponse(false, "No business categories found", "01", null, null);
			}

			List<FetchBusinessCategoryModel> categoryModels = businessCategoryPage.stream().map(this::convertToModel)
					.collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(businessCategoryPage.getNumber() + 1);
			pageDetails.setTotalRecords(businessCategoryPage.getTotalElements());
			pageDetails.setNoOfPages(businessCategoryPage.getTotalPages());
			pageDetails.setPageSize(businessCategoryPage.getSize());

			return buildResponse(true, "Success", "00", categoryModels, pageDetails);

		} catch (Exception e) {
			logger.error("Exception occurred while fetching business categories", e);
			return buildResponse(false, "Exception occurred", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<BusinessCategory> root,
			BusinessCategoryFetchRequest req) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		if (!Strings.isNullOrEmpty(req.getFromDate()) && !Strings.isNullOrEmpty(req.getToDate())) {
			Date startDate = dateFormat.parse(req.getFromDate());
			Date endDate = dateFormat.parse(req.getToDate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
			endDate = cal.getTime();

			predicates.add(cb.between(root.get("createdDt"), startDate, endDate));
		}

		if (!Strings.isNullOrEmpty(req.getCategoryName()) && !"all".equalsIgnoreCase(req.getCategoryName())) {
			predicates
					.add(cb.like(cb.lower(root.get("categoryName")), "%" + req.getCategoryName().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getCategoryCode()) && !"all".equalsIgnoreCase(req.getCategoryCode())) {
			predicates
					.add(cb.like(cb.lower(root.get("categoryCode")), "%" + req.getCategoryCode().toLowerCase() + "%"));
		}

		if (!Strings.isNullOrEmpty(req.getStatus()) && !"all".equalsIgnoreCase(req.getStatus())) {
			predicates.add(cb.like(cb.lower(root.get("status")), "%" + req.getStatus().toLowerCase() + "%"));
		}

		return predicates;
	}

	private FetchBusinessCategoryModel convertToModel(BusinessCategory category) {
		FetchBusinessCategoryModel model = new FetchBusinessCategoryModel();
		model.setId(category.getId());
		model.setCategoryName(category.getCategoryName());
		model.setCategoryCode(category.getCategoryCode());
		model.setImage(category.getImage());
		model.setStatus(category.getStatus());
		model.setCreatedBy(category.getCreatedBy());
		model.setCreatedDt(category.getCreatedDt());
		model.setModifyBy(category.getModifyBy());
		model.setModifyDt(category.getModifyDt());
		model.setAuthBy(category.getAuthBy());
		model.setAuthDate(category.getAuthDate());
		
		if (Strings.isNullOrEmpty(category.getAuthStatus())) {
			model.setAuthStatus("UNKNOWN");
		} else if (category.getAuthStatus().equalsIgnoreCase("1")) {
			model.setAuthStatus("ACTIVE");
		} else if (category.getAuthStatus().equalsIgnoreCase("3")) {
			model.setAuthStatus("BLOCK");
		} else if (category.getAuthStatus().equalsIgnoreCase("4")) {
			model.setAuthStatus("IN-ACTIVE");
		} else {
			model.setAuthStatus(category.getAuthStatus());
		}
		return model;
	}

	private FetchBusinessCategoryResponse buildResponse(boolean status, String message, String respCode,
			List<FetchBusinessCategoryModel> data, PageDetails pageDetails) {
		FetchBusinessCategoryResponse response = new FetchBusinessCategoryResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchBusinessCategoryResponse getAllBusinessCategories() {
		logger.info(
				"\r\n\r\n**************************** GET ALL BUSINESS CATEGORIES *************************************");
		FetchBusinessCategoryResponse response = new FetchBusinessCategoryResponse();

		try {
			List<BusinessCategory> businessCategoryList = businessCategoryRepository.findByStatus("Active");


			if (businessCategoryList.isEmpty()) {
				return buildResponse(false, "Business Category " + "NOT_FOUND", "01", null, null);
			}

			List<FetchBusinessCategoryModel> categoryModels = businessCategoryList.stream().map(this::convertToModel)
					.collect(Collectors.toList());

			return buildResponse(true, "SUCCESS", "00", categoryModels, null);

		} catch (Exception e) {
			logger.error("EXCEPTION : ", e);
			response.setStatus(false);
			response.setMessage("EXCEPTION");
			response.setRespCode("EX");
			return response;
		}
	}

}
