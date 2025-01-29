package com.appan.notification.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SendSmsRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	private String smsEmailType;
	private String userMobileType;

	@NotEmpty(message = "apiName cannot be null")
	@NotBlank(message = "apiName cannot be blank")
	private String apiName;

	@NotEmpty(message = "message cannot be null")
	@NotBlank(message = "message cannot be blank")
	private String message;

	@NotEmpty(message = "smsEmailBody cannot be null")
	@NotBlank(message = "smsEmailBody cannot be blank")
	private String smsEmailBody;

	private String userType;
	private String mobileNo;
	private String emailId;
	private String registerUser;
	private String link;
	private String image;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSmsEmailType() {
		return smsEmailType;
	}

	public void setSmsEmailType(String smsEmailType) {
		this.smsEmailType = smsEmailType;
	}

	public String getUserMobileType() {
		return userMobileType;
	}

	public void setUserMobileType(String userMobileType) {
		this.userMobileType = userMobileType;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSmsEmailBody() {
		return smsEmailBody;
	}

	public void setSmsEmailBody(String smsEmailBody) {
		this.smsEmailBody = smsEmailBody;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getRegisterUser() {
		return registerUser;
	}

	public void setRegisterUser(String registerUser) {
		this.registerUser = registerUser;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
