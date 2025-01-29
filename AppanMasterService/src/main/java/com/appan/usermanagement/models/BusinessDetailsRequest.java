package com.appan.usermanagement.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BusinessDetailsRequest {

	@NotBlank(message = "Firm/Shop name cannot be blank")
	@Size(max = 100, message = "Firm/Shop name cannot exceed 100 characters")
	private String firmShopName;

	@NotBlank(message = "Business address cannot be blank")
	@Size(max = 255, message = "Business address cannot exceed 255 characters")
	private String businessAddress;

	public String getFirmShopName() {
		return firmShopName;
	}

	public void setFirmShopName(String firmShopName) {
		this.firmShopName = firmShopName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

}
