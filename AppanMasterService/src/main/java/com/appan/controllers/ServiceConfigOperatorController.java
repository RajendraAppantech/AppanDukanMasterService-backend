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
import com.appan.serviceConfig.operator.model.OperatorAuthOrBlockRequest;
import com.appan.serviceConfig.operator.model.OperatorCreateRequest;
import com.appan.serviceConfig.operator.model.OperatorFetchRequest;
import com.appan.serviceConfig.operator.model.OperatorFetchResponse;
import com.appan.serviceConfig.operator.model.OperatorModifyRequest;
import com.appan.serviceConfig.operator.services.OperatorAuthOrBlockService;
import com.appan.serviceConfig.operator.services.OperatorCreateService;
import com.appan.serviceConfig.operator.services.OperatorFetchService;
import com.appan.serviceConfig.operator.services.OperatorModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/serviceconfig/operator")
@Validated
public class ServiceConfigOperatorController {

	@Autowired
	private OperatorCreateService createService;

	@Autowired
	private OperatorFetchService fetchService;

	@Autowired
	private OperatorModifyService modifyService;

	@Autowired
	private OperatorAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody OperatorCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public OperatorFetchResponse fetch(@Valid @RequestBody OperatorFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody OperatorModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody OperatorAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public OperatorFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}