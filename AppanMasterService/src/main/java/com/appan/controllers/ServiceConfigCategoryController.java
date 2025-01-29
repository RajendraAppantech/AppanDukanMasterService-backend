package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.serviceConfig.category.model.CategoryAuthOrBlockRequest;
import com.appan.serviceConfig.category.model.CategoryCreateRequest;
import com.appan.serviceConfig.category.model.CategoryFetchRequest;
import com.appan.serviceConfig.category.model.CategoryFetchResponse;
import com.appan.serviceConfig.category.model.CategoryModifyRequest;
import com.appan.serviceConfig.category.services.CategoryAuthOrBlockService;
import com.appan.serviceConfig.category.services.CategoryCreateService;
import com.appan.serviceConfig.category.services.CategoryFetchService;
import com.appan.serviceConfig.category.services.CategoryModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/serviceconfig/category")
@Validated
public class ServiceConfigCategoryController {

	@Autowired
	private CategoryCreateService createService;

	@Autowired
	private CategoryFetchService fetchService;

	@Autowired
	private CategoryModifyService modifyService;

	@Autowired
	private CategoryAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CategoryCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public CategoryFetchResponse fetch(@Valid @RequestBody CategoryFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody CategoryModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody CategoryAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public CategoryFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}