package com.appan.apimaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.apimaster.models.CreateEmailApiMaster;
import com.appan.apimaster.models.EmailApiMasterAuthOrBlockRequest;
import com.appan.apimaster.models.FetchEmailApiMaster;
import com.appan.apimaster.models.FetchEmailApiResponse;
import com.appan.apimaster.models.ModifyEmailApiMaster;
import com.appan.apimaster.services.EmailApiMasterAuthOrBlockService;
import com.appan.apimaster.services.EmailApiMasterCreateService;
import com.appan.apimaster.services.EmailApiMasterFetchService;
import com.appan.apimaster.services.EmailApiMasterModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/emailApi")
@Validated
public class EmailApiMasterController {

	@Autowired
	private EmailApiMasterCreateService emailApiMasterCreateService;

	@Autowired
	private EmailApiMasterFetchService emailApiMasterFetchService;

	@Autowired
	private EmailApiMasterModifyService emailApiMasterModifyService;

	@Autowired
	private EmailApiMasterAuthOrBlockService emailApiMasterAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createEmailApi(@Valid @RequestBody CreateEmailApiMaster req) {
		return emailApiMasterCreateService.createEmailApi(req);
	}

	@PostMapping("/fetch")
	public FetchEmailApiResponse fetchEmailApi(@Valid @RequestBody FetchEmailApiMaster req,@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return emailApiMasterFetchService.fetchEmailApi(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyEmailApi(@Valid @RequestBody ModifyEmailApiMaster req) {
		return emailApiMasterModifyService.modifyEmailApi(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockEmailApi(@Valid @RequestBody EmailApiMasterAuthOrBlockRequest req) {
		return emailApiMasterAuthOrBlockService.authorBlockEmailApi(req);
	}

	@GetMapping("/allData")
	public FetchEmailApiResponse getAllData() {
		return emailApiMasterFetchService.getAllData();
	}
}
