package com.appan.countrymaster.blockpo.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AuthOrBlockBlockPoRequest {

	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@Positive(message = "BlockPo Id must be a positive number")
	private Long blockPoId;

	@NotBlank(message = "Status Code cannot be Blank")
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getBlockPoId() {
		return blockPoId;
	}

	public void setBlockPoId(Long blockPoId) {
		this.blockPoId = blockPoId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
