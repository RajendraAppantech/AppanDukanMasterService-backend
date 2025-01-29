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
import com.appan.registration.fields.model.FieldsAuthOrBlockRequest;
import com.appan.registration.fields.model.FieldsCreateRequest;
import com.appan.registration.fields.model.FieldsFetchRequest;
import com.appan.registration.fields.model.FieldsFetchResponse;
import com.appan.registration.fields.model.FieldsModifyRequest;
import com.appan.registration.fields.services.FieldsAuthOrBlockService;
import com.appan.registration.fields.services.FieldsCreateService;
import com.appan.registration.fields.services.FieldsFetchService;
import com.appan.registration.fields.services.FieldsModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/registration/fields")
@Validated
public class RegistrationFieldsController {

	@Autowired
	private FieldsCreateService createService;

	@Autowired
	private FieldsFetchService fetchService;

	@Autowired
	private FieldsModifyService modifyService;

	@Autowired
	private FieldsAuthOrBlockService authBlockService;

	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody FieldsCreateRequest req) {
		return createService.create(req);
	}

	@PostMapping("/fetch")
	public FieldsFetchResponse fetch(@Valid @RequestBody FieldsFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody FieldsModifyRequest req) {
		return modifyService.modify(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody FieldsAuthOrBlockRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public FieldsFetchResponse getAllData() {
		return fetchService.getAllData();
	}

}