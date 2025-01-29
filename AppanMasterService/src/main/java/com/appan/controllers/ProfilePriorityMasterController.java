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
import com.appan.profilemaster.model.AuthOrBlockProfilePriorityRequest;
import com.appan.profilemaster.model.CreateProfilePriorityRequest;
import com.appan.profilemaster.model.FetchProfilePriorityRequest;
import com.appan.profilemaster.model.FetchProfilePriorityResponse;
import com.appan.profilemaster.model.ModifyProfilePriorityRequest;
import com.appan.profilemaster.services.AuthOrBlockProfilePriorityService;
import com.appan.profilemaster.services.CreateProfilePriorityService;
import com.appan.profilemaster.services.FetchProfilePriorityService;
import com.appan.profilemaster.services.ModifyProfilePriorityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/priority")
@Validated
public class ProfilePriorityMasterController {

	@Autowired
	private CreateProfilePriorityService createService;

	@Autowired
	private FetchProfilePriorityService fetchService;

	@Autowired
	private ModifyProfilePriorityService modifyService;

	@Autowired
	private AuthOrBlockProfilePriorityService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateProfilePriorityRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchProfilePriorityResponse fetch(@Valid @RequestBody FetchProfilePriorityRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyProfilePriorityRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockProfilePriorityRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchProfilePriorityResponse getAllData() {
		return fetchService.getAllData();
	}
}
