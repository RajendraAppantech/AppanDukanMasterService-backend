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
import com.appan.planmaster.circleseries.model.AuthOrBlockCircleSeriesRequest;
import com.appan.planmaster.circleseries.model.CreateCircleSeriesRequest;
import com.appan.planmaster.circleseries.model.FetchCircleSeriesRequest;
import com.appan.planmaster.circleseries.model.FetchCircleSeriesResponse;
import com.appan.planmaster.circleseries.model.ModifyCircleSeriesRequest;
import com.appan.planmaster.services.AuthBlockCircleSeriesService;
import com.appan.planmaster.services.CreateCircleSeriesService;
import com.appan.planmaster.services.FetchCircleSeriesService;
import com.appan.planmaster.services.ModifyCircleSeriesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/circleseries")
@Validated
public class CircleSeriesController {

	@Autowired
	private CreateCircleSeriesService createService;

	@Autowired
	private FetchCircleSeriesService fetchService;

	@Autowired
	private ModifyCircleSeriesService modifyService;

	@Autowired
	private AuthBlockCircleSeriesService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateCircleSeriesRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchCircleSeriesResponse fetch(@Valid @RequestBody FetchCircleSeriesRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyCircleSeriesRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockCircleSeriesRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchCircleSeriesResponse getAllData() {
		return fetchService.getAllData();
	}
}
