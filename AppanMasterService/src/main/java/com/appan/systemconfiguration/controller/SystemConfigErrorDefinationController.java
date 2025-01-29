package com.appan.systemconfiguration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.systemconfiguration.models.SystemConfigErrorDefinationAuthOrBlockRequest;
import com.appan.systemconfiguration.models.SystemConfigErrorDefinationCreateRequest;
import com.appan.systemconfiguration.models.SystemConfigErrorDefinationFetchRequest;
import com.appan.systemconfiguration.models.SystemConfigErrorDefinationFetchResponse;
import com.appan.systemconfiguration.models.SystemConfigErrorDefinationModifyRequest;
import com.appan.systemconfiguration.serivce.SystemConfigErrorDefinationAuthBlockService;
import com.appan.systemconfiguration.serivce.SystemConfigErrorDefinationCreateService;
import com.appan.systemconfiguration.serivce.SystemConfigErrorDefinationFetchService;
import com.appan.systemconfiguration.serivce.SystemConfigErrorDefinationModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/systemConfig/errorDefination")
@Validated
public class SystemConfigErrorDefinationController {

	@Autowired
	private SystemConfigErrorDefinationCreateService createService;

	@Autowired
	private SystemConfigErrorDefinationFetchService fetchService;

	@Autowired
	private SystemConfigErrorDefinationModifyService modifyService;

	@Autowired
	private SystemConfigErrorDefinationAuthBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody SystemConfigErrorDefinationCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public SystemConfigErrorDefinationFetchResponse fetch(
			@Valid @RequestBody SystemConfigErrorDefinationFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody SystemConfigErrorDefinationModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody SystemConfigErrorDefinationAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public SystemConfigErrorDefinationFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}