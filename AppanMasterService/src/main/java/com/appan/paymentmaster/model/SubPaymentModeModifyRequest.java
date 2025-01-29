package com.appan.paymentmaster.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubPaymentModeModifyRequest {

	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	private Long id;
	private String subPaymentMode;
	private String paymentMode;
	private String image;
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubPaymentMode() {
		return subPaymentMode;
	}

	public void setSubPaymentMode(String subPaymentMode) {
		this.subPaymentMode = subPaymentMode;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
