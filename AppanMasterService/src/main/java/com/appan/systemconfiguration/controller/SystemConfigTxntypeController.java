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
import com.appan.systemconfiguration.models.SystemConfigTxnTypeAuthOrBlockRequest;
import com.appan.systemconfiguration.models.SystemConfigTxnTypeCreateRequest;
import com.appan.systemconfiguration.models.SystemConfigTxnTypeFetchRequest;
import com.appan.systemconfiguration.models.SystemConfigTxnTypeFetchResponse;
import com.appan.systemconfiguration.models.SystemConfigTxnTypeModifyRequest;
import com.appan.systemconfiguration.serivce.SystemConfigTxnTypeAuthBlockService;
import com.appan.systemconfiguration.serivce.SystemConfigTxnTypeCreateService;
import com.appan.systemconfiguration.serivce.SystemConfigTxnTypeFetchService;
import com.appan.systemconfiguration.serivce.SystemConfigTxnTypeModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/systemConfig/txntype")
@Validated
public class SystemConfigTxntypeController {

	@Autowired
	private SystemConfigTxnTypeCreateService createService;

	@Autowired
	private SystemConfigTxnTypeFetchService fetchService;

	@Autowired
	private SystemConfigTxnTypeModifyService modifyService;

	@Autowired
	private SystemConfigTxnTypeAuthBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody SystemConfigTxnTypeCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public SystemConfigTxnTypeFetchResponse fetch(@Valid @RequestBody SystemConfigTxnTypeFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody SystemConfigTxnTypeModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody SystemConfigTxnTypeAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public SystemConfigTxnTypeFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}