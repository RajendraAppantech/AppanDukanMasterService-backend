package com.appan.usermanagement.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AddressRequest {

	@NotBlank(message = "Address cannot be blank")
	@Size(max = 255, message = "Address cannot exceed 255 characters")
	private String address;

	@NotBlank(message = "Pincode cannot be blank")
	@Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be a 6-digit number")
	private String pincode;

	@NotBlank(message = "State cannot be blank")
	private String state;

	@NotBlank(message = "City cannot be blank")
	private String city;

	private String blockPo;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBlockPo() {
		return blockPo;
	}

	public void setBlockPo(String blockPo) {
		this.blockPo = blockPo;
	}

}
