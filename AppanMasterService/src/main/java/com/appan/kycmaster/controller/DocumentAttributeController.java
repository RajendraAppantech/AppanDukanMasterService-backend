package com.appan.kycmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.kyc.documentattribute.model.AuthOrBlockDocumentAttributeRequest;
import com.appan.kyc.documentattribute.model.CreateDocumentAttributeRequest;
import com.appan.kyc.documentattribute.model.FetchDocumentAttributeRequest;
import com.appan.kyc.documentattribute.model.FetchDocumentAttributeResponse;
import com.appan.kyc.documentattribute.model.ModifyDocumentAttributeRequest;
import com.appan.kycmaster.services.AuthBlockDocumentAttributeService;
import com.appan.kycmaster.services.DocumentAttributeService;
import com.appan.kycmaster.services.FetchDocumentAttributeService;
import com.appan.kycmaster.services.ModifyDocumentAttributeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/documentattribute")
@Validated
public class DocumentAttributeController {

	@Autowired
	private DocumentAttributeService documentAttributeService;

	@Autowired
	private AuthBlockDocumentAttributeService authBlockService;

	@Autowired
	private FetchDocumentAttributeService fetchDocumentAttributeService;

	@Autowired
	private ModifyDocumentAttributeService modifyDocumentAttributeService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateDocumentAttributeRequest req) {
		return documentAttributeService.create(req);
	}

	@PostMapping("/fetch")
	public FetchDocumentAttributeResponse fetch(@Valid @RequestBody FetchDocumentAttributeRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchDocumentAttributeService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyDocumentAttributeRequest req) {
		return modifyDocumentAttributeService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authOrBlock(@Valid @RequestBody AuthOrBlockDocumentAttributeRequest req) {
		return authBlockService.authOrBlock(req);
	}

	@GetMapping("/allData")
	public FetchDocumentAttributeResponse getAllData() {
		return fetchDocumentAttributeService.getAllData();
	}
}
