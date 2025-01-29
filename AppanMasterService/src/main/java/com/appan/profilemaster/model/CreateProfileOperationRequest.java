package com.appan.profilemaster.model;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CreateProfileOperationRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	private String operationName;
	private String profileName;
	private Date startDate;
	private Date endDate;
	private Boolean isDateValidity;
	private Boolean isOnlyUi;
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsDateValidity() {
		return isDateValidity;
	}

	public void setIsDateValidity(Boolean isDateValidity) {
		this.isDateValidity = isDateValidity;
	}

	public Boolean getIsOnlyUi() {
		return isOnlyUi;
	}

	public void setIsOnlyUi(Boolean isOnlyUi) {
		this.isOnlyUi = isOnlyUi;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
