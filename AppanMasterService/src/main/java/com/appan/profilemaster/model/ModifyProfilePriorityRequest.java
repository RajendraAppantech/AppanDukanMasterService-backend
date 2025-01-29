package com.appan.profilemaster.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ModifyProfilePriorityRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	private Long id;
	private String userType;
	private String userName;
	private String allowedProfile1;
	private String allowedProfile2;
	private String allowedProfile3;
	private String allowedProfile4;
	private String allowedProfile5;
	private String status;

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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAllowedProfile1() {
		return allowedProfile1;
	}

	public void setAllowedProfile1(String allowedProfile1) {
		this.allowedProfile1 = allowedProfile1;
	}

	public String getAllowedProfile2() {
		return allowedProfile2;
	}

	public void setAllowedProfile2(String allowedProfile2) {
		this.allowedProfile2 = allowedProfile2;
	}

	public String getAllowedProfile3() {
		return allowedProfile3;
	}

	public void setAllowedProfile3(String allowedProfile3) {
		this.allowedProfile3 = allowedProfile3;
	}

	public String getAllowedProfile4() {
		return allowedProfile4;
	}

	public void setAllowedProfile4(String allowedProfile4) {
		this.allowedProfile4 = allowedProfile4;
	}

	public String getAllowedProfile5() {
		return allowedProfile5;
	}

	public void setAllowedProfile5(String allowedProfile5) {
		this.allowedProfile5 = allowedProfile5;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
