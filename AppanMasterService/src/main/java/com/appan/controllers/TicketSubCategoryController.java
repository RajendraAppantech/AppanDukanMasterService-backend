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
import com.appan.ticket.subcategory.models.TicketSubCategoryAuthOrBlockRequest;
import com.appan.ticket.subcategory.models.TicketSubCategoryCreateRequest;
import com.appan.ticket.subcategory.models.TicketSubCategoryFetchRequest;
import com.appan.ticket.subcategory.models.TicketSubCategoryFetchResponse;
import com.appan.ticket.subcategory.models.TicketSubCategoryModifyRequest;
import com.appan.ticket.subcategory.service.TicketSubCategoryAuthBlockService;
import com.appan.ticket.subcategory.service.TicketSubCategoryCreateService;
import com.appan.ticket.subcategory.service.TicketSubCategoryFetchService;
import com.appan.ticket.subcategory.service.TicketSubCategoryModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/ticket/subCategory")
@Validated
public class TicketSubCategoryController {

	@Autowired
	private TicketSubCategoryCreateService createService;

	@Autowired
	private TicketSubCategoryFetchService fetchService;

	@Autowired
	private TicketSubCategoryModifyService modifyService;

	@Autowired
	private TicketSubCategoryAuthBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody TicketSubCategoryCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public TicketSubCategoryFetchResponse fetch(@Valid @RequestBody TicketSubCategoryFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody TicketSubCategoryModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody TicketSubCategoryAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public TicketSubCategoryFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}