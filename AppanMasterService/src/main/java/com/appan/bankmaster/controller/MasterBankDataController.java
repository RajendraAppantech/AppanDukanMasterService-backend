package com.appan.bankmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.bankmaster.masterbankdata.model.FetchMasterBankDataResponse;
import com.appan.bankmaster.masterbankdata.model.MasterBankDataAuthOrBlockRequest;
import com.appan.bankmaster.masterbankdata.model.MasterBankDataCreateRequest;
import com.appan.bankmaster.masterbankdata.model.MasterBankDataFetchRequest;
import com.appan.bankmaster.masterbankdata.model.MasterBankDataModifyRequest;
import com.appan.bankmaster.services.MasterBankDataAuthOrBlockService;
import com.appan.bankmaster.services.MasterBankDataCreateService;
import com.appan.bankmaster.services.MasterBankDataFetchService;
import com.appan.bankmaster.services.MasterBankDataModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/masterbankdata")
@Validated
public class MasterBankDataController {

	@Autowired
	private MasterBankDataCreateService masterBankDataCreateService;

	@Autowired
	private MasterBankDataFetchService masterBankDataFetchService;

	@Autowired
	private MasterBankDataModifyService masterBankDataModifyService;

	@Autowired
	private MasterBankDataAuthOrBlockService masterBankDataAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createMasterBankData(@Valid @RequestBody MasterBankDataCreateRequest req) {
		return masterBankDataCreateService.createMasterBankData(req);
	}

	@PostMapping("/fetch")
	public FetchMasterBankDataResponse fetchMasterBankData(@Valid @RequestBody MasterBankDataFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return masterBankDataFetchService.fetchMasterBankData(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyMasterBankData(@Valid @RequestBody MasterBankDataModifyRequest req) {
		return masterBankDataModifyService.modifyMasterBankData(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockMasterBankData(@Valid @RequestBody MasterBankDataAuthOrBlockRequest req) {
		return masterBankDataAuthOrBlockService.authorBlockMasterBankData(req);
	}

	@GetMapping("/allData")
	public FetchMasterBankDataResponse getAllMasterBankData() {
		return masterBankDataFetchService.getAllMasterBankData();
	}
}
