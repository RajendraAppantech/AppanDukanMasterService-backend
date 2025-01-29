package com.appan.userconfig.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ModifyUsernameFormateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	private Long id;
	private String usernameFormat;
	private String userType;
	private String prefix;
	private String suffix;
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

	public String getUsernameFormat() {
		return usernameFormat;
	}

	public void setUsernameFormat(String usernameFormat) {
		this.usernameFormat = usernameFormat;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
