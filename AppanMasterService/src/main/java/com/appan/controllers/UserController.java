package com.appan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.user.model.FetchUserRequest;
import com.appan.user.model.FetchUserResponse;
import com.appan.user.model.ForgotPasswordRequest;
import com.appan.user.model.UpdatePasswordRequest;
import com.appan.user.model.UpdateTpinRequest;
import com.appan.user.model.UserAuthOrBlockRequest;
import com.appan.user.model.UserRequest;
import com.appan.user.services.AuthOrBlockUserService;
import com.appan.user.services.CreateUserService;
import com.appan.user.services.FetchUserService;
import com.appan.user.services.LogoutService;
import com.appan.user.services.ModifyUserService;
import com.appan.user.services.UpdatePasswordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/user")
@Validated
public class UserController {

	@Autowired
	private LogoutService logoutService;

	@Autowired
	private CreateUserService createUserService;

	@Autowired
	private AuthOrBlockUserService authOrBlockUserService;

	@Autowired
	private ModifyUserService modifyUserService;

	@Autowired
	private FetchUserService fetchUserService;

	@Autowired
	private UpdatePasswordService updatePasswordService;

	@PostMapping("/create")
	public CommonResponse createUser(@Valid @RequestBody UserRequest req) {
		return createUserService.createUser(req);
	}

	@PostMapping("/fetch")
	public FetchUserResponse fetchUser(@Valid @RequestBody FetchUserRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchUserService.fetchUser(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse modifyUser(@Valid @RequestBody UserRequest req) {
		return modifyUserService.modifyUser(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblockUser(@Valid @RequestBody UserAuthOrBlockRequest req) {
		return authOrBlockUserService.authorblockUser(req);
	}

	@PostMapping("/logout")
	public CommonResponse logout(@Valid @RequestBody ForgotPasswordRequest req) {
		return logoutService.logout(req);
	}

	@PostMapping("/changePassword")
	public CommonResponse updatePassword(@Valid @RequestBody UpdatePasswordRequest req) {
		return updatePasswordService.updatePassword(req);
	}

	@PostMapping("/changeTpin")
	public CommonResponse changeTpin(@Valid @RequestBody UpdateTpinRequest req) {
		return updatePasswordService.changeTpin(req);
	}
}
