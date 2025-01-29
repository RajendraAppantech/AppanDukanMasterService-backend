package com.appan.notification.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EmailTemplateModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "emailType cannot be null")
	@NotBlank(message = "emailType cannot be blank")
	@Size(min = 2, max = 50, message = "emailType must be between 2 and 50 characters")
	private String emailType;

	@NotEmpty(message = "subject cannot be null")
	@NotBlank(message = "subject cannot be blank")
	@Size(min = 2, max = 50, message = "subject must be between 2 and 50 characters")
	private String subject;
	
	@NotEmpty(message = "emailBody cannot be null")
	@NotBlank(message = "emailBody cannot be blank")
	private String emailBody;
	
	@NotEmpty(message = "status cannot be null")
	@NotBlank(message = "status cannot be blank")
	private String status;

	@NotNull(message = "The ID value must not be null")
	@Min(value = 1, message = "ID must be a positive number")
	private Long id;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
