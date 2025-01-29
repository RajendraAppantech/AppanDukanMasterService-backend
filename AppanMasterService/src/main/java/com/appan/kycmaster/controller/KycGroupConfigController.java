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
import com.appan.kyc.kycgroupconfig.model.AuthOrBlockKycGroupConfigRequest;
import com.appan.kyc.kycgroupconfig.model.CreateKycGroupConfigRequest;
import com.appan.kyc.kycgroupconfig.model.FetchKycGroupConfigRequest;
import com.appan.kyc.kycgroupconfig.model.FetchKycGroupConfigResponse;
import com.appan.kyc.kycgroupconfig.model.ModifyKycGroupConfigRequest;
import com.appan.kycmaster.services.AuthBlockKycGroupConfigService;
import com.appan.kycmaster.services.FetchKycGroupConfigService;
import com.appan.kycmaster.services.KycGroupConfigService;
import com.appan.kycmaster.services.ModifyKycGroupConfigService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/kycgroupconfig")
@Validated
public class KycGroupConfigController {

	@Autowired
	private KycGroupConfigService kycGroupConfigService;

	@Autowired
	private AuthBlockKycGroupConfigService authBlockKycGroupConfigService;

	@Autowired
	private FetchKycGroupConfigService fetchKycGroupConfigService;

	@Autowired
	private ModifyKycGroupConfigService modifyKycGroupConfigService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateKycGroupConfigRequest req) {
		return kycGroupConfigService.create(req);
	}

	@PostMapping("/fetch")
	public FetchKycGroupConfigResponse fetch(@Valid @RequestBody FetchKycGroupConfigRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchKycGroupConfigService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyKycGroupConfigRequest req) {
		return modifyKycGroupConfigService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authOrBlock(@Valid @RequestBody AuthOrBlockKycGroupConfigRequest req) {
		return authBlockKycGroupConfigService.authOrBlock(req);
	}

	@GetMapping("/allData")
	public FetchKycGroupConfigResponse getAllData() {
		return fetchKycGroupConfigService.getAllData();
	}
}
