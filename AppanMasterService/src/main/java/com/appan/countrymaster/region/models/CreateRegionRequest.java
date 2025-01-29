package com.appan.countrymaster.region.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CreateRegionRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@NotBlank(message = "regionName cannot be Blank")
	@NotEmpty(message = "regionName cannot be null")
	@Size(min = 2, max = 20, message = "regionName must be between 2 and 20 characters")
	private String regionName;

	@NotBlank(message = "regionType cannot be Blank")
	@NotEmpty(message = "regionType cannot be null")
	@Size(min = 2, max = 30, message = "regionType must be between 2 and 20 characters")
	private String regionType;
	
	@NotBlank(message = "Status Code cannot be Blank")
	private String status;

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionType() {
		return regionType;
	}

	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
