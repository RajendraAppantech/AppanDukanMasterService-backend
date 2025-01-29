package com.appan.usermanagement.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdateEmailIdRequest {


	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	
	@NotEmpty(message = "userId cannot be null")
	@NotBlank(message = "userId cannot be blank")
	private String userId;
	
	@NotEmpty(message = "emailId cannot be null")
	@NotBlank(message = "emailId cannot be blank")
	private String emailId;
	
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
}
