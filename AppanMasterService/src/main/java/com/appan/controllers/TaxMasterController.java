package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.taxmaster.model.AuthOrBlockTaxRequest;
import com.appan.taxmaster.model.CreateTaxRequest;
import com.appan.taxmaster.model.FetchTaxRequest;
import com.appan.taxmaster.model.FetchTaxResponse;
import com.appan.taxmaster.model.ModifyTaxRequest;
import com.appan.taxmaster.services.AuthBlockTaxService;
import com.appan.taxmaster.services.CreateTaxService;
import com.appan.taxmaster.services.FetchTaxService;
import com.appan.taxmaster.services.ModifyTaxService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/tax")
@Validated
public class TaxMasterController {

	@Autowired
	private CreateTaxService createService;

	@Autowired
	private FetchTaxService fetchService;

	@Autowired
	private ModifyTaxService modifyService;

	@Autowired
	private AuthBlockTaxService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateTaxRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchTaxResponse fetch(@Valid @RequestBody FetchTaxRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyTaxRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockTaxRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchTaxResponse getAllData() {
		return fetchService.getAllData();
	}
}
