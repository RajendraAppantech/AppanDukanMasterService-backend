package com.appan.bankmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.bankmaster.companybankaccount.model.CompanyBankAccountAuthOrBlockRequest;
import com.appan.bankmaster.companybankaccount.model.CompanyBankAccountCreateRequest;
import com.appan.bankmaster.companybankaccount.model.CompanyBankAccountFetchRequest;
import com.appan.bankmaster.companybankaccount.model.CompanyBankAccountModifyRequest;
import com.appan.bankmaster.companybankaccount.model.FetchCompanyBankAccountResponse;
import com.appan.bankmaster.services.CompanyBankAccountAuthOrBlockService;
import com.appan.bankmaster.services.CompanyBankAccountCreateService;
import com.appan.bankmaster.services.CompanyBankAccountFetchService;
import com.appan.bankmaster.services.CompanyBankAccountModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/companybankaccount")
@Validated
public class CompanyBankAccountController {

	@Autowired
	private CompanyBankAccountCreateService companyBankAccountCreateService;

	@Autowired
	private CompanyBankAccountFetchService companyBankAccountFetchService;

	@Autowired
	private CompanyBankAccountModifyService companyBankAccountModifyService;

	@Autowired
	private CompanyBankAccountAuthOrBlockService companyBankAccountAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createCompanyBankAccount(@Valid @RequestBody CompanyBankAccountCreateRequest req) {
		return companyBankAccountCreateService.createCompanyBankAccount(req);
	}

	@PostMapping("/fetch")
	public FetchCompanyBankAccountResponse fetchCompanyBankAccount(
			@Valid @RequestBody CompanyBankAccountFetchRequest req, @RequestParam(defaultValue = "1") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		return companyBankAccountFetchService.fetchCompanyBankAccount(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyCompanyBankAccount(@Valid @RequestBody CompanyBankAccountModifyRequest req) {
		return companyBankAccountModifyService.modifyCompanyBankAccount(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockCompanyBankAccount(@Valid @RequestBody CompanyBankAccountAuthOrBlockRequest req) {
		return companyBankAccountAuthOrBlockService.authorBlockCompanyBankAccount(req);
	}

	@GetMapping("/allData")
	public FetchCompanyBankAccountResponse getAllCompanyBankAccounts() {
		return companyBankAccountFetchService.getAllCompanyBankAccounts();
	}
}
