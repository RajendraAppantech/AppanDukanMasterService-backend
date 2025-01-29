package com.appan.usermanagement.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateNewUserRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@NotBlank(message = "User type cannot be blank")
	private String userType;

	@NotBlank(message = "Entity type cannot be blank")
	private String entityType;

	@NotBlank(message = "Code cannot be blank")
	private String code;

	@NotBlank(message = "Full name cannot be blank")
	@Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
	private String fullName;

	@NotBlank(message = "Mobile number cannot be blank")
	private String mobileNumber;

	@NotBlank(message = "Email ID cannot be blank")
	@Email(message = "Email ID must be valid")
	private String emailId;

	@NotBlank(message = "Address cannot be blank")
	@Size(max = 255, message = "Address cannot exceed 255 characters")
	private String address;

	@NotBlank(message = "Profile cannot be blank")
	private String profile;

	@NotBlank(message = "Parent cannot be blank")
	private String parent;

	@NotBlank(message = "ParentId cannot be blank")
	private String parentId;

	@NotBlank(message = "ParentName cannot be blank")
	private String parentName;

	@NotBlank(message = "Pincode cannot be blank")
	@Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be a 6-digit number")
	private String pincode;

	@NotBlank(message = "State cannot be blank")
	private String state;

	@NotBlank(message = "City cannot be blank")
	private String city;

	@NotBlank(message = "BlockPo cannot be blank")
	private String blockPo;

	@NotBlank(message = "UserMenuType cannot be blank")
	private String userMenuType;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBlockPo() {
		return blockPo;
	}

	public void setBlockPo(String blockPo) {
		this.blockPo = blockPo;
	}

	public String getUserMenuType() {
		return userMenuType;
	}

	public void setUserMenuType(String userMenuType) {
		this.userMenuType = userMenuType;
	}

}
