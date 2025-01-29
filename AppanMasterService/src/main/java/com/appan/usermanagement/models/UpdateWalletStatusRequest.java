package com.appan.usermanagement.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdateWalletStatusRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	
	@NotEmpty(message = "userId cannot be null")
	@NotBlank(message = "userId cannot be blank")
	private String userId;
	
	@NotEmpty(message = "walletStatus cannot be null")
	@NotBlank(message = "walletStatus cannot be blank")
	private String walletStatus;
	
	@NotEmpty(message = "tpin cannot be null")
	@NotBlank(message = "tpin cannot be blank")
	private String tpin;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWalletStatus() {
		return walletStatus;
	}

	public void setWalletStatus(String walletStatus) {
		this.walletStatus = walletStatus;
	}
}