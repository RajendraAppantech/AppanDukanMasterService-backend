package com.appan.countrymaster.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.district.models.AuthOrBlockDistrictRequest;
import com.appan.countrymaster.district.models.CreateDistrictRequest;
import com.appan.countrymaster.district.models.FetchDistrictRequest;
import com.appan.countrymaster.district.models.FetchDistrictResponse;
import com.appan.countrymaster.district.models.ModifyDistrictRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.services.AuthBlockDistrictService;
import com.appan.countrymaster.services.CreateDistrictService;
import com.appan.countrymaster.services.FetchDistrictService;
import com.appan.countrymaster.services.ModifyDistrictService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/district")
@Validated
public class DistrictController {
	
	@Autowired
	private CreateDistrictService createService;
	
	@Autowired
	private FetchDistrictService fetchService;
	
	@Autowired
	private ModifyDistrictService modifyService;
	
	@Autowired
	private AuthBlockDistrictService authBlockService;
	
	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateDistrictRequest req) {
		return createService.create(req);
	}
	
	@PostMapping("/fetch")
	public FetchDistrictResponse fetch(@Valid @RequestBody FetchDistrictRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) throws ParseException {
		return fetchService.fetch(req, pageNo, pageSize);
	}
	
	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyDistrictRequest req) {
		return modifyService.modify(req);
	}
	
	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockDistrictRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchDistrictResponse getAllData() {
		return fetchService.getAllData();
	}

}
