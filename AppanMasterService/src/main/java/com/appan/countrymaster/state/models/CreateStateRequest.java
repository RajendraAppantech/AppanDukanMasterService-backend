package com.appan.countrymaster.state.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CreateStateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "State name cannot be null or empty")
	@NotBlank(message = "State name cannot be Blank")
	@Size(min = 2, max = 100, message = "State name must be between 2 and 100 characters")
	private String stateName;

	@NotEmpty(message = "Country name cannot be null or empty")
	@NotBlank(message = "Country name cannot be Blank")
	@Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters")
	private String countryName;

	// @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Fingpay State ID must be
	// alphanumeric")
	@Size(min = 5, max = 20, message = "Fingpay State ID must be between 5 and 20 characters")
	private String fingpayStateId;

	// @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Gift Card State ID must be
	// alphanumeric")
	@Size(min = 5, max = 20, message = "Gift Card State ID must be between 5 and 20 characters")
	private String giftCardStateId;

	@NotBlank(message = "Status Code cannot be Blank")
	private String status;

	// Getters and Setters

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getFingpayStateId() {
		return fingpayStateId;
	}

	public void setFingpayStateId(String fingpayStateId) {
		this.fingpayStateId = fingpayStateId;
	}

	public String getGiftCardStateId() {
		return giftCardStateId;
	}

	public void setGiftCardStateId(String giftCardStateId) {
		this.giftCardStateId = giftCardStateId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
