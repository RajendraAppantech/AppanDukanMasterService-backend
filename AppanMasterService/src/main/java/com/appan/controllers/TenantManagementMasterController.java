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
import com.appan.tenant.model.AuthOrBlockTenantRequest;
import com.appan.tenant.model.CreateTenantRequest;
import com.appan.tenant.model.FetchTenantRequest;
import com.appan.tenant.model.FetchTenantResponse;
import com.appan.tenant.model.ModifyTenantRequest;
import com.appan.tenant.services.AuthBlockTenantService;
import com.appan.tenant.services.CreateTenantService;
import com.appan.tenant.services.FetchTenantService;
import com.appan.tenant.services.ModifyTenantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/tenant")
@Validated
public class TenantManagementMasterController {

	@Autowired
	private CreateTenantService createService;

	@Autowired
	private FetchTenantService fetchService;

	@Autowired
	private ModifyTenantService modifyService;

	@Autowired
	private AuthBlockTenantService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateTenantRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchTenantResponse fetch(@Valid @RequestBody FetchTenantRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyTenantRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authblock")
	public CommonResponse authBlock(@Valid @RequestBody AuthOrBlockTenantRequest req) {
		return authBlockService.authBlock(req);
	}

	@GetMapping("/allData")
	public FetchTenantResponse getAllData() {
		return fetchService.getAllData();
	}
}
