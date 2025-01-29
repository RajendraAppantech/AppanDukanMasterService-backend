package com.appan.usermenu.model;

import jakarta.validation.constraints.NotBlank;

public class UpdateUserMenuReq {

	@NotBlank(message = "Username is required")
	private String username;

	private String userId;

	@NotBlank(message = "User Profile is required")
	private String userProfile;

	@NotBlank(message = "User Menu is required")
	private String userMenu;

	private String userRole;

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

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public String getUserMenu() {
		return userMenu;
	}

	public void setUserMenu(String userMenu) {
		this.userMenu = userMenu;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

}
