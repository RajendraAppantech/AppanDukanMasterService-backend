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
import com.appan.switchms.models.AuthOrBlockSwitchOperatorRequest;
import com.appan.switchms.models.AuthOrBlockSwitchTypeRequest;
import com.appan.switchms.models.FetchSwitchOperatorRequest;
import com.appan.switchms.models.FetchSwitchOperatorResponse;
import com.appan.switchms.models.FetchSwitchTypeRequest;
import com.appan.switchms.models.FetchSwitchTypeResponse;
import com.appan.switchms.models.ModifySwitchOpratorRequest;
import com.appan.switchms.models.ModifySwitchTypeRequest;
import com.appan.switchms.models.SwitchOpratorCreateRequest;
import com.appan.switchms.models.SwitchTypeCreateRequest;
import com.appan.switchms.service.AuthOrBlockSwitchOperatorService;
import com.appan.switchms.service.AuthOrBlockSwitchTypeService;
import com.appan.switchms.service.CreateSwitchOperatorService;
import com.appan.switchms.service.FetchSwitchOperatorService;
import com.appan.switchms.service.FetchSwitchTypeService;
import com.appan.switchms.service.ModifySwitchOperatorService;
import com.appan.switchms.service.ModifySwitchTypeService;
import com.appan.switchms.service.SwtichTypeCreateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/switch")
@Validated
public class SwitchController {

	@Autowired
	private SwtichTypeCreateService createService;

	@Autowired
	private FetchSwitchTypeService fetchService;

	@Autowired
	private ModifySwitchTypeService modifyService;

	@Autowired
	private AuthOrBlockSwitchTypeService authBlockService;

	@Autowired
	private CreateSwitchOperatorService createOperatorService;

	@Autowired
	private FetchSwitchOperatorService fetchOperatorService;

	@Autowired
	private ModifySwitchOperatorService modifyOperatorService;

	@Autowired
	private AuthOrBlockSwitchOperatorService authBlockOperatorService;

	@PostMapping("/type/create")
	public CommonResponse create(@Valid @RequestBody SwitchTypeCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/type/fetch")
	public FetchSwitchTypeResponse fetch(@Valid @RequestBody FetchSwitchTypeRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/type/modify")
	public CommonResponse modify(@Valid @RequestBody ModifySwitchTypeRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/type/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockSwitchTypeRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/type/allData")
	public FetchSwitchTypeResponse getAllData() {
		return fetchService.getAllData();
	}

	@PostMapping("/operator/create")
	public CommonResponse create(@Valid @RequestBody SwitchOpratorCreateRequest req) {
		return createOperatorService.create(req);
	}

	@PostMapping("/operator/fetch")
	public FetchSwitchOperatorResponse fetch(@Valid @RequestBody FetchSwitchOperatorRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchOperatorService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/operator/modify")
	public CommonResponse modify(@Valid @RequestBody ModifySwitchOpratorRequest req) {
		return modifyOperatorService.modify(req);
	}

	@PostMapping("/operator/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockSwitchOperatorRequest req) {
		return authBlockOperatorService.authorblock(req);
	}

	@GetMapping("/operator/allData")
	public FetchSwitchOperatorResponse getAllDatas() {
		return fetchOperatorService.getAllData();
	}
}
