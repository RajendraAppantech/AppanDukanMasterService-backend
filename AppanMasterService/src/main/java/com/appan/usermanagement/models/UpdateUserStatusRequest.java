package com.appan.usermanagement.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdateUserStatusRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	
	@NotEmpty(message = "userId cannot be null")
	@NotBlank(message = "userId cannot be blank")
	private String userId;
	
	@NotEmpty(message = "userStatus cannot be null")
	@NotBlank(message = "userStatus cannot be blank")
	private String userStatus;
	
	@NotEmpty(message = "remark cannot be null")
	@NotBlank(message = "remark cannot be blank")
	private String remark;	
	
	@NotEmpty(message = "tpin cannot be null")
	@NotBlank(message = "tpin cannot be blank")
	private String tpin;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}