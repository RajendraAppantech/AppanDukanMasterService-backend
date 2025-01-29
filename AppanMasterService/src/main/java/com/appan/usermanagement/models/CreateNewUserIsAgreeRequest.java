package com.appan.usermanagement.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateNewUserIsAgreeRequest {

	@NotBlank(message = "UserId name cannot be blank")
	@Size(max = 100, message = "UserId cannot exceed 100 characters")
	private String userId;

	private Boolean userAgreement;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getUserAgreement() {
		return userAgreement;
	}

	public void setUserAgreement(Boolean userAgreement) {
		this.userAgreement = userAgreement;
	}

}
