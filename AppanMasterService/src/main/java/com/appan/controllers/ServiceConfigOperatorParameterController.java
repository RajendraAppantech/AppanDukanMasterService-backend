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
import com.appan.serviceConfig.operatorParameter.model.OperatorParameterAuthOrBlockRequest;
import com.appan.serviceConfig.operatorParameter.model.OperatorParameterCreateRequest;
import com.appan.serviceConfig.operatorParameter.model.OperatorParameterFetchRequest;
import com.appan.serviceConfig.operatorParameter.model.OperatorParameterFetchResponse;
import com.appan.serviceConfig.operatorParameter.model.OperatorParameterModifyRequest;
import com.appan.serviceConfig.operatorParameter.services.OperatorParameterAuthOrBlockService;
import com.appan.serviceConfig.operatorParameter.services.OperatorParameterCreateService;
import com.appan.serviceConfig.operatorParameter.services.OperatorParameterFetchService;
import com.appan.serviceConfig.operatorParameter.services.OperatorParameterModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/serviceconfig/OperatorParameter")
@Validated
public class ServiceConfigOperatorParameterController {

	@Autowired
	private OperatorParameterCreateService createService;

	@Autowired
	private OperatorParameterFetchService fetchService;

	@Autowired
	private OperatorParameterModifyService modifyService;

	@Autowired
	private OperatorParameterAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody OperatorParameterCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public OperatorParameterFetchResponse fetch(@Valid @RequestBody OperatorParameterFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody OperatorParameterModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody OperatorParameterAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public OperatorParameterFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}