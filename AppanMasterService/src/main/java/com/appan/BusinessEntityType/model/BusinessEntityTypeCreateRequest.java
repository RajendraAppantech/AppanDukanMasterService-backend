package com.appan.BusinessEntityType.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BusinessEntityTypeCreateRequest {

	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 10, message = "Username must be between 2 and 10 characters")
	private String username;

	@NotBlank(message = "Entity Type Name cannot be blank")
	private String entityTypeName;

	@NotBlank(message = "Code cannot be blank")
	@Size(min = 2, max = 10, message = "Code must be between 2 and 10 characters")
	private String code;

	@NotNull(message = "KYC status must be provided")
	private Boolean isKyc;

	@NotBlank(message = "Status cannot be blank")
	private String status;

	// Getters and Setters

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEntityTypeName() {
		return entityTypeName;
	}

	public void setEntityTypeName(String entityTypeName) {
		this.entityTypeName = entityTypeName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getIsKyc() {
		return isKyc;
	}

	public void setIsKyc(Boolean isKyc) {
		this.isKyc = isKyc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
