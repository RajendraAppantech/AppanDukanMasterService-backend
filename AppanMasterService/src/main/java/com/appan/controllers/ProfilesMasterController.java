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
import com.appan.profilemaster.model.AuthOrBlockProfilesMasterRequest;
import com.appan.profilemaster.model.CreateProfilesMasterRequest;
import com.appan.profilemaster.model.FetchProfilesMasterRequest;
import com.appan.profilemaster.model.FetchProfilesResponse;
import com.appan.profilemaster.model.ModifyProfilesMasterRequest;
import com.appan.profilemaster.services.AuthOrBlockProfilesMasterService;
import com.appan.profilemaster.services.CreateProfilesMasterService;
import com.appan.profilemaster.services.FetchProfilesMasterService;
import com.appan.profilemaster.services.ModifyProfilesMasterService;
import com.appan.profilemaster.services.OperationAutoriService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/profiles")
@Validated
public class ProfilesMasterController {

	@Autowired
	private CreateProfilesMasterService createService;

	@Autowired
	private FetchProfilesMasterService fetchService;

	@Autowired
	private ModifyProfilesMasterService modifyService;

	@Autowired
	private AuthOrBlockProfilesMasterService authBlockService;
	
	@Autowired
	private OperationAutoriService operationAutoriService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateProfilesMasterRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchProfilesResponse fetch(@Valid @RequestBody FetchProfilesMasterRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyProfilesMasterRequest req) {
		return modifyService.modify(req);
	}
	
	@PostMapping("/opAuthorization")
	public CommonResponse opAuth(@Valid @RequestBody ModifyProfilesMasterRequest req) {
		return operationAutoriService.opAuth(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockProfilesMasterRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchProfilesResponse getAllData() {
		return fetchService.getAllData();
	}
}
