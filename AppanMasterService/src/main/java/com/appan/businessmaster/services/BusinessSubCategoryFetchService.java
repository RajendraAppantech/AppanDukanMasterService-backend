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

import com.appan.BusinessSubCategory.model.BusinessSubCategoryFetchRequest;
import com.appan.BusinessSubCategory.model.FetchBusinessSubCategoryModel;
import com.appan.BusinessSubCategory.model.FetchBusinessSubCategoryResponse;
import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.BusinessSubCategory;
import com.appan.entity.UserMaster;
import com.appan.repositories.Repositories.BusinessSubCategoryRepository;
import com.appan.repositories.Repositories.UserMasterRepository;
import com.google.common.base.Strings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class BusinessSubCategoryFetchService {

	@Autowired
	private BusinessSubCategoryRepository businessSubCategoryRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;

	private static final Logger logger = LoggerFactory.getLogger(BusinessSubCategoryFetchService.class);

	public FetchBusinessSubCategoryResponse fetchBusinessSubCategory(BusinessSubCategoryFetchRequest req,
			Integer pageNo, Integer pageSize) {
		logger.info("Fetching business subcategories with filters");

		try {

			UserMaster user = userMasterRepository.findByUserId(req.getUsername().toUpperCase());
			if (user == null) {
				return buildResponse(false, "User not found", "01", null, null);
			}

			Pageable paging = PageRequest.of(pageNo - 1, pageSize);
			Specification<BusinessSubCategory> specification = (root, query, criteriaBuilder) -> {
				List<Predicate> predicates = null;
				try {
					predicates = buildPredicates(criteriaBuilder, root, req);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				query.orderBy(criteriaBuilder.desc(root.get("createdDt")));
				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			};

			Page<BusinessSubCategory> businessSubCategoryPage = businessSubCategoryRepository.findAll(specification,
					paging);
			if (businessSubCategoryPage.isEmpty()) {
				return buildResponse(false, "No business subcategories found", "01", null, null);
			}

			List<FetchBusinessSubCategoryModel> subCategoryModels = businessSubCategoryPage.stream()
					.map(this::convertToModel).collect(Collectors.toList());

			PageDetails pageDetails = new PageDetails();
			pageDetails.setPageNo(businessSubCategoryPage.getNumber() + 1);
			pageDetails.setTotalRecords(businessSubCategoryPage.getTotalElements());
			pageDetails.setNoOfPages(businessSubCategoryPage.getTotalPages());
			pageDetails.setPageSize(businessSubCategoryPage.getSize());

			return buildResponse(true, "Success", "00", subCategoryModels, pageDetails);

		} catch (Exception e) {
			logger.error("Exception occurred while fetching business subcategories", e);
			return buildResponse(false, "Exception occurred", "03", null, null);
		}
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<BusinessSubCategory> root,
			BusinessSubCategoryFetchRequest req) throws ParseException {
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

		if (!Strings.isNullOrEmpty(req.getSubCategoryName()) && !"all".equalsIgnoreCase(req.getSubCategoryName())) {
			predicates.add(
					cb.like(cb.lower(root.get("subCategoryName")), "%" + req.getSubCategoryName().toLowerCase() + "%"));
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

	private FetchBusinessSubCategoryModel convertToModel(BusinessSubCategory subCategory) {
		FetchBusinessSubCategoryModel model = new FetchBusinessSubCategoryModel();
		model.setId(subCategory.getId());
		model.setSubCategoryName(subCategory.getSubCategoryName());
		model.setCategoryName(subCategory.getCategoryName());
		model.setCategoryCode(subCategory.getCategoryCode());
		model.setImage(subCategory.getImage());
		model.setStatus(subCategory.getStatus());
		model.setCreatedBy(subCategory.getCreatedBy());
		model.setCreatedDt(subCategory.getCreatedDt());
		model.setModifyBy(subCategory.getModifyBy());
		model.setModifyDt(subCategory.getModifyDt());
		model.setAuthBy(subCategory.getAuthBy());
		model.setAuthDate(subCategory.getAuthDate());
		model.setAuthStatus(subCategory.getAuthStatus());
		return model;
	}

	private FetchBusinessSubCategoryResponse buildResponse(boolean status, String message, String respCode,
			List<FetchBusinessSubCategoryModel> data, PageDetails pageDetails) {
		FetchBusinessSubCategoryResponse response = new FetchBusinessSubCategoryResponse();
		response.setStatus(status);
		response.setMessage(message);
		response.setRespCode(respCode);
		response.setData(data);
		response.setPageDetails(pageDetails);
		return response;
	}

	public FetchBusinessSubCategoryResponse getAllBusinessSubCategories() {
		logger.info("Fetching all business subcategories");

		FetchBusinessSubCategoryResponse response = new FetchBusinessSubCategoryResponse();

		try {
			List<BusinessSubCategory> businessSubCategoryList = businessSubCategoryRepository.findByStatus("Active");

			if (businessSubCategoryList.isEmpty()) {
				return buildResponse(false, "Business SubCategory not found", "01", null, null);
			}

			List<FetchBusinessSubCategoryModel> subCategoryModels = businessSubCategoryList.stream()
					.map(this::convertToModel).collect(Collectors.toList());

			return buildResponse(true, "Success", "00", subCategoryModels, null);

		} catch (Exception e) {
			logger.error("Exception occurred while fetching business subcategories", e);
			response.setStatus(false);
			response.setMessage("Exception occurred");
			response.setRespCode("EX");
			return response;
		}
	}

}
