package com.appan.systemconfiguration.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SystemConfigOperationTypeModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@NotNull(message = "systemConfigOperationTypeId cannot be null")
	private Long systemConfigOperationTypeId;

	@NotBlank(message = "operationType cannot be Blank")
	@NotEmpty(message = "operationType cannot be null")
	@Size(min = 2, max = 50, message = "operationType must be between 2 and 50 characters")
	private String operationType;

	@NotBlank(message = "status cannot be Blank")
	@NotEmpty(message = "status cannot be null")
	private String status;

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

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public Long getSystemConfigOperationTypeId() {
		return systemConfigOperationTypeId;
	}

	public void setSystemConfigOperationTypeId(Long systemConfigOperationTypeId) {
		this.systemConfigOperationTypeId = systemConfigOperationTypeId;
	}

}
