package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.ticketmanagement.model.TicketCreateRequest;
import com.appan.ticketmanagement.model.TicketFetchRequest;
import com.appan.ticketmanagement.model.TicketFetchResponse;
import com.appan.ticketmanagement.model.TicketUpdateToRequest;
import com.appan.ticketmanagement.services.TicketAssignToRequest;
import com.appan.ticketmanagement.services.TicketCreateService;
import com.appan.ticketmanagement.services.TicketFetchService;
import com.appan.ticketmanagement.services.TicketModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/ticketmanagement")
@Validated
public class TicketManagementController {

	@Autowired
	private TicketFetchService fetchService;

	@Autowired
	private TicketModifyService modifyService;

	@Autowired
	private TicketCreateService createService;

	@PostMapping("/fetch")
	public TicketFetchResponse fetch(@Valid @RequestBody TicketFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/assignto")
	public CommonResponse assignto(@Valid @RequestBody TicketAssignToRequest req) {
		return modifyService.assignto(req);
	}

	@PostMapping("/update")
	public CommonResponse update(@Valid @RequestBody TicketUpdateToRequest req) {
		return modifyService.update(req);
	}

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody TicketCreateRequest req) {
		return createService.create(req);
	}

}
