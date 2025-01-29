package com.appan.bankmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.bankmaster.userbank.model.FetchUserBankResponse;
import com.appan.bankmaster.userbank.model.UserBankAuthOrBlockRequest;
import com.appan.bankmaster.userbank.model.UserBankCreateRequest;
import com.appan.bankmaster.userbank.model.UserBankFetchRequest;
import com.appan.bankmaster.userbank.model.UserBankModifyRequest;
import com.appan.bankmaster.userbank.services.UserBankAuthOrBlockService;
import com.appan.bankmaster.userbank.services.UserBankCreateService;
import com.appan.bankmaster.userbank.services.UserBankFetchService;
import com.appan.bankmaster.userbank.services.UserBankModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/userbank")
@Validated
public class UserBankController {

	@Autowired
	private UserBankCreateService userBankCreateService;

	@Autowired
	private UserBankFetchService userBankFetchService;

	@Autowired
	private UserBankModifyService userBankModifyService;

	@Autowired
	private UserBankAuthOrBlockService userBankAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createUserBank(@Valid @RequestBody UserBankCreateRequest req) {
		return userBankCreateService.createUserBank(req);
	}

	@PostMapping("/fetch")
	public FetchUserBankResponse fetchUserBank(@Valid @RequestBody UserBankFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return userBankFetchService.fetchUserBank(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyUserBank(@Valid @RequestBody UserBankModifyRequest req) {
		return userBankModifyService.modifyUserBank(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockUserBank(@Valid @RequestBody UserBankAuthOrBlockRequest req) {
		return userBankAuthOrBlockService.authorBlockUserBank(req);
	}

	@GetMapping("/allData")
	public FetchUserBankResponse getAllUserBank() {
		return userBankFetchService.getAllData();
	}
}
