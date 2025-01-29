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
import com.appan.planmaster.rechargeplans.model.AuthOrBlockRechargePlanRequest;
import com.appan.planmaster.rechargeplans.model.CreateRechargePlanRequest;
import com.appan.planmaster.rechargeplans.model.FetchRechargePlanRequest;
import com.appan.planmaster.rechargeplans.model.FetchRechargePlanResponse;
import com.appan.planmaster.rechargeplans.model.ModifyRechargePlanRequest;
import com.appan.planmaster.services.AuthBlockRechargePlanService;
import com.appan.planmaster.services.CreateRechargePlanService;
import com.appan.planmaster.services.FetchRechargePlanService;
import com.appan.planmaster.services.ModifyRechargePlanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/rechargeplans")
@Validated
public class RechargePlansController {

	@Autowired
	private CreateRechargePlanService createService;

	@Autowired
	private FetchRechargePlanService fetchService;

	@Autowired
	private ModifyRechargePlanService modifyService;

	@Autowired
	private AuthBlockRechargePlanService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateRechargePlanRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FetchRechargePlanResponse fetch(@Valid @RequestBody FetchRechargePlanRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyRechargePlanRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockRechargePlanRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchRechargePlanResponse getAllData() {
		return fetchService.getAllData();
	}
}
