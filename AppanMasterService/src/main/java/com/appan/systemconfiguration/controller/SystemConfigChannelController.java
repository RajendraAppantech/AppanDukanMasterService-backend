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
import com.appan.systemconfiguration.models.SystemConfigChannelAuthOrBlockRequest;
import com.appan.systemconfiguration.models.SystemConfigChannelCreateRequest;
import com.appan.systemconfiguration.models.SystemConfigChannelFetchRequest;
import com.appan.systemconfiguration.models.SystemConfigChannelFetchResponse;
import com.appan.systemconfiguration.models.SystemConfigChannelModifyRequest;
import com.appan.systemconfiguration.serivce.SystemConfigChannelAuthBlockService;
import com.appan.systemconfiguration.serivce.SystemConfigChannelCreateService;
import com.appan.systemconfiguration.serivce.SystemConfigChannelFetchService;
import com.appan.systemconfiguration.serivce.SystemConfigChannelModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/systemConfig/channel")
@Validated
public class SystemConfigChannelController {

	@Autowired
	private SystemConfigChannelCreateService createService;

	@Autowired
	private SystemConfigChannelFetchService fetchService;

	@Autowired
	private SystemConfigChannelModifyService modifyService;

	@Autowired
	private SystemConfigChannelAuthBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody SystemConfigChannelCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public SystemConfigChannelFetchResponse fetch(@Valid @RequestBody SystemConfigChannelFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody SystemConfigChannelModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody SystemConfigChannelAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public SystemConfigChannelFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}