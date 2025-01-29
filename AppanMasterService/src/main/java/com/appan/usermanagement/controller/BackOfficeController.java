package com.appan.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.usermanagement.models.AuthOrBlockBackRequest;
import com.appan.usermanagement.models.CreateBackOfficeRequest;
import com.appan.usermanagement.models.FetchBackOfficeRequest;
import com.appan.usermanagement.models.FetchBackOfficeResponse;
import com.appan.usermanagement.models.ModifyBackOfficeRequest;
import com.appan.usermanagement.services.AuthBlockBacktService;
import com.appan.usermanagement.services.BackOfficeCreateService;
import com.appan.usermanagement.services.BackOfficeFetchService;
import com.appan.usermanagement.services.BackOfficeModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/backoffice")
@Validated
public class BackOfficeController {

	@Autowired
	private BackOfficeCreateService createService;

	@Autowired
	private BackOfficeFetchService fetchService;

	@Autowired
	private BackOfficeModifyService modifyService;

	@Autowired
	private AuthBlockBacktService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateBackOfficeRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchBackOfficeResponse fetch(@Valid @RequestBody FetchBackOfficeRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyBackOfficeRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authblock")
	public CommonResponse authBlock(@Valid @RequestBody AuthOrBlockBackRequest req) {
		return authBlockService.authBlock(req);
	}

	@GetMapping("/allData")
	public FetchBackOfficeResponse getAllData() {
		return fetchService.getAllData();
	}
}
