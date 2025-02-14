package com.appan.profilemaster.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ModifyProfilesMasterRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	private Long id;
	private String profileName;
	private String code;
	private String userType;
	private Boolean signupProfile;
	private String status;
	private String menu;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Boolean getSignupProfile() {
		return signupProfile;
	}

	public void setSignupProfile(Boolean signupProfile) {
		this.signupProfile = signupProfile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

}
