package com.appan.registration.steps.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class StepsCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "registrationStepsName cannot be null")
	@NotBlank(message = "registrationStepsName cannot be blank")
	@Size(min = 2, max = 50, message = "registrationStepsName must be between 2 and 50 characters")
	private String stepsName;

	@NotEmpty(message = "lable cannot be null")
	@NotBlank(message = "lable cannot be blank")
	@Size(min = 2, max = 50, message = "lable must be between 2 and 50 characters")
	private String label;

	@NotEmpty(message = "userType cannot be null")
	@NotBlank(message = "userType cannot be blank")
	private String userType;

	@NotEmpty(message = "entityType cannot be null")
	@NotBlank(message = "entityType cannot be blank")
	private String entityType;

	@NotEmpty(message = "registrationStepsRank cannot be null")
	@NotBlank(message = "registrationStepsRank cannot be blank")
	private String rank;

	private Boolean isMandatory;
	private Boolean isSignup;

	private String status;

	public String getUsername() {
		return username;
	}

	public String getStepsName() {
		return stepsName;
	}

	public void setStepsName(String stepsName) {
		this.stepsName = stepsName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Boolean getIsSignup() {
		return isSignup;
	}

	public void setIsSignup(Boolean isSignup) {
		this.isSignup = isSignup;
	}
}
