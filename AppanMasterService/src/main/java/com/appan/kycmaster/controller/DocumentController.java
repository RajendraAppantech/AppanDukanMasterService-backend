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
import com.appan.kyc.document.model.AuthOrBlockDocumentRequest;
import com.appan.kyc.document.model.CreateDocumentRequest;
import com.appan.kyc.document.model.FetchDocumentRequest;
import com.appan.kyc.document.model.FetchDocumentResponse;
import com.appan.kyc.document.model.ModifyDocumentRequest;
import com.appan.kycmaster.services.AuthBlockDocumentService;
import com.appan.kycmaster.services.DocumentService;
import com.appan.kycmaster.services.FetchDocumentService;
import com.appan.kycmaster.services.ModifyDocumentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/document")
@Validated
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	@Autowired
	private AuthBlockDocumentService authBlockDocumentService;

	@Autowired
	private FetchDocumentService fetchDocumentService;

	@Autowired
	private ModifyDocumentService modifyDocumentService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateDocumentRequest req) {
		return documentService.create(req);
	}

	@PostMapping("/fetch")
	public FetchDocumentResponse fetch(@Valid @RequestBody FetchDocumentRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchDocumentService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyDocumentRequest req) {
		return modifyDocumentService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authOrBlock(@Valid @RequestBody AuthOrBlockDocumentRequest req) {
		return authBlockDocumentService.authOrBlock(req);
	}

	@GetMapping("/allData")
	public FetchDocumentResponse getAllData() {
		return fetchDocumentService.getAllData();
	}
}
