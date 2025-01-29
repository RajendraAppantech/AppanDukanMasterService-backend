package com.appan.apimaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.apimaster.models.CreateKycApiMaster;
import com.appan.apimaster.models.FetchKycApiMaster;
import com.appan.apimaster.models.FetchKycApiResponse;
import com.appan.apimaster.models.KycApiMasterAuthOrBlockRequest;
import com.appan.apimaster.models.ModifyKycApiMaster;
import com.appan.apimaster.services.KycApiMasterAuthOrBlockService;
import com.appan.apimaster.services.KycApiMasterCreateService;
import com.appan.apimaster.services.KycApiMasterFetchService;
import com.appan.apimaster.services.KycApiMasterModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/kycApi")
@Validated
public class KycApiMasterController {

	@Autowired
	private KycApiMasterCreateService kycApiMasterCreateService;

	@Autowired
	private KycApiMasterFetchService kycApiMasterFetchService;

	@Autowired
	private KycApiMasterModifyService kycApiMasterModifyService;

	@Autowired
	private KycApiMasterAuthOrBlockService kycApiMasterAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createKycApi(@Valid @RequestBody CreateKycApiMaster req) {
		return kycApiMasterCreateService.createKycApi(req);
	}

	@PostMapping("/fetch")
	public FetchKycApiResponse fetchKycApi(@Valid @RequestBody FetchKycApiMaster req,@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return kycApiMasterFetchService.fetchKycApi(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyKycApi(@Valid @RequestBody ModifyKycApiMaster req) {
		return kycApiMasterModifyService.modifyKycApi(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockKycApi(@Valid @RequestBody KycApiMasterAuthOrBlockRequest req) {
		return kycApiMasterAuthOrBlockService.authorBlockKycApi(req);
	}

	@GetMapping("/allData")
	public FetchKycApiResponse getAllKycApis() {
		return kycApiMasterFetchService.getAllData();
	}
}
