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
import com.appan.userconfig.models.AuthOrBlockUserTypeOpRequest;
import com.appan.userconfig.models.AuthOrBlockUserTypeRequest;
import com.appan.userconfig.models.AuthOrBlockUsernameFormateRequest;
import com.appan.userconfig.models.CreateUserTypeOpRequest;
import com.appan.userconfig.models.CreateUserTypeRequest;
import com.appan.userconfig.models.CreateUsernameFormateRequest;
import com.appan.userconfig.models.FetchUserTypeOpRequest;
import com.appan.userconfig.models.FetchUserTypeOpResponse;
import com.appan.userconfig.models.FetchUserTypeRequest;
import com.appan.userconfig.models.FetchUserTypeResponse;
import com.appan.userconfig.models.FetchUsernameFormateRequest;
import com.appan.userconfig.models.FetchUsernameFormateResponse;
import com.appan.userconfig.models.ModifyUserTypeOpRequest;
import com.appan.userconfig.models.ModifyUserTypeRequest;
import com.appan.userconfig.models.ModifyUsernameFormateRequest;
import com.appan.userconfig.services.AuthBlockUserTypeOpService;
import com.appan.userconfig.services.AuthBlockUserTypeService;
import com.appan.userconfig.services.AuthBlockUsernameFormateService;
import com.appan.userconfig.services.CreateUserTypeOpService;
import com.appan.userconfig.services.CreateUserTypeService;
import com.appan.userconfig.services.CreateUsernameFormateService;
import com.appan.userconfig.services.FetchUserTypeOpService;
import com.appan.userconfig.services.FetchUserTypeService;
import com.appan.userconfig.services.FetchUsernameFormateService;
import com.appan.userconfig.services.ModifyUserTypeOpService;
import com.appan.userconfig.services.ModifyUserTypeService;
import com.appan.userconfig.services.ModifyUsernameFormateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/userconfig")
@Validated
public class UserConfigController {

	@Autowired
	private CreateUserTypeService createUserTypeService;

	@Autowired
	private FetchUserTypeService fetchUserTypeService;

	@Autowired
	private ModifyUserTypeService modifyUserTypeService;

	@Autowired
	private AuthBlockUserTypeService authBlockUserTypeService;

	@PostMapping("/usertype/create")
	public CommonResponse createUserType(@Valid @RequestBody CreateUserTypeRequest req) {
		return createUserTypeService.create(req);
	}

	@PostMapping("/usertype/fetch")
	public FetchUserTypeResponse fetchUserType(@Valid @RequestBody FetchUserTypeRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchUserTypeService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/usertype/modify")
	public CommonResponse modifyUserType(@Valid @RequestBody ModifyUserTypeRequest req) {
		return modifyUserTypeService.modify(req);
	}

	@PostMapping("/usertype/authorblock")
	public CommonResponse authorblockUserType(@Valid @RequestBody AuthOrBlockUserTypeRequest req) {
		return authBlockUserTypeService.authorblock(req);
	}

	@GetMapping("/usertype/allData")
	public FetchUserTypeResponse getAllDataUserType() {
		return fetchUserTypeService.getAllData();
	}

	@Autowired
	private CreateUsernameFormateService createUsernameFormateService;

	@Autowired
	private FetchUsernameFormateService fetchUsernameFormateService;

	@Autowired
	private ModifyUsernameFormateService modifyUsernameFormateService;

	@Autowired
	private AuthBlockUsernameFormateService authBlockUsernameFormateService;

	@PostMapping("/usernameformat/create")
	public CommonResponse createUsernameFormate(@Valid @RequestBody CreateUsernameFormateRequest req) {
		return createUsernameFormateService.create(req);
	}

	@PostMapping("/usernameformat/fetch")
	public FetchUsernameFormateResponse fetchUsernameFormate(@Valid @RequestBody FetchUsernameFormateRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchUsernameFormateService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/usernameformat/modify")
	public CommonResponse modifyUsernameFormate(@Valid @RequestBody ModifyUsernameFormateRequest req) {
		return modifyUsernameFormateService.modify(req);
	}

	@PostMapping("/usernameformat/authorblock")
	public CommonResponse authorblockUsernameFormate(@Valid @RequestBody AuthOrBlockUsernameFormateRequest req) {
		return authBlockUsernameFormateService.authorblock(req);
	}

	@GetMapping("/usernameformat/allData")
	public FetchUsernameFormateResponse getAllDataUsernameFormate() {
		return fetchUsernameFormateService.getAllData();
	}

	@Autowired
	private CreateUserTypeOpService createUserTypeOpService;

	@Autowired
	private FetchUserTypeOpService fetchUserTypeOpService;

	@Autowired
	private ModifyUserTypeOpService modifyUserTypeOpService;

	@Autowired
	private AuthBlockUserTypeOpService authBlockUserTypeOpService;

	@PostMapping("/usertypeop/create")
	public CommonResponse createUserTypeOp(@Valid @RequestBody CreateUserTypeOpRequest req) {
		return createUserTypeOpService.create(req);
	}

	@PostMapping("/usertypeop/fetch")
	public FetchUserTypeOpResponse fetchUserTypeOp(@Valid @RequestBody FetchUserTypeOpRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchUserTypeOpService.fetch(req, pageNo, pageSize);
	}

	@PostMapping("/usertypeop/modify")
	public CommonResponse modifyUserTypeOp(@Valid @RequestBody ModifyUserTypeOpRequest req) {
		return modifyUserTypeOpService.modify(req);
	}

	@PostMapping("/usertypeop/authorblock")
	public CommonResponse authorblockUserTypeOp(@Valid @RequestBody AuthOrBlockUserTypeOpRequest req) {
		return authBlockUserTypeOpService.authorblock(req);
	}

	@GetMapping("/usertypeop/allData")
	public FetchUserTypeOpResponse getAllDataUserTypeOp() {
		return fetchUserTypeOpService.getAllData();
	}
}
