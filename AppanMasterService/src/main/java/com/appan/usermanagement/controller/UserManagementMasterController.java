package com.appan.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appan.countrymaster.region.models.CommonResponse;
import com.appan.usermanagement.models.CommonKycResponse;
import com.appan.usermanagement.models.CreateNewUserBusinessDetailsRequest;
import com.appan.usermanagement.models.CreateNewUserIsAgreeRequest;
import com.appan.usermanagement.models.CreateNewUserKycRequest;
import com.appan.usermanagement.models.CreateNewUserRequest;
import com.appan.usermanagement.models.CreateUserManagementRequest;
import com.appan.usermanagement.models.GetParentDetailsResponse;
import com.appan.usermanagement.models.KycSummaryRequst;
import com.appan.usermanagement.models.ModifySettlementRequest;
import com.appan.usermanagement.models.ModifyUserManagementRequest;
import com.appan.usermanagement.models.UpdateCapBalRequst;
import com.appan.usermanagement.models.UpdateEmailIdRequest;
import com.appan.usermanagement.models.UpdateMobileNoRequest;
import com.appan.usermanagement.models.UpdateProfileRequest;
import com.appan.usermanagement.models.UpdateUserStatusRequest;
import com.appan.usermanagement.models.UpdateWalletStatusRequest;
import com.appan.usermanagement.models.UserManagementFetchRequest;
import com.appan.usermanagement.models.UserManagementFetchResponse;
import com.appan.usermanagement.models.UserMappingRequest;
import com.appan.usermanagement.services.AuthBlockUserManagementService;
import com.appan.usermanagement.services.CreateNewIsAgreeService;
import com.appan.usermanagement.services.CreateNewUserBusinessService;
import com.appan.usermanagement.services.CreateNewUserKycService;
import com.appan.usermanagement.services.CreateNewUserService;
import com.appan.usermanagement.services.CreateUserManagementService;
import com.appan.usermanagement.services.GetParentDetailsService;
import com.appan.usermanagement.services.ModifySettlementService;
import com.appan.usermanagement.services.ModifyUserManagKycService;
import com.appan.usermanagement.services.ModifyUserManagementService;
import com.appan.usermanagement.services.PendingKycStatusService;
import com.appan.usermanagement.services.UpdateBasicDetailsService;
import com.appan.usermanagement.services.UserManagementFetchService;
import com.appan.usermanagement.services.UserManagementSearchUserService;
import com.appan.usermanagement.services.UserMappingService;
import com.appan.usermanagement.services.ViewChildDetailsService;
import com.appan.usermanagement.services.ViewParentDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("master/UserManagement")
@Validated
public class UserManagementMasterController {

	@Autowired
	private CreateUserManagementService createService;

	@Autowired
	private UserManagementFetchService fetchService;

	@Autowired
	private ModifyUserManagementService modifyService;

	@Autowired
	private AuthBlockUserManagementService authBlockService;

	@Autowired
	private UserManagementSearchUserService managementSearchUserService;

	@Autowired
	private UpdateBasicDetailsService basicDetailsService;

	@Autowired
	private ViewChildDetailsService childDetailsService;

	@Autowired
	private ViewParentDetailsService parentDetailsService;

	@Autowired
	private PendingKycStatusService pendingKycStatusService;

	@Autowired
	private GetParentDetailsService getParentDetailsService;

	@Autowired
	private UserMappingService userMappingService;

	@Autowired
	private CreateNewUserService createNewUser;

	@Autowired
	private CreateNewUserBusinessService createNewUserBusiness;

	@Autowired
	private CreateNewUserKycService createNewUserKycService;

	@Autowired
	private CreateNewIsAgreeService createNewIsAgreeService;

	@Autowired
	private ModifyUserManagKycService modifyUserManagKycService;

	@Autowired
	private ModifySettlementService modifySettlementService;

	// old
	@PostMapping("/create")
	public CommonResponse create(@Valid @RequestBody CreateUserManagementRequest req) {
		return createService.create(req);
	}

	// Add Profile details
	@PostMapping("/createNewUser")
	public CommonResponse createNewUser(@Valid @RequestBody CreateNewUserRequest req) {
		return createNewUser.create(req);
	}

	// Add & update Business Details
	@PostMapping("/createNewUserBusiness")
	public CommonResponse createBusinessDetails(@Valid @RequestBody CreateNewUserBusinessDetailsRequest req) {
		return createNewUserBusiness.create(req);
	}

	// Add KYC Details
	@PostMapping("/createNewUserKyc")
	public CommonResponse createNewUserKyc(@Valid @RequestBody CreateNewUserKycRequest req) {
		return createNewUserKycService.create(req);
	}

