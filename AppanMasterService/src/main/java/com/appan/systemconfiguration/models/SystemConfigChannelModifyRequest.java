package com.appan.systemconfiguration.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SystemConfigChannelModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@NotNull(message = "systemConfigChannelId cannot be Blank")
	private Long systemConfigChannelId;

	@NotBlank(message = "regionName cannot be Blank")
	@NotEmpty(message = "regionName cannot be null")
	@Size(min = 2, max = 20, message = "regionName must be between 2 and 20 characters")
	private String channelName;

	@NotBlank(message = "status cannot be Blank")
	@NotEmpty(message = "status cannot be null")
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

	public Long getSystemConfigChannelId() {
		return systemConfigChannelId;
	}

	public void setSystemConfigChannelId(Long systemConfigChannelId) {
		this.systemConfigChannelId = systemConfigChannelId;
	}

	
	
	
}
