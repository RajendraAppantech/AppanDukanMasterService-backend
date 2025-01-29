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
import com.appan.wallet.bulk.model.FetchWalletBulkDebitCreditRequest;
import com.appan.wallet.bulk.model.FetchWalletBulkDebitCreditResponse;
import com.appan.wallet.bulk.services.FetchWalletBulkDebitCreditService;
import com.appan.wallet.debitrequest.model.FetchWalletDebitRequest;
import com.appan.wallet.debitrequest.model.FetchWalletDebitResponse;
import com.appan.wallet.debitrequest.model.ModifyWalletDebitRequest;
import com.appan.wallet.debitrequest.services.FetchWalletDebitService;
import com.appan.wallet.debitrequest.services.ModifyWalletDebitService;
import com.appan.wallet.managepayment.model.FetchWalletPaymentRequest;
import com.appan.wallet.managepayment.model.FetchWalletPaymentResponse;
import com.appan.wallet.managepayment.model.ModifyWalletPaymentRequest;
import com.appan.wallet.managepayment.services.FetchWalletPaymentService;
import com.appan.wallet.managepayment.services.ModifyWalletPaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/wallet")
@Validated
public class WalletDebitController {

	@Autowired
	private FetchWalletDebitService fetchService;

	@Autowired
	private ModifyWalletDebitService modifyService;

	@PostMapping("/debitrequest/fetch")
	public FetchWalletDebitResponse fetch(@Valid @RequestBody FetchWalletDebitRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/debitrequest/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyWalletDebitRequest req) {
		return modifyService.modify(req);
	}

	@GetMapping("/debitrequest/allData")
	public FetchWalletDebitResponse getAllDatas() {
		return fetchService.getAllData();
	}

	@Autowired
	private FetchWalletPaymentService fetchServices;

	@Autowired
	private ModifyWalletPaymentService modifyServices;

	@PostMapping("/managepayment/fetch")
	public FetchWalletPaymentResponse fetch(@Valid @RequestBody FetchWalletPaymentRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchServices.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/managepayment/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyWalletPaymentRequest req) {
		return modifyServices.modify(req);
	}

	@GetMapping("/managepayment/allData")
	public FetchWalletPaymentResponse getAllData() {
		return fetchServices.getAllData();
	}

	@Autowired
	private FetchWalletBulkDebitCreditService fetchWalletBulkDebitCreditService;

	@PostMapping("/bulkcreditdebit/fetch")
	public FetchWalletBulkDebitCreditResponse fetch(@Valid @RequestBody FetchWalletBulkDebitCreditRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchWalletBulkDebitCreditService.fetch(req, pageNo, pageSize);
	}

}
