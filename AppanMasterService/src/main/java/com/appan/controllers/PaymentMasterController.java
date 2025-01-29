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
import com.appan.paymentmaster.model.FetchPaymentResponse;
import com.appan.paymentmaster.model.FetchSubPaymentResponse;
import com.appan.paymentmaster.model.PaymentModeAuthRequest;
import com.appan.paymentmaster.model.PaymentModeCreateRequest;
import com.appan.paymentmaster.model.PaymentModeFetchRequest;
import com.appan.paymentmaster.model.PaymentModeModifyRequest;
import com.appan.paymentmaster.model.SubPaymentModeAuthRequest;
import com.appan.paymentmaster.model.SubPaymentModeCreateRequest;
import com.appan.paymentmaster.model.SubPaymentModeFetchRequest;
import com.appan.paymentmaster.model.SubPaymentModeModifyRequest;
import com.appan.paymentmaster.services.AuthOrBlockSubPaymentmodeService;
import com.appan.paymentmaster.services.CreateSubPaymentmodeService;
import com.appan.paymentmaster.services.FetchSubPaymentmodeService;
import com.appan.paymentmaster.services.ModifySubPaymentmodeService;
import com.appan.paymentmaster.services.PaymentmodeAuthOrBlockService;
import com.appan.paymentmaster.services.PaymentmodeCreateService;
import com.appan.paymentmaster.services.PaymentmodeFetchService;
import com.appan.paymentmaster.services.PaymentmodeModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/payment")
@Validated
public class PaymentMasterController {

	@Autowired
	private PaymentmodeCreateService createService;

	@Autowired
	private PaymentmodeFetchService fetchService;

	@Autowired
	private PaymentmodeModifyService modifyService;

	@Autowired
	private PaymentmodeAuthOrBlockService authBlockService;

	@Autowired
	private CreateSubPaymentmodeService createSubPaymentmodeService;

	@Autowired
	private FetchSubPaymentmodeService fetchSubPaymentmodeService;

	@Autowired
	private ModifySubPaymentmodeService modifySubPaymentmodeService;

	@Autowired
	private AuthOrBlockSubPaymentmodeService authBlockSubPaymentmodeService;

	// Payment Mode APIs

	@PostMapping("/paymentmode/create")
	public CommonResponse createPaymentMode(@Valid @RequestBody PaymentModeCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/paymentmode/fetch")
	public FetchPaymentResponse fetchPaymentModes(@Valid @RequestBody PaymentModeFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/paymentmode/modify")
	public CommonResponse modifyPaymentMode(@Valid @RequestBody PaymentModeModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/paymentmode/authorblock")
	public CommonResponse authPaymentMode(@Valid @RequestBody PaymentModeAuthRequest req) {
		return authBlockService.authorizeOrBlock(req);
	}

	@GetMapping("/paymentmode/allData")
	public FetchPaymentResponse fetchAllPaymentModes() {
		return fetchService.getAllData();
	}

	// Sub Payment Mode APIs

	@PostMapping("/subpayment/create")
	public CommonResponse createSubPaymentMode(@Valid @RequestBody SubPaymentModeCreateRequest req) {
		return createSubPaymentmodeService.create(req);
	}

	@PostMapping("/subpayment/fetch")
	public FetchSubPaymentResponse fetchSubPaymentModes(@Valid @RequestBody SubPaymentModeFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchSubPaymentmodeService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/subpayment/modify")
	public CommonResponse modifySubPaymentMode(@Valid @RequestBody SubPaymentModeModifyRequest req) {
		return modifySubPaymentmodeService.modify(req);
	}

	@PostMapping("/subpayment/authorblock")
	public CommonResponse authSubPaymentMode(@Valid @RequestBody SubPaymentModeAuthRequest req) {
		return authBlockSubPaymentmodeService.authorizeOrBlock(req);
	}

	@GetMapping("/subpayment/allData")
	public FetchSubPaymentResponse fetchAllSubPaymentModes() {
		return fetchSubPaymentmodeService.getAllData();
	}

}
