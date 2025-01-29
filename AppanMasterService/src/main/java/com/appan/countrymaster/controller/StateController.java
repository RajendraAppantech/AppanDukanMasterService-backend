package com.appan.countrymaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.countrymaster.services.AuthBlockStateService;
import com.appan.countrymaster.services.CreateStateService;
import com.appan.countrymaster.services.FetchStatenService;
import com.appan.countrymaster.services.ModifyStateService;
import com.appan.countrymaster.state.models.AuthOrBlockStateRequest;
import com.appan.countrymaster.state.models.CreateStateRequest;
import com.appan.countrymaster.state.models.FetchStateRequest;
import com.appan.countrymaster.state.models.FetchStateResponse;
import com.appan.countrymaster.state.models.ModifyStateRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/state")
@Validated
public class StateController {
	
	
	@Autowired
	private CreateStateService createService;
	
	@Autowired
	private FetchStatenService fetchService;
	
	@Autowired
	private ModifyStateService modifyService;
	
	@Autowired
	private AuthBlockStateService authBlockService;
	
	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateStateRequest req) {
		return createService.create(req);
	}
	
	@PostMapping("/fetch")
	public FetchStateResponse fetch(@Valid @RequestBody FetchStateRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}
	
	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyStateRequest req) {
		return modifyService.modify(req);
	}
	
	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AuthOrBlockStateRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FetchStateResponse getAllData() {
		return fetchService.getAllData();
	}

}
