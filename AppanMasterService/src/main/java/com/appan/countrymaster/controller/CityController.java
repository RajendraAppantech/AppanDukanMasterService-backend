package com.appan.countrymaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.city.models.AuthOrBlockCityRequest;
import com.appan.countrymaster.city.models.CreateCityRequest;
import com.appan.countrymaster.city.models.FetchCityRequest;
import com.appan.countrymaster.city.models.FetchCityResponse;
import com.appan.countrymaster.city.models.ModifyCityRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.services.AuthBlockCityService;
import com.appan.countrymaster.services.CreateCityService;
import com.appan.countrymaster.services.FetchCityService;
import com.appan.countrymaster.services.ModifyCityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/city")
@Validated
public class CityController {
	
	@Autowired
	private CreateCityService createService;
	
	@Autowired
	private FetchCityService fetchService;
	
	@Autowired
	private ModifyCityService modifyService;
	
	@Autowired
	private AuthBlockCityService authBlockService;
	
	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateCityRequest req) {
		return createService.create(req);
	}
	
	@PostMapping("/fetch")
	public FetchCityResponse fetch(@Valid @RequestBody FetchCityRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}
	
	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyCityRequest req) {
		return modifyService.modify(req);
	}
	
	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockCityRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchCityResponse getAllData() {
		return fetchService.getAllData();
	}

}
