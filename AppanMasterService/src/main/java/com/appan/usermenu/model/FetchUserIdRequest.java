package com.appan.usermenu.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FetchUserIdRequest {

	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 30, message = "Username must be between 5 and 30 characters")
	private String username;

	@NotBlank(message = "User profile is required")
	@Size(min = 3, max = 30, message = "User profile must be between 3 and 30 characters")
	private String userProfile;

	@NotBlank(message = "User role is required")
	@Size(min = 3, max = 30, message = "User role must be between 3 and 30 characters")
	private String userRole;

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

}
