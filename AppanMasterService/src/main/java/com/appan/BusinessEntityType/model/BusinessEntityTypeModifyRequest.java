package com.appan.BusinessEntityType.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BusinessEntityTypeModifyRequest {

	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 10, message = "Username must be between 5 and 10 characters")
	private String username;

	private Long id;

	private String entityTypeName;

	private String code;

	private Boolean isKyc;

	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
