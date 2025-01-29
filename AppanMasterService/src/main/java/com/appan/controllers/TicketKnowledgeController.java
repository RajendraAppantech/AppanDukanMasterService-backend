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
import com.appan.ticket.knowledge.models.TicketKnowledgeAuthOrBlockRequest;
import com.appan.ticket.knowledge.models.TicketKnowledgeCreateRequest;
import com.appan.ticket.knowledge.models.TicketKnowledgeFetchRequest;
import com.appan.ticket.knowledge.models.TicketKnowledgeFetchResponse;
import com.appan.ticket.knowledge.models.TicketKnowledgeModifyRequest;
import com.appan.ticket.knowledge.service.TicketKnowledgeAuthBlockService;
import com.appan.ticket.knowledge.service.TicketKnowledgeCreateService;
import com.appan.ticket.knowledge.service.TicketKnowledgeFetchService;
import com.appan.ticket.knowledge.service.TicketKnowledgeModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/ticket/knowledgeCategory")
@Validated
public class TicketKnowledgeController {

	@Autowired
	private TicketKnowledgeCreateService createService;

	@Autowired
	private TicketKnowledgeFetchService fetchService;

	@Autowired
	private TicketKnowledgeModifyService modifyService;

	@Autowired
	private TicketKnowledgeAuthBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody TicketKnowledgeCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public TicketKnowledgeFetchResponse fetch(@Valid @RequestBody TicketKnowledgeFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody TicketKnowledgeModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody TicketKnowledgeAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public TicketKnowledgeFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}