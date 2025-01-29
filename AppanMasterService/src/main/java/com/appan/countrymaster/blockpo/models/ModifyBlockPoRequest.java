package com.appan.countrymaster.blockpo.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ModifyBlockPoRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@Positive(message = "BlockPo Id must be a positive number")
	private Long blockPoId;

	@Size(min = 2, max = 100, message = "Block PO Name must be between 2 and 100 characters")
	private String blockPoName;

	@Size(min = 2, max = 100, message = "City Name must be between 2 and 100 characters")
	private String cityName;

	@NotEmpty(message = "Status cannot be null")
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

	public String getBlockPoName() {
		return blockPoName;
	}

	public void setBlockPoName(String blockPoName) {
		this.blockPoName = blockPoName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
