package com.appan.kyc.kycgroupconfig.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ModifyKycGroupConfigRequest {

	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 10, message = "Username must be between 2 and 10 characters")
	private String username;
	private Long id;
	private String documentName;
	private String documentGroupName;
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

	public String getDocumentGroupName() {
		return documentGroupName;
	}

	public void setDocumentGroupName(String documentGroupName) {
		this.documentGroupName = documentGroupName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
