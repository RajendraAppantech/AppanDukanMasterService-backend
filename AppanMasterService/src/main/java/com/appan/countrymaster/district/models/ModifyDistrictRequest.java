package com.appan.countrymaster.district.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ModifyDistrictRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotNull(message = "District ID cannot be null")
	private Long districtId;

	@NotEmpty(message = "District Name cannot be null")
	@NotBlank(message = "District Name cannot be blank")
	@Size(min = 2, max = 100, message = "District Name must be between 2 and 100 characters")
	private String districtName;

	@NotEmpty(message = "State Name cannot be null")
	@NotBlank(message = "State Name cannot be blank")
	@Size(min = 2, max = 50, message = "State Name must be between 2 and 50 characters")
	private String stateName;

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

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
