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
import com.appan.walletmaster.model.SourceOfFundAuthOrBlockRequest;
import com.appan.walletmaster.model.SourceOfFundCreateRequest;
import com.appan.walletmaster.model.SourceOfFundFetchRequest;
import com.appan.walletmaster.model.SourceOfFundFetchResponse;
import com.appan.walletmaster.model.SourceOfFundModifyRequest;
import com.appan.walletmaster.model.WalletAuthOrBlockRequest;
import com.appan.walletmaster.model.WalletCreateRequest;
import com.appan.walletmaster.model.WalletFetchRequest;
import com.appan.walletmaster.model.WalletFetchResponse;
import com.appan.walletmaster.model.WalletModifyRequest;
import com.appan.walletmaster.services.SourceOfFundAuthOrBlockService;
import com.appan.walletmaster.services.SourceOfFundCreateService;
import com.appan.walletmaster.services.SourceOfFundFetchService;
import com.appan.walletmaster.services.SourceOfFundModifyService;
import com.appan.walletmaster.services.WalletAuthOrBlockService;
import com.appan.walletmaster.services.WalletCreateService;
import com.appan.walletmaster.services.WalletFetchService;
import com.appan.walletmaster.services.WalletModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/wallet")
@Validated
public class WalletMasterController {

	@Autowired
	private WalletCreateService walletCreateService;

	@Autowired
	private WalletFetchService walletFetchService;

	@Autowired
	private WalletModifyService walletModifyService;

	@Autowired
	private WalletAuthOrBlockService walletAuthOrBlockService;

	@PostMapping("/wallet/create")
	public CommonResponse createWallet(@Valid @RequestBody WalletCreateRequest req) {
		return walletCreateService.createWallet(req);
	}

	@PostMapping("/wallet/fetch")
	public WalletFetchResponse fetchWallet(@Valid @RequestBody WalletFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return walletFetchService.fetchWallet(req, pageNo, pageSize);
	}

	@PostMapping("/wallet/modify")
	public CommonResponse modifyWallet(@Valid @RequestBody WalletModifyRequest req) {
		return walletModifyService.modifyWallet(req);
	}

	@PostMapping("/wallet/authorblock")
	public CommonResponse authorBlockWallet(@Valid @RequestBody WalletAuthOrBlockRequest req) {
		return walletAuthOrBlockService.authorBlockWallet(req);
	}

	@GetMapping("/wallet/allData")
	public WalletFetchResponse getAllWallets() {
		return walletFetchService.getAllData();
	}

	@Autowired
	private SourceOfFundCreateService sourceOfFundCreateService;

	@Autowired
	private SourceOfFundFetchService sourceOfFundFetchService;

	@Autowired
	private SourceOfFundModifyService sourceOfFundModifyService;

	@Autowired
	private SourceOfFundAuthOrBlockService sourceOfFundAuthOrBlockService;

	@PostMapping("/sourceoffund/create")
	public CommonResponse createSourceOfFund(@Valid @RequestBody SourceOfFundCreateRequest req) {
		return sourceOfFundCreateService.createSourceOfFund(req);
	}

	@PostMapping("/sourceoffund/fetch")
	public SourceOfFundFetchResponse fetchSourceOfFund(@Valid @RequestBody SourceOfFundFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return sourceOfFundFetchService.fetchSourceOfFund(req, pageNo, pageSize);
	}

	@PostMapping("/sourceoffund/modify")
	public CommonResponse modifySourceOfFund(@Valid @RequestBody SourceOfFundModifyRequest req) {
		return sourceOfFundModifyService.modifySourceOfFund(req);
	}

	@PostMapping("/sourceoffund/authorblock")
	public CommonResponse authorBlockSourceOfFund(@Valid @RequestBody SourceOfFundAuthOrBlockRequest req) {
		return sourceOfFundAuthOrBlockService.authorBlockSourceOfFund(req);
	}

	@GetMapping("/sourceoffund/allData")
	public SourceOfFundFetchResponse getAllSourceOfFunds() {
		return sourceOfFundFetchService.getAllData();
	}

}
