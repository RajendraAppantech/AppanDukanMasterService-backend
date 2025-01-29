
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
import com.appan.systemconfiguration.models.SystemConfigOperationAuthOrBlockRequest;
import com.appan.systemconfiguration.models.SystemConfigOperationCreateRequest;
import com.appan.systemconfiguration.models.SystemConfigOperationFetchRequest;
import com.appan.systemconfiguration.models.SystemConfigOperationFetchResponse;
import com.appan.systemconfiguration.models.SystemConfigOperationModifyRequest;
import com.appan.systemconfiguration.serivce.SystemConfigOperationAuthBlockService;
import com.appan.systemconfiguration.serivce.SystemConfigOperationCreateService;
import com.appan.systemconfiguration.serivce.SystemConfigOperationFetchService;
import com.appan.systemconfiguration.serivce.SystemConfigOperationModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/systemConfig/operation")
@Validated
public class SystemConfigOperationController {

	@Autowired
	private SystemConfigOperationCreateService createService;

	@Autowired
	private SystemConfigOperationFetchService fetchService;

	@Autowired
	private SystemConfigOperationModifyService modifyService;

	@Autowired
	private SystemConfigOperationAuthBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody SystemConfigOperationCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public SystemConfigOperationFetchResponse fetch(@Valid @RequestBody SystemConfigOperationFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody SystemConfigOperationModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody SystemConfigOperationAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public SystemConfigOperationFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}