package com.appan.kycmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.kyc.kycgroup.model.AuthOrBlockKycGroupRequest;
import com.appan.kyc.kycgroup.model.CreateKycGroupRequest;
import com.appan.kyc.kycgroup.model.FetchKycGroupRequest;
import com.appan.kyc.kycgroup.model.FetchKycGroupResponse;
import com.appan.kyc.kycgroup.model.ModifyKycGroupRequest;
import com.appan.kycmaster.services.AuthBlockKycGroupService;
import com.appan.kycmaster.services.FetchKycGroupService;
import com.appan.kycmaster.services.KycGroupService;
import com.appan.kycmaster.services.ModifyKycGroupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/kycgroup")
@Validated
public class KycGroupController {

	@Autowired
	private KycGroupService kycGroupService;

	@Autowired
	private AuthBlockKycGroupService authBlockKycGroupService;

	@Autowired
	private FetchKycGroupService fetchKycGroupService;

	@Autowired
	private ModifyKycGroupService modifyKycGroupService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateKycGroupRequest req) {
		return kycGroupService.create(req);
	}

	@PostMapping("/fetch")
	public FetchKycGroupResponse fetch(@Valid @RequestBody FetchKycGroupRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchKycGroupService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyKycGroupRequest req) {
		return modifyKycGroupService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authOrBlock(@Valid @RequestBody AuthOrBlockKycGroupRequest req) {
		return authBlockKycGroupService.authOrBlock(req);
	}

	@GetMapping("/allData")
	public FetchKycGroupResponse getAllData() {
		return fetchKycGroupService.getAllData();
	}
}
