package com.appan.countrymaster.district.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthOrBlockDistrictRequest {

	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@Min(value = 1, message = "ID must be a positive number")
	private Long districtId;

	@NotBlank(message = "Status Code cannot be Blank")
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
