package com.appan.usermenu.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthOrBlockUsermenuRequest {

	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 10, message = "Username must be between 5 and 10 characters")
	private String username;

	@NotBlank(message = "userProfile cannot be Blank")
	@Size(min = 3, max = 10, message = "User profile must be between 3 and 10 characters")
	private String userProfile;

	@NotBlank(message = "userRole cannot be Blank")
	@Size(min = 3, max = 10, message = "User role must be between 3 and 10 characters")
	private String userRole;

	@NotBlank(message = "Status Code cannot be Blank")
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
