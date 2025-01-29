package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.BusinessEntityType.model.BusinessEntityTypeAuthOrBlockRequest;
import com.appan.BusinessEntityType.model.BusinessEntityTypeCreateRequest;
import com.appan.BusinessEntityType.model.BusinessEntityTypeFetchRequest;
import com.appan.BusinessEntityType.model.BusinessEntityTypeModifyRequest;
import com.appan.BusinessEntityType.model.FetchBusinessEntityTypeResponse;
import com.appan.businessmaster.services.BusinessEntityTypeAuthOrBlockService;
import com.appan.businessmaster.services.BusinessEntityTypeCreateService;
import com.appan.businessmaster.services.BusinessEntityTypeFetchService;
import com.appan.businessmaster.services.BusinessEntityTypeModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/businessentity")
@Validated
public class BusinessEntityTypeController {

	@Autowired
	private BusinessEntityTypeCreateService BusinessEntityTypeCreateService;

	@Autowired
	private BusinessEntityTypeFetchService BusinessEntityTypeFetchService;

	@Autowired
	private BusinessEntityTypeModifyService BusinessEntityTypeModifyService;

	@Autowired
	private BusinessEntityTypeAuthOrBlockService BusinessEntityTypeAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createBusinessEntityType(@Valid @RequestBody BusinessEntityTypeCreateRequest req) {
		return BusinessEntityTypeCreateService.createBusinessEntityType(req);
	}

	@PostMapping("/fetch")
	public FetchBusinessEntityTypeResponse fetchBusinessEntityType(
			@Valid @RequestBody BusinessEntityTypeFetchRequest req, @RequestParam(defaultValue = "1") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		return BusinessEntityTypeFetchService.fetchBusinessEntityType(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyBusinessEntityType(@Valid @RequestBody BusinessEntityTypeModifyRequest req) {
		return BusinessEntityTypeModifyService.modifyBusinessEntityType(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockBusinessEntityType(@Valid @RequestBody BusinessEntityTypeAuthOrBlockRequest req) {
		return BusinessEntityTypeAuthOrBlockService.authorBlockBusinessEntityType(req);
	}

	@GetMapping("/allData")
	public FetchBusinessEntityTypeResponse getAllBusinessEntityTypes() {
		return BusinessEntityTypeFetchService.getAllBusinessEntityTypes();
	}
}
