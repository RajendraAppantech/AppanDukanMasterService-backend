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
import com.appan.serviceConfig.operatorGrouping.model.OperatorGroupingAuthOrBlockRequest;
import com.appan.serviceConfig.operatorGrouping.model.OperatorGroupingCreateRequest;
import com.appan.serviceConfig.operatorGrouping.model.OperatorGroupingFetchRequest;
import com.appan.serviceConfig.operatorGrouping.model.OperatorGroupingFetchResponse;
import com.appan.serviceConfig.operatorGrouping.model.OperatorGroupingModifyRequest;
import com.appan.serviceConfig.operatorGrouping.services.OperatorGroupingAuthOrBlockService;
import com.appan.serviceConfig.operatorGrouping.services.OperatorGroupingCreateService;
import com.appan.serviceConfig.operatorGrouping.services.OperatorGroupingFetchService;
import com.appan.serviceConfig.operatorGrouping.services.OperatorGroupingModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/serviceconfig/operatorGrouping")
@Validated
public class ServiceConfigOperatorGroupingController {

	@Autowired
	private OperatorGroupingCreateService createService;

	@Autowired
	private OperatorGroupingFetchService fetchService;

	@Autowired
	private OperatorGroupingModifyService modifyService;

	@Autowired
	private OperatorGroupingAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody OperatorGroupingCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public OperatorGroupingFetchResponse fetch(@Valid @RequestBody OperatorGroupingFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody OperatorGroupingModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody OperatorGroupingAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public OperatorGroupingFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}