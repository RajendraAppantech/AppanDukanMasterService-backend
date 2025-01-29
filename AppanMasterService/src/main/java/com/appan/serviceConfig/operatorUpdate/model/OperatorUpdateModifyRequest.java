package com.appan.serviceConfig.operatorUpdate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OperatorUpdateModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	
	@NotNull(message = "The ID value must not be null")
	@Min(value = 1, message = "ID must be a positive number")
	private Long id;

	@NotEmpty(message = "operatorName cannot be null")
	@NotBlank(message = "operatorName cannot be blank")
	@Size(min = 2, max = 50, message = "operatorName must be between 2 and 50 characters")
	private String operatorName;

	@NotEmpty(message = "apiOperatorCode1 cannot be null")
	@NotBlank(message = "apiOperatorCode1 cannot be blank")
	@Size(min = 2, max = 50, message = "apiOperatorCode1 must be between 2 and 50 characters")
	private String apiOperatorCode1;
	private String apiOperatorCode2;
	private String apiOperatorCode3;
	private String apiOperatorCode4;
	private String apiOperatorCode5;
	private String apiOperatorCode6;
	private String apiOperatorCode7;
	private String apiOperatorCode8;
	private String status;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getApiOperatorCode1() {
		return apiOperatorCode1;
	}
	public void setApiOperatorCode1(String apiOperatorCode1) {
		this.apiOperatorCode1 = apiOperatorCode1;
	}
	public String getApiOperatorCode2() {
		return apiOperatorCode2;
	}
	public void setApiOperatorCode2(String apiOperatorCode2) {
		this.apiOperatorCode2 = apiOperatorCode2;
	}
	public String getApiOperatorCode4() {
		return apiOperatorCode4;
	}
	public void setApiOperatorCode4(String apiOperatorCode4) {
		this.apiOperatorCode4 = apiOperatorCode4;
	}
	public String getApiOperatorCode5() {
		return apiOperatorCode5;
	}
	public void setApiOperatorCode5(String apiOperatorCode5) {
		this.apiOperatorCode5 = apiOperatorCode5;
	}
	public String getApiOperatorCode7() {
		return apiOperatorCode7;
	}
	public void setApiOperatorCode7(String apiOperatorCode7) {
		this.apiOperatorCode7 = apiOperatorCode7;
	}
	public String getApiOperatorCode8() {
		return apiOperatorCode8;
	}
	public void setApiOperatorCode8(String apiOperatorCode8) {
		this.apiOperatorCode8 = apiOperatorCode8;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getApiOperatorCode3() {
		return apiOperatorCode3;
	}
	public void setApiOperatorCode3(String apiOperatorCode3) {
		this.apiOperatorCode3 = apiOperatorCode3;
	}
	public String getApiOperatorCode6() {
		return apiOperatorCode6;
	}
	public void setApiOperatorCode6(String apiOperatorCode6) {
		this.apiOperatorCode6 = apiOperatorCode6;
	}
}