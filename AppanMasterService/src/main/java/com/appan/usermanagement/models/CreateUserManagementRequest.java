package com.appan.usermanagement.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserManagementRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@NotBlank(message = "User type cannot be blank")
	private String userType;

	@NotBlank(message = "Code cannot be blank")
	private String code;

	@NotBlank(message = "Entity type cannot be blank")
	private String entityType;

	@NotNull(message = "User agreement must not be null")
	private Boolean userAgreement;

	@NotBlank(message = "Full name cannot be blank")
	@Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
	private String fullName;

	@NotBlank(message = "Mobile number cannot be blank")
	private String mobileNumber;

	@NotBlank(message = "Email ID cannot be blank")
	@Email(message = "Email ID must be valid")
	private String emailId;

	@NotBlank(message = "Profile cannot be blank")
	private String profile;

	@NotBlank(message = "Parent cannot be blank")
	private String parent;

	private String parentId;

	private String parentName;

	@Valid
	private AddressRequest addressRequest;

	@Valid
	private BusinessDetailsRequest businessDetailsRequest;

	@Valid
	private VerificationDetailsRequest verificationDetailsRequest;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Boolean getUserAgreement() {
		return userAgreement;
	}

	public void setUserAgreement(Boolean userAgreement) {
		this.userAgreement = userAgreement;
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

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public AddressRequest getAddressRequest() {
		return addressRequest;
	}

	public void setAddressRequest(AddressRequest addressRequest) {
		this.addressRequest = addressRequest;
	}

	public BusinessDetailsRequest getBusinessDetailsRequest() {
		return businessDetailsRequest;
	}

	public void setBusinessDetailsRequest(BusinessDetailsRequest businessDetailsRequest) {
		this.businessDetailsRequest = businessDetailsRequest;
	}

	public VerificationDetailsRequest getVerificationDetailsRequest() {
		return verificationDetailsRequest;
	}

	public void setVerificationDetailsRequest(VerificationDetailsRequest verificationDetailsRequest) {
		this.verificationDetailsRequest = verificationDetailsRequest;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
