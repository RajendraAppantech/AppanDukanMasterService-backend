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
import com.appan.ticket.childcategory.models.TicketChildCategoryAuthOrBlockRequest;
import com.appan.ticket.childcategory.models.TicketChildCategoryCreateRequest;
import com.appan.ticket.childcategory.models.TicketChildCategoryFetchRequest;
import com.appan.ticket.childcategory.models.TicketChildCategoryFetchResponse;
import com.appan.ticket.childcategory.models.TicketChildCategoryModifyRequest;
import com.appan.ticket.childcategory.service.TicketChildCategoryAuthBlockService;
import com.appan.ticket.childcategory.service.TicketChildCategoryCreateService;
import com.appan.ticket.childcategory.service.TicketChildCategoryFetchService;
import com.appan.ticket.childcategory.service.TicketChildCategoryModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/ticket/childCategory")
@Validated
public class TicketChildCategoryController {

	@Autowired
	private TicketChildCategoryCreateService createService;

	@Autowired
	private TicketChildCategoryFetchService fetchService;

	@Autowired
	private TicketChildCategoryModifyService modifyService;

	@Autowired
	private TicketChildCategoryAuthBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody TicketChildCategoryCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public TicketChildCategoryFetchResponse fetch(@Valid @RequestBody TicketChildCategoryFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody TicketChildCategoryModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody TicketChildCategoryAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public TicketChildCategoryFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}