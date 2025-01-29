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
import com.appan.serviceConfig.service.model.ServiceAuthOrBlockRequest;
import com.appan.serviceConfig.service.model.ServiceCreateRequest;
import com.appan.serviceConfig.service.model.ServiceFetchRequest;
import com.appan.serviceConfig.service.model.ServiceFetchResponse;
import com.appan.serviceConfig.service.model.ServiceModifyRequest;
import com.appan.serviceConfig.service.services.ServiceAuthOrBlockService;
import com.appan.serviceConfig.service.services.ServiceCreateService;
import com.appan.serviceConfig.service.services.ServiceFetchService;
import com.appan.serviceConfig.service.services.ServiceModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/serviceconfig/service")
@Validated
public class ServiceConfigServiceController {

	@Autowired
	private ServiceCreateService createService;

	@Autowired
	private ServiceFetchService fetchService;

	@Autowired
	private ServiceModifyService modifyService;

	@Autowired
	private ServiceAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody ServiceCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public ServiceFetchResponse fetch(@Valid @RequestBody ServiceFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ServiceModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody ServiceAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public ServiceFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}