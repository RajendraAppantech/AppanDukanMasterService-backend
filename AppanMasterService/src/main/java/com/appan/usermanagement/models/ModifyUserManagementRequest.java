package com.appan.usermanagement.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ModifyUserManagementRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	private Long id;

	private String fullName;
	private String mobileNumber;
	private String emailId;

	private Boolean userAgreement;

	private String status;
	private String remark;

	@Valid
	private ModifyAddressRequest modifyAddressRequest;

	@Valid
	private ModifyBusinessDetailsRequest modifyBusinessDetailsRequest;

	@Valid
	private ModifyVerificationDetailsRequest modifyVerificationDetailsRequest;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Boolean getUserAgreement() {
		return userAgreement;
	}

	public void setUserAgreement(Boolean userAgreement) {
		this.userAgreement = userAgreement;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ModifyAddressRequest getModifyAddressRequest() {
		return modifyAddressRequest;
	}

	public void setModifyAddressRequest(ModifyAddressRequest modifyAddressRequest) {
		this.modifyAddressRequest = modifyAddressRequest;
	}

	public ModifyBusinessDetailsRequest getModifyBusinessDetailsRequest() {
		return modifyBusinessDetailsRequest;
	}

	public void setModifyBusinessDetailsRequest(ModifyBusinessDetailsRequest modifyBusinessDetailsRequest) {
		this.modifyBusinessDetailsRequest = modifyBusinessDetailsRequest;
	}

	public ModifyVerificationDetailsRequest getModifyVerificationDetailsRequest() {
		return modifyVerificationDetailsRequest;
	}

	public void setModifyVerificationDetailsRequest(ModifyVerificationDetailsRequest modifyVerificationDetailsRequest) {
		this.modifyVerificationDetailsRequest = modifyVerificationDetailsRequest;
	}

}
