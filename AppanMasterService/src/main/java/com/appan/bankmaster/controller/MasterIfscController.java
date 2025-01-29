package com.appan.bankmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.bankmaster.masterifsc.model.FetchMasterIfscResponse;
import com.appan.bankmaster.masterifsc.model.MasterIfscAuthOrBlockRequest;
import com.appan.bankmaster.masterifsc.model.MasterIfscCreateRequest;
import com.appan.bankmaster.masterifsc.model.MasterIfscFetchRequest;
import com.appan.bankmaster.masterifsc.model.MasterIfscModifyRequest;
import com.appan.bankmaster.services.MasterIfscAuthOrBlockService;
import com.appan.bankmaster.services.MasterIfscCreateService;
import com.appan.bankmaster.services.MasterIfscFetchService;
import com.appan.bankmaster.services.MasterIfscModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/masterifsc")
@Validated
public class MasterIfscController {

	@Autowired
	private MasterIfscCreateService masterIfscCreateService;

	@Autowired
	private MasterIfscFetchService masterIfscFetchService;

	@Autowired
	private MasterIfscModifyService masterIfscModifyService;

	@Autowired
	private MasterIfscAuthOrBlockService masterIfscAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createMasterIfsc(@Valid @RequestBody MasterIfscCreateRequest req) {
		return masterIfscCreateService.createMasterIfsc(req);
	}

	@PostMapping("/fetch")
	public FetchMasterIfscResponse fetchMasterIfsc(@Valid @RequestBody MasterIfscFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return masterIfscFetchService.fetchMasterIfsc(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyMasterIfsc(@Valid @RequestBody MasterIfscModifyRequest req) {
		return masterIfscModifyService.modifyMasterIfsc(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockMasterIfsc(@Valid @RequestBody MasterIfscAuthOrBlockRequest req) {
		return masterIfscAuthOrBlockService.authorBlockMasterIfsc(req);
	}

	@GetMapping("/allData")
	public FetchMasterIfscResponse getAllMasterIfsc() {
		return masterIfscFetchService.getAllMasterIfsc();
	}
}
