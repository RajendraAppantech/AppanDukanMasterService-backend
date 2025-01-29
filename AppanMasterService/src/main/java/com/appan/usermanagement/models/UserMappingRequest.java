package com.appan.usermanagement.models;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserMappingRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "fromUserType cannot be null")
	@NotBlank(message = "fromUserType cannot be blank")
	private String fromUserType;

	@Valid
	private List<FromUserData> fromUserData;

	@NotEmpty(message = "toUserType cannot be null")
	@NotBlank(message = "toUserType cannot be blank")
	private String toUserType;

	@NotEmpty(message = "toUserId cannot be null")
	@NotBlank(message = "toUserId cannot be blank")
	private String toUserId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFromUserType() {
		return fromUserType; 
	}

	public void setFromUserType(String fromUserType) {
		this.fromUserType = fromUserType;
	}

	public List<FromUserData> getFromUserData() {
		return fromUserData;
	}

	public void setFromUserData(List<FromUserData> fromUserData) {
		this.fromUserData = fromUserData;
	}

	public String getToUserType() {
		return toUserType;
	}

	public void setToUserType(String toUserType) {
		this.toUserType = toUserType;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

}
