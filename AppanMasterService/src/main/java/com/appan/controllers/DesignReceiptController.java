package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.design.models.DesginReceiptAuthOrBlockRequest;
import com.appan.design.models.DesginReceiptCreateRequest;
import com.appan.design.models.DesginReceiptFetchRequest;
import com.appan.design.models.DesginReceiptFetchResponse;
import com.appan.design.models.DesginReceiptModifyRequest;
import com.appan.design.services.DesginReceiptAuthOrBlockService;
import com.appan.design.services.DesginReceiptCreateService;
import com.appan.design.services.DesginReceiptFetchService;
import com.appan.design.services.DesginReceiptModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/desgin/receipts")
@Validated
public class DesignReceiptController {

	@Autowired
	private DesginReceiptCreateService createService;

	@Autowired
	private DesginReceiptFetchService fetchService;

	@Autowired
	private DesginReceiptModifyService modifyService;

	@Autowired
	private DesginReceiptAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody DesginReceiptCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public DesginReceiptFetchResponse fetch(@Valid @RequestBody DesginReceiptFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody DesginReceiptModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody DesginReceiptAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

}
