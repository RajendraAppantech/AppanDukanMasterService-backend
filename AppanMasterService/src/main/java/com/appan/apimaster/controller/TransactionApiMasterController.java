package com.appan.apimaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.apimaster.models.FetchTransactionApiResponse;
import com.appan.apimaster.models.TransactionApiAuthOrBlockRequest;
import com.appan.apimaster.models.TransactionApiCreateRequest;
import com.appan.apimaster.models.TransactionApiFetchRequest;
import com.appan.apimaster.models.TransactionApiModifyRequest;
import com.appan.apimaster.services.TransactionApiAuthOrBlockService;
import com.appan.apimaster.services.TransactionApiCreateService;
import com.appan.apimaster.services.TransactionApiFetchService;
import com.appan.apimaster.services.TransactionApiModifyService;
import com.appan.countrymaster.region.models.CommonResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/transactionApi")
@Validated
public class TransactionApiMasterController {

	@Autowired
	private TransactionApiCreateService transactionApiCreateService;

	@Autowired
	private TransactionApiFetchService transactionApiFetchService;

	@Autowired
	private TransactionApiModifyService transactionApiModifyService;

	@Autowired
	private TransactionApiAuthOrBlockService transactionApiAuthOrBlockService;

	@PostMapping("/create")
	public CommonResponse createTransactionApi(@Valid @RequestBody TransactionApiCreateRequest req) {
		return transactionApiCreateService.createTransactionApi(req);
	}

	@PostMapping("/fetch")
	public FetchTransactionApiResponse fetchTransactionApi(@Valid @RequestBody TransactionApiFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return transactionApiFetchService.fetchTransactionApi(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyTransactionApi(@Valid @RequestBody TransactionApiModifyRequest req) {
		return transactionApiModifyService.modifyTransactionApi(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorBlockTransactionApi(@Valid @RequestBody TransactionApiAuthOrBlockRequest req) {
		return transactionApiAuthOrBlockService.authorBlockTransactionApi(req);
	}

	@GetMapping("/allData")
	public FetchTransactionApiResponse getAllTransactionApis() {
		return transactionApiFetchService.getAllData();
	}
}