	// Add & Update UserAgreement
	@PostMapping("/createNewIsAgree")
	public CommonResponse createNewUserKyc(@Valid @RequestBody CreateNewUserIsAgreeRequest req) {
		return createNewIsAgreeService.create(req);
	}

	@PostMapping("/fetch")
	public UserManagementFetchResponse fetch(@Valid @RequestBody UserManagementFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return fetchService.fetch(req, pageNo, pageSize);
	}

	// old
	@PostMapping("/modify")
	public CommonResponse modify(@Valid @RequestBody ModifyUserManagementRequest req) {
		return modifyService.modify(req);
	}

	// Update KYC
	@PostMapping("/modifyKyc")
	public CommonResponse modifyKyc(@Valid @RequestBody CreateNewUserKycRequest req) {
		return modifyUserManagKycService.modify(req);
	}

	@PostMapping("/settlementBank/updateStatus")
	public CommonResponse updateStatus(@Valid @RequestBody ModifySettlementRequest req) {
		return modifySettlementService.updateStatus(req);
	}

	@PostMapping("/authorblock")
	public CommonResponse authorblock(@Valid @RequestBody ModifyUserManagementRequest req) {
		return authBlockService.authorblock(req);
	}

	@GetMapping("/allData")
	public UserManagementFetchResponse getAllData() {
		return fetchService.getAllData();
	}

	@PostMapping("/searchUser")
	public UserManagementFetchResponse search(@Valid @RequestBody UserManagementFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return managementSearchUserService.search(req, pageNo, pageSize);
	}

	@PostMapping("/updateMobileNo")
	public CommonResponse updateMobileNo(@Valid @RequestBody UpdateMobileNoRequest req) {
		return basicDetailsService.updateMobileNo(req);
	}

	@PostMapping("/updateEmailId")
	public CommonResponse updateEmailId(@Valid @RequestBody UpdateEmailIdRequest req) {
		return basicDetailsService.updateEmailId(req);
	}

	@PostMapping("/updateProfile")
	public CommonResponse updateProfile(@Valid @RequestBody UpdateProfileRequest req) {
		return basicDetailsService.updateProfile(req);
	}

	@PostMapping("/updateWalletStatus")
	public CommonResponse updateWalletStatus(@Valid @RequestBody UpdateWalletStatusRequest req) {
		return basicDetailsService.updateWalletStatus(req);
	}

	@PostMapping("/updateUserStatus")
	public CommonResponse updateUserStatus(@Valid @RequestBody UpdateUserStatusRequest req) {
		return basicDetailsService.updateUserStatus(req);
	}

	@PostMapping("/kycSummary")
	public CommonKycResponse kycSummary(@Valid @RequestBody KycSummaryRequst req) {
		return basicDetailsService.kycSummary(req);
	}

	@GetMapping("/fullKycSummary")
	public CommonKycResponse fullKycSummary() {
		return basicDetailsService.fullKycSummary();
	}

	// need to pass parentId=userId
	@PostMapping("/viewChild")
	public UserManagementFetchResponse viewChild(@Valid @RequestBody UserManagementFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return childDetailsService.viewChild(req, pageNo, pageSize);
	}

	// need to pass userId = parentId
	@PostMapping("/viewParent")
	public UserManagementFetchResponse viewParent(@Valid @RequestBody UserManagementFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return parentDetailsService.viewParent(req, pageNo, pageSize);
	}

	@PostMapping("/updadeCapBalance")
	public CommonResponse updateCapBal(@Valid @RequestBody UpdateCapBalRequst req) {
		return basicDetailsService.updateCapBal(req);
	}

	@PostMapping("/pendingKyc")
	public UserManagementFetchResponse pendingKyc(@Valid @RequestBody UserManagementFetchRequest req,
			@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return pendingKycStatusService.pendingKyc(req, pageNo, pageSize);
	}

	@PostMapping("/getParentDetails")
	public GetParentDetailsResponse getParentDetails(@Valid @RequestBody UserManagementFetchRequest req) {
		return getParentDetailsService.getParentDetails(req);
	}

	@PostMapping("/getUserType")
	public GetParentDetailsResponse getUserType(@Valid @RequestBody UserManagementFetchRequest req) {
		return getParentDetailsService.getUserType(req);
	}

	@PostMapping("/getProfile")
	public CommonResponse getProfile(@Valid @RequestBody UserManagementFetchRequest req) {
		return basicDetailsService.getProfile(req);
	}

	// user mapping
	@PostMapping("/userMapping")
	public CommonResponse userMapping(@Valid @RequestBody UserMappingRequest req) {
		return userMappingService.userMapping(req);
	}

}
