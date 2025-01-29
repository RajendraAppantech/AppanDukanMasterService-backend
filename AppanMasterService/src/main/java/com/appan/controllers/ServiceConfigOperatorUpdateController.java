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
import com.appan.serviceConfig.operatorUpdate.model.OperatorUpdateAuthOrBlockRequest;
import com.appan.serviceConfig.operatorUpdate.model.OperatorUpdateCreateRequest;
import com.appan.serviceConfig.operatorUpdate.model.OperatorUpdateFetchRequest;
import com.appan.serviceConfig.operatorUpdate.model.OperatorUpdateFetchResponse;
import com.appan.serviceConfig.operatorUpdate.model.OperatorUpdateModifyRequest;
import com.appan.serviceConfig.operatorUpdate.services.OperatorUpdateAuthOrBlockService;
import com.appan.serviceConfig.operatorUpdate.services.OperatorUpdateCreateService;
import com.appan.serviceConfig.operatorUpdate.services.OperatorUpdateFetchService;
import com.appan.serviceConfig.operatorUpdate.services.OperatorUpdateModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/serviceconfig/operatorUpdate")
@Validated
public class ServiceConfigOperatorUpdateController {

	@Autowired
	private OperatorUpdateCreateService createService;

	@Autowired
	private OperatorUpdateFetchService fetchService;

	@Autowired
	private OperatorUpdateModifyService modifyService;

	@Autowired
	private OperatorUpdateAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody OperatorUpdateCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public OperatorUpdateFetchResponse fetch(@Valid @RequestBody OperatorUpdateFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody OperatorUpdateModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody OperatorUpdateAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public OperatorUpdateFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}