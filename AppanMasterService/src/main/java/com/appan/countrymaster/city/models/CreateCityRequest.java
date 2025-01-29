package com.appan.countrymaster.city.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CreateCityRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "City Name cannot be null")
	@NotBlank(message = "City Name cannot be blank")
	@Size(min = 2, max = 100, message = "City Name must be between 2 and 100 characters")
	private String cityName;

	@NotEmpty(message = "District Name cannot be null")
	@NotBlank(message = "District Name cannot be blank")
	@Size(min = 2, max = 100, message = "District Name must be between 2 and 100 characters")
	private String districtName;

	@NotEmpty(message = "State Name cannot be null")
	@NotBlank(message = "State Name cannot be blank")
	@Size(min = 2, max = 100, message = "State Name must be between 2 and 100 characters")
	private String stateName;

	@NotBlank(message = "Status Code cannot be Blank")
	private String status;

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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
