package com.appan.systemconfiguration.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SystemConfigErrorDefinationModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@NotNull(message = "systemConfigErrorDefinitionId cannot be null")
	private Long systemConfigErrorDefinitionId;

	@NotBlank(message = "errorCode cannot be Blank")
	@NotEmpty(message = "errorCode cannot be null")
	@Size(min = 2, max = 20, message = "errorCode must be between 2 and 20 characters")
	private String errorCode;

	@NotBlank(message = "errorMessage cannot be Blank")
	@NotEmpty(message = "errorMessage cannot be null")
	@Size(min = 2, max = 50, message = "errorMessage must be between 2 and 50 characters")
	private String errorMessage;

	@NotBlank(message = "errorType cannot be Blank")
	@NotEmpty(message = "errorType cannot be null")
	@Size(min = 2, max = 50, message = "errorType must be between 2 and 20 characters")
	private String errorType;

	@NotBlank(message = "errorDesc cannot be Blank")
	@NotEmpty(message = "errorDesc cannot be null")
	@Size(min = 2, max = 50, message = "errorDesc must be between 2 and 20 characters")
	private String errorDesc;

	private String status;

	private String resolution;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public Long getSystemConfigErrorDefinitionId() {
		return systemConfigErrorDefinitionId;
	}

	public void setSystemConfigErrorDefinitionId(Long systemConfigErrorDefinitionId) {
		this.systemConfigErrorDefinitionId = systemConfigErrorDefinitionId;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

}
