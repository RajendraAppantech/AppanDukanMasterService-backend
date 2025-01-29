package com.appan.usermanagement.models;

public class ModifyAddressRequest {

	private String address;
	private String pincode;
	private String state;
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
