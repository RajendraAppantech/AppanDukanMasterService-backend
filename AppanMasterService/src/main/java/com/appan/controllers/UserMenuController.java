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
import com.appan.usermenu.model.AuthOrBlockUsermenuRequest;
import com.appan.usermenu.model.CreateUsermenuRequest;
import com.appan.usermenu.model.FetchUserIdRequest;
import com.appan.usermenu.model.FetchUserMenuReq;
import com.appan.usermenu.model.FetchUserRoleRequest;
import com.appan.usermenu.model.FetchUsermenuResponse;
import com.appan.usermenu.model.FetchmenuRequest;
import com.appan.usermenu.model.ModifyUsermenuRequest;
import com.appan.usermenu.model.UpdateUserMenuReq;
import com.appan.usermenu.model.UsermenuRequest;
import com.appan.usermenu.services.FetchMenuService;
import com.appan.usermenu.services.FetchUserIdService;
import com.appan.usermenu.services.FetchUserMenuService;
import com.appan.usermenu.services.FetchUserRoleService;
import com.appan.usermenu.services.GetUserProfileService;
import com.appan.usermenu.services.UpdateUserMenuService;
import com.appan.usermenu.services.UsermenuAuthOrBlockService;
import com.appan.usermenu.services.UsermenuBrowseService;
import com.appan.usermenu.services.UsermenuCreateService;
import com.appan.usermenu.services.UsermenuModifyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/role")
@Validated
public class UserMenuController {

	@Autowired
	private UsermenuCreateService usermenuCreateService;

	@Autowired
	private UsermenuBrowseService usermenuBrowseService;

	@Autowired
	private UsermenuModifyService usermenuModifyService;

	@Autowired
	private UsermenuAuthOrBlockService usermenuAuthOrBlockService;

	@Autowired
	private FetchUserIdService fetchUserIdService;

	@Autowired
	private FetchMenuService fetchMenuService;

	@Autowired
	private FetchUserRoleService fetchUserRoleService;

	@Autowired
	private FetchUserMenuService fetchUserMenuService;

	@Autowired
	private UpdateUserMenuService updateUserMenuService;

	@Autowired
	private GetUserProfileService getUserProfileService;

	@PostMapping("/create")
	public CommonResponse usermenuCreate(@Valid @RequestBody CreateUsermenuRequest req) {
		return usermenuCreateService.createUsermenu(req);
	}

	@PostMapping("/fetch")
	public FetchUsermenuResponse usermenuFetch(@Valid @RequestBody UsermenuRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return usermenuBrowseService.fetchUsermenu(req, pageNo, pageSize);
	}

	@PostMapping("/modify")
	public CommonResponse usermenuModify(@Valid @RequestBody ModifyUsermenuRequest req) {
		return usermenuModifyService.modifyUsermenu(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse usermenuauthorblock(@Valid @RequestBody AuthOrBlockUsermenuRequest req) {
		return usermenuAuthOrBlockService.authorblockUsermenu(req);
	}

	@PostMapping("/fetchuserrole")
	public CommonResponse fetchuserrole(@Valid @RequestBody FetchUserRoleRequest req) {
		return fetchUserRoleService.fetchuserrole(req);
	}

	@PostMapping("/fetchuserid")
	public CommonResponse fetchuserid(@Valid @RequestBody FetchUserIdRequest req) {
		return fetchUserIdService.fetchuserid(req);
	}

	@PostMapping("/fetchmenu")
	public CommonResponse fetchmenu(@Valid @RequestBody FetchmenuRequest req) {
		return fetchMenuService.fetchmenu(req);
	}

	@PostMapping("/fetchUserMenu")
	public CommonResponse fetchUserMenu(@Valid @RequestBody FetchUserMenuReq req) {
		return fetchUserMenuService.fetchUserMenu(req);
	}

	@PostMapping("/updateUserMenu")
	public CommonResponse updateUserMenu(@Valid @RequestBody UpdateUserMenuReq req) {
		return updateUserMenuService.updateUserMenu(req);
	}

	@GetMapping("/allUserProfile")
	public CommonResponse getAllUserProfile() {
		return getUserProfileService.getAllUserProfile();
	}

}
