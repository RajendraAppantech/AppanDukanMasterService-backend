package com.appan.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 10, message = "Username must be between 5 and 10 characters")
	private String username;
	@NotBlank(message = "User ID cannot be Blank")
	@Size(min = 2, max = 10, message = "User ID must be between 5 and 10 characters")
	private String userId;
	@NotBlank(message = "Name cannot be Blank")
	@Size(min = 2, max = 100, message = "Name must be between 2 and 30 characters")
	private String name;
	@NotBlank(message = "Mobile No cannot be Blank")
	@Size(min = 10, max = 13, message = "Mobile No must be between 10 and 13 characters")
	private String mobileNo;
	private String emailId;
	private String userRole;
	private String userProfile;
	private String userCode;
	private String status;
	private String fromDate;
	private String toDate;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
}