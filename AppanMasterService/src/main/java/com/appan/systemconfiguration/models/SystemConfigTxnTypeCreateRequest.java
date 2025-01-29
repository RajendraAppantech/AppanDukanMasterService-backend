package com.appan.systemconfiguration.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SystemConfigTxnTypeCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	
	@NotBlank(message = "code cannot be Blank")
	@NotEmpty(message = "code cannot be null")
    @Size(min = 2, max = 20, message = "code must be between 2 and 20 characters")
    private String code;
	
	@NotBlank(message = "txnType cannot be Blank")
	@NotEmpty(message = "txnType cannot be null")
    @Size(min = 2, max = 20, message = "txnType must be between 2 and 20 characters")
    private String txnType;
	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
}
