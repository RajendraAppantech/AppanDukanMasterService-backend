package com.appan.serviceConfig.operatorGrouping.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class OperatorGroupingCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "groupName cannot be null")
	@NotBlank(message = "groupName cannot be blank")
	@Size(min = 2, max = 50, message = "groupName must be between 2 and 50 characters")
	private String groupName;
	
	@NotEmpty(message = "categoryName cannot be null")
	@NotBlank(message = "categoryName cannot be blank")
	@Size(min = 2, max = 50, message = "categoryName must be between 2 and 50 characters")
	private String operatorName;

    @NotEmpty(message = "parameterName cannot be null")
	@NotBlank(message = "parameterName cannot be blank")
	@Size(min = 2, max = 50, message = "parameterName must be between 2 and 50 characters")
    private String parameterName;

    @NotEmpty(message = "value cannot be null")
	@NotBlank(message = "value cannot be blank")
	@Size(min = 2, max = 50, message = "value must be between 2 and 50 characters")
    private String value;

    @NotEmpty(message = "status cannot be null")
	@NotBlank(message = "status cannot be blank")
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}