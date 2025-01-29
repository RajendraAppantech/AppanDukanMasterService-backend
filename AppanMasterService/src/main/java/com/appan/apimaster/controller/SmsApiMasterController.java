package com.appan.apimaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.apimaster.models.CreateSmsApiMaster;
import com.appan.apimaster.models.FetchSmsApiMaster;
import com.appan.apimaster.models.FetchSmsApiResponse;
import com.appan.apimaster.models.ModifySmsApiMaster;
import com.appan.apimaster.models.SmsApiMasterAuthOrBlockRequest;
import com.appan.apimaster.services.SmsApiMasterAuthOrBlockService;
import com.appan.apimaster.services.SmsApiMasterCreateService;
import com.appan.apimaster.services.SmsApiMasterFetchService;
import com.appan.apimaster.services.SmsApiMasterModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/smsApi")
@Validated
public class SmsApiMasterController {

	@Autowired
	private SmsApiMasterCreateService smsApiMasterCreateService;

	@Autowired
	private SmsApiMasterFetchService smsApiMasterFetchService;

	@Autowired
	private SmsApiMasterModifyService smsApiMasterModifyService;

	@Autowired
	private SmsApiMasterAuthOrBlockService smsApiMasterAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createSmsApi(@Valid @RequestBody CreateSmsApiMaster req) {
		return smsApiMasterCreateService.createSmsApi(req);
	}

	@PostMapping("/fetch")
	public FetchSmsApiResponse fetchSmsApi(@Valid @RequestBody FetchSmsApiMaster req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return smsApiMasterFetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifySmsApi(@Valid @RequestBody ModifySmsApiMaster req) {
		return smsApiMasterModifyService.modifySmsApi(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockSmsApi(@Valid @RequestBody SmsApiMasterAuthOrBlockRequest req) {
		return smsApiMasterAuthOrBlockService.authorBlockSmsApi(req);
	}

	@GetMapping("/allData")
	public FetchSmsApiResponse getAllSmsApis() {
		return smsApiMasterFetchService.getAllData();
	}
}
