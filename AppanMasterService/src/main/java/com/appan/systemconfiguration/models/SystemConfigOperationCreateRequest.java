package com.appan.systemconfiguration.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SystemConfigOperationCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	
	@NotBlank(message = "operationName cannot be Blank")
	@NotEmpty(message = "operationName cannot be null")
    @Size(min = 2, max = 100, message = "operationType must be between 2 and 100 characters")
    private String operationName;
	
	@NotBlank(message = "operationType cannot be Blank")
	@NotEmpty(message = "operationType cannot be null")
    @Size(min = 2, max = 100, message = "operationType must be between 2 and 100 characters")
    private String operationType;
	
	@NotBlank(message = "operationAuth cannot be Blank")
	@NotEmpty(message = "operationAuth cannot be null")
    @Size(min = 2, max = 50, message = "operationAuth must be between 2 and 50 characters")
    private String operationAuth;
	
	@NotBlank(message = "userType cannot be Blank")
	@NotEmpty(message = "userType cannot be null")
    @Size(min = 2, max = 50, message = "userType must be between 2 and 50 characters")
    private String userType;
	
	@NotBlank(message = "channel cannot be Blank")
	@NotEmpty(message = "channel cannot be null")
    @Size(min = 2, max = 50, message = "channel must be between 2 and 50 characters")
    private String channel;
	
	@NotBlank(message = "description cannot be Blank")
	@NotEmpty(message = "description cannot be null")
    @Size(min = 2, max = 100, message = "description must be between 2 and 100 characters")
    private String description;
	
	@NotBlank(message = "code cannot be Blank")
	@NotEmpty(message = "code cannot be null")
    @Size(min = 2, max = 100, message = "code must be between 2 and 100 characters")
    private String code;
	
	@NotBlank(message = "isOnlyUi cannot be Blank")
	@NotEmpty(message = "isOnlyUi cannot be null")
    private String isOnlyUi;
	
	@NotBlank(message = "isMakerChecker cannot be Blank")
	@NotEmpty(message = "isMakerChecker cannot be null")
    private String isMakerChecker;
	
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

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationAuth() {
		return operationAuth;
	}

	public void setOperationAuth(String operationAuth) {
		this.operationAuth = operationAuth;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsOnlyUi() {
		return isOnlyUi;
	}

	public void setIsOnlyUi(String isOnlyUi) {
		this.isOnlyUi = isOnlyUi;
	}

	public String getIsMakerChecker() {
		return isMakerChecker;
	}

	public void setIsMakerChecker(String isMakerChecker) {
		this.isMakerChecker = isMakerChecker;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
}
