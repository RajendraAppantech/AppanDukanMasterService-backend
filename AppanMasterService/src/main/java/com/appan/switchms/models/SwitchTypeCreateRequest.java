package com.appan.switchms.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SwitchTypeCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	
	@NotBlank(message = "switchType cannot be Blank")
	@NotEmpty(message = "switchType cannot be null")
    @Size(min = 2, max = 50, message = "switchType must be between 2 and 50 characters")
    private String switchType;

	@NotBlank(message = "priority cannot be Blank")
	@NotEmpty(message = "priority cannot be null")
	private String priority;
	private String code;
	private String status;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSwitchType() {
		return switchType;
	}
	public void setSwitchType(String switchType) {
		this.switchType = switchType;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
