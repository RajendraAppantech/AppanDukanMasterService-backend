package com.appan.countrymaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.country.models.AuthOrBlockCountryRequest;
import com.appan.countrymaster.country.models.CreateCountryRequest;
import com.appan.countrymaster.country.models.FetchCountryRequest;
import com.appan.countrymaster.country.models.FetchCountryResponse;
import com.appan.countrymaster.country.models.ModifyCountryRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.services.AuthBlockCountryService;
import com.appan.countrymaster.services.CreateCountryService;
import com.appan.countrymaster.services.FetchCountryService;
import com.appan.countrymaster.services.ModifyCountryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/country")
@Validated
public class CountryController {

	@Autowired
	private CreateCountryService createService;

	@Autowired
	private FetchCountryService fetchService;

	@Autowired
	private ModifyCountryService modifyService;

	@Autowired
	private AuthBlockCountryService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateCountryRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchCountryResponse fetch(@Valid @RequestBody FetchCountryRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyCountryRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockCountryRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchCountryResponse getAllData() {
		return fetchService.getAllData();
	}

}
