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
import com.appan.profilemaster.model.AuthOrBlockProfileOperationRequest;
import com.appan.profilemaster.model.CreateProfileOperationRequest;
import com.appan.profilemaster.model.FetchProfileOperationRequest;
import com.appan.profilemaster.model.FetchProfileOperationsResponse;
import com.appan.profilemaster.model.ModifyProfileOperationRequest;
import com.appan.profilemaster.services.AuthOrBlockProfileOperationService;
import com.appan.profilemaster.services.CreateProfileOperationService;
import com.appan.profilemaster.services.FetchProfileOperationService;
import com.appan.profilemaster.services.ModifyProfileOperationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/operations")
@Validated
public class ProfileOperationMasterController {

	@Autowired
	private CreateProfileOperationService createService;

	@Autowired
	private FetchProfileOperationService fetchService;

	@Autowired
	private ModifyProfileOperationService modifyService;

	@Autowired
	private AuthOrBlockProfileOperationService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateProfileOperationRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchProfileOperationsResponse fetch(@Valid @RequestBody FetchProfileOperationRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyProfileOperationRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockProfileOperationRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchProfileOperationsResponse getAllData() {
		return fetchService.getAllData();
	}
}
