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
import com.appan.registration.steps.model.StepsAuthOrBlockRequest;
import com.appan.registration.steps.model.StepsCreateRequest;
import com.appan.registration.steps.model.StepsFetchRequest;
import com.appan.registration.steps.model.StepsFetchResponse;
import com.appan.registration.steps.model.StepsModifyRequest;
import com.appan.registration.steps.services.StepsAuthOrBlockService;
import com.appan.registration.steps.services.StepsCreateService;
import com.appan.registration.steps.services.StpesFetchService;
import com.appan.registration.steps.services.StpesModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/registration/steps")
@Validated
public class RegistrationStepsController {

	@Autowired
	private StepsCreateService createService;

	@Autowired
	private StpesFetchService fetchService;

	@Autowired
	private StpesModifyService modifyService;

	@Autowired
	private StepsAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody StepsCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public StepsFetchResponse fetch(@Valid @RequestBody StepsFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody StepsModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody StepsAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public StepsFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}