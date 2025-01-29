package com.appan.switchms.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ModifySwitchOpratorRequest {

	@NotNull(message = "id cannot be null")
	private long id;

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@NotBlank(message = "Username cannot be Blank")
	@NotEmpty(message = "Username cannot be null")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String userName;

	@NotBlank(message = "operatorName cannot be Blank")
	@NotEmpty(message = "operatorName cannot be null")
	@Size(min = 2, max = 100, message = "operatorName must be between 2 and 100 characters")
	private String operatorName;

	@NotNull(message = "apiCount cannot be Blank")
	private int apiCount;

	@NotBlank(message = "apiName cannot be Blank")
	@NotEmpty(message = "apiName cannot be null")
	private String apiName1;
	private String apiName2;
	private String apiName3;
	private String apiName4;
	private String apiName5;
	private String apiName6;
	private String apiName7;
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public int getApiCount() {
		return apiCount;
	}

	public void setApiCount(int apiCount) {
		this.apiCount = apiCount;
	}

	public String getApiName1() {
		return apiName1;
	}

	public void setApiName1(String apiName1) {
		this.apiName1 = apiName1;
	}

	public String getApiName2() {
		return apiName2;
	}

	public void setApiName2(String apiName2) {
		this.apiName2 = apiName2;
	}

	public String getApiName3() {
		return apiName3;
	}

	public void setApiName3(String apiName3) {
		this.apiName3 = apiName3;
	}

	public String getApiName4() {
		return apiName4;
	}

	public void setApiName4(String apiName4) {
		this.apiName4 = apiName4;
	}

	public String getApiName5() {
		return apiName5;
	}

	public void setApiName5(String apiName5) {
		this.apiName5 = apiName5;
	}

	public String getApiName6() {
		return apiName6;
	}

	public void setApiName6(String apiName6) {
		this.apiName6 = apiName6;
	}

	public String getApiName7() {
		return apiName7;
	}

	public void setApiName7(String apiName7) {
		this.apiName7 = apiName7;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}