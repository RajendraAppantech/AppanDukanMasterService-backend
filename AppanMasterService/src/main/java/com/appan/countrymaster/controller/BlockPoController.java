package com.appan.countrymaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.blockpo.models.AuthOrBlockBlockPoRequest;
import com.appan.countrymaster.blockpo.models.CreateBlockPoRequest;
import com.appan.countrymaster.blockpo.models.FetchBlockPoRequest;
import com.appan.countrymaster.blockpo.models.FetchBlockPoResponse;
import com.appan.countrymaster.blockpo.models.ModifyBlockPoRequest;
import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.services.AuthBlockBlockPoService;
import com.appan.countrymaster.services.CreateBlockPoService;
import com.appan.countrymaster.services.FetchBlockPoService;
import com.appan.countrymaster.services.ModifyBlockPoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/blockpo")
@Validated
public class BlockPoController {

	@Autowired
	private CreateBlockPoService createService;

	@Autowired
	private FetchBlockPoService fetchService;

	@Autowired
	private ModifyBlockPoService modifyService;

	@Autowired
	private AuthBlockBlockPoService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateBlockPoRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchBlockPoResponse fetch(@Valid @RequestBody FetchBlockPoRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyBlockPoRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockBlockPoRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchBlockPoResponse getAllData() {
		return fetchService.getAllData();
	}

}
