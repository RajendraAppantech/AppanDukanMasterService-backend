package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.BusinessCategory.model.BusinessCategoryAuthOrBlockRequest;
import com.appan.BusinessCategory.model.BusinessCategoryCreateRequest;
import com.appan.BusinessCategory.model.BusinessCategoryFetchRequest;
import com.appan.BusinessCategory.model.BusinessCategoryModifyRequest;
import com.appan.BusinessCategory.model.FetchBusinessCategoryResponse;
import com.appan.businessmaster.services.BusinessCategoryAuthOrBlockService;
import com.appan.businessmaster.services.BusinessCategoryCreateService;
import com.appan.businessmaster.services.BusinessCategoryFetchService;
import com.appan.businessmaster.services.BusinessCategoryModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/businesscategory")
@Validated
public class BusinessCategoryController {

	@Autowired
	private BusinessCategoryCreateService businessCategoryCreateService;

	@Autowired
	private BusinessCategoryFetchService businessCategoryFetchService;

	@Autowired
	private BusinessCategoryModifyService businessCategoryModifyService;

	@Autowired
	private BusinessCategoryAuthOrBlockService businessCategoryAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createBusinessCategory(@Valid @RequestBody BusinessCategoryCreateRequest req) {
		return businessCategoryCreateService.createBusinessCategory(req);
	}

	@PostMapping("/fetch")
	public FetchBusinessCategoryResponse fetchBusinessCategory(@Valid @RequestBody BusinessCategoryFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return businessCategoryFetchService.fetchBusinessCategory(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyBusinessCategory(@Valid @RequestBody BusinessCategoryModifyRequest req) {
		return businessCategoryModifyService.modifyBusinessCategory(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockBusinessCategory(@Valid @RequestBody BusinessCategoryAuthOrBlockRequest req) {
		return businessCategoryAuthOrBlockService.authorBlockBusinessCategory(req);
	}

	@GetMapping("/allData")
	public FetchBusinessCategoryResponse getAllBusinessCategories() {
		return businessCategoryFetchService.getAllBusinessCategories();
	}
}
