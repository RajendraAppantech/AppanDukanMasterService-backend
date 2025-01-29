package com.appan.notification.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SmsTemplateCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "smsType cannot be null")
	@NotBlank(message = "smsType cannot be blank")
	@Size(min = 2, max = 50, message = "smsType must be between 2 and 50 characters")
	private String smsType;

	@NotEmpty(message = "templateId cannot be null")
	@NotBlank(message = "templateId cannot be blank")
	@Size(min = 2, max = 50, message = "templateId must be between 2 and 50 characters")
	private String templateId;
	
	@NotEmpty(message = "smsContent cannot be null")
	@NotBlank(message = "smsContent cannot be blank")
	private String smsContent;
	
	@NotEmpty(message = "status cannot be null")
	@NotBlank(message = "status cannot be blank")
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
