package com.appan.usermanagement.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class FromUserData {


	@NotEmpty(message = "fromUserId cannot be null")
	@NotBlank(message = "fromUserId cannot be blank")
	private String fromUserId;

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
}
