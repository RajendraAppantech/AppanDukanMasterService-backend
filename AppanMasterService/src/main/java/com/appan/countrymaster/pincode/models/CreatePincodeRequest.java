package com.appan.countrymaster.pincode.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreatePincodeRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotBlank(message = "Pincode cannot be blank")
	@Size(min = 6, max = 6, message = "Pincode must be 6 digits")
	@Pattern(regexp = "^[0-9]{6}$", message = "Pincode must contain exactly 6 digits")
	private String pincode;

	@Size(min = 2, max = 100, message = "Block PO Name must be between 2 and 100 characters")
	private String blockPoName;

	@NotBlank(message = "Status Code cannot be Blank")
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getBlockPoName() {
		return blockPoName;
	}

	public void setBlockPoName(String blockPoName) {
		this.blockPoName = blockPoName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
