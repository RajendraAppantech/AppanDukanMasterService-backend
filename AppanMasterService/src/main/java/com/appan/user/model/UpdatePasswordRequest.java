package com.appan.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdatePasswordRequest {

	@NotNull(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	private String username;

	@NotNull(message = "old password cannot be null")
	@NotBlank(message = "old password cannot be Blank")
	private String oldPassword;

	@NotNull(message = "password cannot be null")
	@NotBlank(message = "password cannot be Blank")
	private String password;

	@NotNull(message = "confirm password cannot be null")
	@NotBlank(message = "confirm password cannot be Blank")
	private String confirmPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}
