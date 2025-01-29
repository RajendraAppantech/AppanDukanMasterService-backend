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
import com.appan.ticket.category.models.TicketCategoryAuthOrBlockRequest;
import com.appan.ticket.category.models.TicketCategoryCreateRequest;
import com.appan.ticket.category.models.TicketCategoryFetchRequest;
import com.appan.ticket.category.models.TicketCategoryFetchResponse;
import com.appan.ticket.category.models.TicketCategoryModifyRequest;
import com.appan.ticket.category.service.TicketCategoryAuthBlockService;
import com.appan.ticket.category.service.TicketCategoryCreateService;
import com.appan.ticket.category.service.TicketCategoryFetchService;
import com.appan.ticket.category.service.TicketCategoryModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/ticket/category")
@Validated
public class TicketCategoryController {

	@Autowired
	private TicketCategoryCreateService createService;

	@Autowired
	private TicketCategoryFetchService fetchService;

	@Autowired
	private TicketCategoryModifyService modifyService;

	@Autowired
	private TicketCategoryAuthBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody TicketCategoryCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public TicketCategoryFetchResponse fetch(@Valid @RequestBody TicketCategoryFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody TicketCategoryModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody TicketCategoryAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public TicketCategoryFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}