package com.appan.planmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.planmaster.circle.model.AuthOrBlockCircleRequest;
import com.appan.planmaster.circle.model.CreateCircleRequest;
import com.appan.planmaster.circle.model.FetchCircleRequest;
import com.appan.planmaster.circle.model.FetchCircleResponse;
import com.appan.planmaster.circle.model.ModifyCircleRequest;
import com.appan.planmaster.services.AuthBlockCircleService;
import com.appan.planmaster.services.CreateCircleService;
import com.appan.planmaster.services.FetchCircleService;
import com.appan.planmaster.services.ModifyCircleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/circle")
@Validated
public class CirclesController {

	@Autowired
	private CreateCircleService createService;

	@Autowired
	private FetchCircleService fetchService;

	@Autowired
	private ModifyCircleService modifyService;

	@Autowired
	private AuthBlockCircleService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateCircleRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchCircleResponse fetch(@Valid @RequestBody FetchCircleRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyCircleRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockCircleRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchCircleResponse getAllData() {
		return fetchService.getAllData();
	}
}
