package com.appan.kyc.document.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ModifyDocumentRequest {

	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 10, message = "Username must be between 2 and 10 characters")
	private String username;
	private Long id;
	private String documentName;
	private Boolean hasExpiry;
	private Boolean isEkyc;
	private Integer priority;
	private Boolean isMandatory;
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

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Boolean getHasExpiry() {
		return hasExpiry;
	}

	public void setHasExpiry(Boolean hasExpiry) {
		this.hasExpiry = hasExpiry;
	}

	public Boolean getIsEkyc() {
		return isEkyc;
	}

	public void setIsEkyc(Boolean isEkyc) {
		this.isEkyc = isEkyc;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
