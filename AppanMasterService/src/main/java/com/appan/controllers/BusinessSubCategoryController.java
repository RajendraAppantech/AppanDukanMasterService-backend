package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.BusinessSubCategory.model.BusinessSubCategoryAuthOrBlockRequest;
import com.appan.BusinessSubCategory.model.BusinessSubCategoryCreateRequest;
import com.appan.BusinessSubCategory.model.BusinessSubCategoryFetchRequest;
import com.appan.BusinessSubCategory.model.BusinessSubCategoryModifyRequest;
import com.appan.BusinessSubCategory.model.FetchBusinessSubCategoryResponse;
import com.appan.businessmaster.services.BusinessSubCategoryAuthOrBlockService;
import com.appan.businessmaster.services.BusinessSubCategoryCreateService;
import com.appan.businessmaster.services.BusinessSubCategoryFetchService;
import com.appan.businessmaster.services.BusinessSubCategoryModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/businesssubcategory")
@Validated
public class BusinessSubCategoryController {

	@Autowired
	private BusinessSubCategoryCreateService businessSubCategoryCreateService;

	@Autowired
	private BusinessSubCategoryFetchService businessSubCategoryFetchService;

	@Autowired
	private BusinessSubCategoryModifyService businessSubCategoryModifyService;

	@Autowired
	private BusinessSubCategoryAuthOrBlockService businessSubCategoryAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createBusinessSubCategory(@Valid @RequestBody BusinessSubCategoryCreateRequest req) {
		return businessSubCategoryCreateService.createBusinessSubCategory(req);
	}

	@PostMapping("/fetch")
	public FetchBusinessSubCategoryResponse fetchBusinessSubCategory(
			@Valid @RequestBody BusinessSubCategoryFetchRequest req, @RequestParam(defaultValue = "1") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		return businessSubCategoryFetchService.fetchBusinessSubCategory(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyBusinessSubCategory(@Valid @RequestBody BusinessSubCategoryModifyRequest req) {
		return businessSubCategoryModifyService.modifyBusinessSubCategory(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockBusinessSubCategory(
			@Valid @RequestBody BusinessSubCategoryAuthOrBlockRequest req) {
		return businessSubCategoryAuthOrBlockService.authorBlockBusinessSubCategory(req);
	}

	@GetMapping("/allData")
	public FetchBusinessSubCategoryResponse getAllBusinessSubCategories() {
		return businessSubCategoryFetchService.getAllBusinessSubCategories();
	}
}
