package com.appan.notification.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AlertCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "alertName cannot be null")
	@NotBlank(message = "alertName cannot be blank")
	@Size(min = 2, max = 50, message = "alertName must be between 2 and 50 characters")
	private String alertName;

	@NotEmpty(message = "parametersName cannot be null")
	@NotBlank(message = "parametersName cannot be blank")
	@Size(min = 2, max = 50, message = "parametersName must be between 2 and 50 characters")
	private String parametersName;
	private Boolean isEmail;
	private Boolean isSms;
	private Boolean isWhatsapp;
	
	@NotEmpty(message = "status cannot be null")
	@NotBlank(message = "status cannot be blank")
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAlertName() {
		return alertName;
	}

	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}

	public String getParametersName() {
		return parametersName;
	}

	public void setParametersName(String parametersName) {
		this.parametersName = parametersName;
	}

	public Boolean getIsEmail() {
		return isEmail;
	}

	public void setIsEmail(Boolean isEmail) {
		if(isEmail == null) {
			this.isEmail = false;
		}else {
			this.isEmail = isEmail;
		}
	}

	public Boolean getIsSms() {
		return isSms;
	}

	public void setIsSms(Boolean isSms) {
		if(isSms == null) {
			this.isSms = false;
		}else {
			this.isSms = isSms;
		}
	}

	public Boolean getIsWhatsapp() {
		return isWhatsapp;
	}

	public void setIsWhatsapp(Boolean isWhatsapp) {
		if(isWhatsapp == null) {
			this.isWhatsapp = false;
		}else {
			this.isWhatsapp = isWhatsapp;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
