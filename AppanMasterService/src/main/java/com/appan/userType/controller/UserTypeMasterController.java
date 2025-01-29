package com.appan.userType.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.userType.models.UserTypeAuthOrBlockRequest;
import com.appan.userType.models.UserTypeCreateRequest;
import com.appan.userType.models.UserTypeFetchRequest;
import com.appan.userType.models.UserTypeFetchResponse;
import com.appan.userType.models.UserTypeModifyRequest;
import com.appan.userType.services.UserTypeAuthOrBlockService;
import com.appan.userType.services.UserTypeCreateService;
import com.appan.userType.services.UserTypeFetchService;
import com.appan.userType.services.UserTypeModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/usertype")
@Validated
public class UserTypeMasterController {

	@Autowired
	private UserTypeCreateService createService;

	@Autowired
	private UserTypeFetchService fetchService;

	@Autowired
	private UserTypeModifyService modifyService;

	@Autowired
	private UserTypeAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody UserTypeCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public UserTypeFetchResponse fetch(@Valid @RequestBody UserTypeFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody UserTypeModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody UserTypeAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public UserTypeFetchResponse getAllData() {
		return fetchService.getAllData();
	}
}
