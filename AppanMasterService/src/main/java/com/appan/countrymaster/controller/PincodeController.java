package com.appan.countrymaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.pincode.models.AuthOrBlockPincodeRequest;
import com.appan.countrymaster.pincode.models.CreatePincodeRequest;
import com.appan.countrymaster.pincode.models.CreateStateCityBlockRequest;
import com.appan.countrymaster.pincode.models.FetchPincodeRequest;
import com.appan.countrymaster.pincode.models.FetchPincodeResponse;
import com.appan.countrymaster.pincode.models.ModifyPincodeRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.services.AuthBlockPincodeService;
import com.appan.countrymaster.services.CreatePincodeService;
import com.appan.countrymaster.services.FetchPincodeService;
import com.appan.countrymaster.services.FetchStateCityBlockService;
import com.appan.countrymaster.services.ModifyPincodeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/pincode")
@Validated
public class PincodeController {

	@Autowired
	private CreatePincodeService createService;

	@Autowired
	private FetchPincodeService fetchService;

	@Autowired
	private ModifyPincodeService modifyService;

	@Autowired
	private AuthBlockPincodeService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreatePincodeRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchPincodeResponse fetch(@Valid @RequestBody FetchPincodeRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyPincodeRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockPincodeRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchPincodeResponse getAllData() {
		return fetchService.getAllData();
	}

	@Autowired
	private FetchStateCityBlockService fetchStateCityBlockService;

	@PostMapping("/fetchStateCityBlock")
	public CommonResponse fetchStateCityBlock(@Valid @RequestBody CreateStateCityBlockRequest req) {
		return fetchStateCityBlockService.create(req);
	}

}
