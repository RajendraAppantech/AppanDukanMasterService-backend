package com.appan.systemconfiguration.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SystemConfigChannelCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@NotBlank(message = "channelName cannot be Blank")
	@NotEmpty(message = "channelName cannot be null")
	@Size(min = 2, max = 20, message = "channelName must be between 2 and 20 characters")
	private String channelName;

	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
