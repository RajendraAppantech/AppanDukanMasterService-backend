package com.appan.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ForgotPasswordRequest {

	@NotNull(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	private String username;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
