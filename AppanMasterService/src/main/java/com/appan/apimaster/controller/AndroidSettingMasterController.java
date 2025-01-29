package com.appan.apimaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.apimaster.models.AuthOrBlockAndroidSettingMasterReq;
import com.appan.apimaster.models.CreateAndroidSettingMasterReq;
import com.appan.apimaster.models.FetchAndroidSettingMasterReq;
import com.appan.apimaster.models.FetchAndroidSettingResponse;
import com.appan.apimaster.models.ModifyAndroidSettingMasterReq;
import com.appan.apimaster.services.AndroidSettingMasterAuthOrBlockService;
import com.appan.apimaster.services.AndroidSettingMasterCreateService;
import com.appan.apimaster.services.AndroidSettingMasterFetchService;
import com.appan.apimaster.services.AndroidSettingMasterModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/androidSetting")
@Validated
public class AndroidSettingMasterController {

	@Autowired
	private AndroidSettingMasterCreateService androidSettingMasterCreateService;

	@Autowired
	private AndroidSettingMasterFetchService androidSettingMasterFetchService;

	@Autowired
	private AndroidSettingMasterModifyService androidSettingMasterModifyService;

	@Autowired
	private AndroidSettingMasterAuthOrBlockService androidSettingMasterAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createAndroidSetting(@Valid @RequestBody CreateAndroidSettingMasterReq req) {
		return androidSettingMasterCreateService.createAndroidSetting(req);
	}

	@PostMapping("/fetch")
	public FetchAndroidSettingResponse fetchAndroidSetting(@Valid @RequestBody FetchAndroidSettingMasterReq req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return androidSettingMasterFetchService.fetchAndroidSetting(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyAndroidSetting(@Valid @RequestBody ModifyAndroidSettingMasterReq req) {
		return androidSettingMasterModifyService.modifyAndroidSetting(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockAndroidSetting(@Valid @RequestBody AuthOrBlockAndroidSettingMasterReq req) {
		return androidSettingMasterAuthOrBlockService.authorBlockAndroidSetting(req);
	}

	@GetMapping("/allData")
	public FetchAndroidSettingResponse getAllData() {
		return androidSettingMasterFetchService.getAllData();
	}
}
