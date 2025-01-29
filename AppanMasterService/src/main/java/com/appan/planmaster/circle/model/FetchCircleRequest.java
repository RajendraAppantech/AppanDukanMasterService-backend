package com.appan.planmaster.circle.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class FetchCircleRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	private String circleName;
	private String code;
	private String circleCode1;
	private String circleCode2;
	private String circleCode3;
	private String status;
	private String fromDate;
	private String toDate;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCircleCode1() {
		return circleCode1;
	}

	public void setCircleCode1(String circleCode1) {
		this.circleCode1 = circleCode1;
	}

	public String getCircleCode2() {
		return circleCode2;
	}

	public void setCircleCode2(String circleCode2) {
		this.circleCode2 = circleCode2;
	}

	public String getCircleCode3() {
		return circleCode3;
	}

	public void setCircleCode3(String circleCode3) {
		this.circleCode3 = circleCode3;
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
