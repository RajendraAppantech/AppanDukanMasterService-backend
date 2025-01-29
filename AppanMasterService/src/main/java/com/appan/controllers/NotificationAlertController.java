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
import com.appan.notification.models.AlertAuthOrBlockRequest;
import com.appan.notification.models.AlertCreateRequest;
import com.appan.notification.models.AlertFetchRequest;
import com.appan.notification.models.AlertFetchResponse;
import com.appan.notification.models.AlertModifyRequest;
import com.appan.notification.services.AlertAuthOrBlockService;
import com.appan.notification.services.AlertCreateService;
import com.appan.notification.services.AlertFetchService;
import com.appan.notification.services.AlertModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/notification/alert")
@Validated
public class NotificationAlertController {

	@Autowired
	private AlertCreateService createService;

	@Autowired
	private AlertFetchService fetchService;

	@Autowired
	private AlertModifyService modifyService;

	@Autowired
	private AlertAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody AlertCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public AlertFetchResponse fetch(@Valid @RequestBody AlertFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody AlertModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody AlertAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public AlertFetchResponse getAllData() {
		return fetchService.getAllData();
	}
}