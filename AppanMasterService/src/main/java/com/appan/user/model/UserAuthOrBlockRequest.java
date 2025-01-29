package com.appan.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserAuthOrBlockRequest {

	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 10, message = "Username must be between 5 and 10 characters")
	private String username;
	
	@NotBlank(message = "User ID cannot be Blank")
    @Size(min = 2, max = 100, message = "User ID must be between 5 and 10 characters")
    private String userId;

	@NotBlank(message = "Status cannot be Blank")
	private String status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
