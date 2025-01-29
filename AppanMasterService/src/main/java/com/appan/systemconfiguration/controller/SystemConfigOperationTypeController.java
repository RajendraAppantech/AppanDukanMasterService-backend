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
import com.appan.systemconfiguration.models.SystemConfigOperationTypeAuthOrBlockRequest;
import com.appan.systemconfiguration.models.SystemConfigOperationTypeCreateRequest;
import com.appan.systemconfiguration.models.SystemConfigOperationTypeFetchRequest;
import com.appan.systemconfiguration.models.SystemConfigOperationTypeFetchResponse;
import com.appan.systemconfiguration.models.SystemConfigOperationTypeModifyRequest;
import com.appan.systemconfiguration.serivce.SystemConfigOperationTypeAuthBlockService;
import com.appan.systemconfiguration.serivce.SystemConfigOperationTypeCreateService;
import com.appan.systemconfiguration.serivce.SystemConfigOperationTypeFetchService;
import com.appan.systemconfiguration.serivce.SystemConfigOperationTypeModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/systemConfig/operationType")
@Validated
public class SystemConfigOperationTypeController {

	@Autowired
	private SystemConfigOperationTypeCreateService createService;

	@Autowired
	private SystemConfigOperationTypeFetchService fetchService;

	@Autowired
	private SystemConfigOperationTypeModifyService modifyService;

	@Autowired
	private SystemConfigOperationTypeAuthBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody SystemConfigOperationTypeCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public SystemConfigOperationTypeFetchResponse fetch(@Valid @RequestBody SystemConfigOperationTypeFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody SystemConfigOperationTypeModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody SystemConfigOperationTypeAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public SystemConfigOperationTypeFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}