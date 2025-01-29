package com.appan.BusinessEntityType.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BusinessEntityTypeFetchRequest {

	@NotBlank(message = "Username cannot be blank")
	@Size(min = 5, max = 10, message = "Username must be between 5 and 10 characters")
	private String username;

	@Size(max = 100, message = "Entity Type Name must be no more than 100 characters")
	private String entityTypeName;

	@Size(max = 10, message = "Code must be no more than 10 characters")
	private String code;

	private Boolean isKyc;

	@Size(max = 20, message = "Status must be no more than 20 characters")
	private String status;

	private String fromDate;
	
	private String toDate;

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

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

}
