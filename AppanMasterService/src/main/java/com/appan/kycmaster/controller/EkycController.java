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
import com.appan.kyc.ekyc.model.AuthOrBlockEkycRequest;
import com.appan.kyc.ekyc.model.CreateEkycRequest;
import com.appan.kyc.ekyc.model.FetchEkycRequest;
import com.appan.kyc.ekyc.model.FetchEkycResponse;
import com.appan.kyc.ekyc.model.ModifyEkycRequest;
import com.appan.kycmaster.services.AuthBlockEkycService;
import com.appan.kycmaster.services.EkycService;
import com.appan.kycmaster.services.FetchEkycService;
import com.appan.kycmaster.services.ModifyEkycService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/ekyc")
@Validated
public class EkycController {

	@Autowired
	private EkycService ekycService;

	@Autowired
	private AuthBlockEkycService authBlockEkycService;

	@Autowired
	private FetchEkycService fetchEkycService;

	@Autowired
	private ModifyEkycService modifyEkycService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateEkycRequest req) {
		return ekycService.create(req);
	}

	@PostMapping("/fetch")
	public FetchEkycResponse fetch(@Valid @RequestBody FetchEkycRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchEkycService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyEkycRequest req) {
		return modifyEkycService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authOrBlock(@Valid @RequestBody AuthOrBlockEkycRequest req) {
		return authBlockEkycService.authOrBlock(req);
	}

	@GetMapping("/allData")
	public FetchEkycResponse getAllData() {
		return fetchEkycService.getAllData();
	}
}
