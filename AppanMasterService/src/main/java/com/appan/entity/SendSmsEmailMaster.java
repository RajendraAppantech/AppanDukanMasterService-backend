package com.appan.entity;

import java.util.Date;

import com.appan.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "send_sms_email_master")
public class SendSmsEmailMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sms_email_type", nullable = false, length = 20)
	private String smsEmailType;

	@Column(name = "user_mobile_type", nullable = false, length = 100)
	private String userMobileType;

	@Column(name = "user_type", length = 100)
	private String userType;

	@Column(name = "mobile_no", length = 13)
	private String mobileNo;

	@Column(name = "email_id", length = 300)
	private String emailId;

	@Column(name = "api_name", nullable = false, length = 100)
	private String apiName;

	@Column(name = "message", nullable = false, length = 800)
	private String message;

	@Column(name = "sms_email_body", nullable = false, length = 1024)
	private String smsEmailBody;

	@Column(name = "status", length = 50)
	private String status;

	@Column(name = "register_user", length = 255)
	private String registerUser;

	@Column(name = "link", length = 100)
	private String link;

	@Column(name = "image", length = 100)
	private String image;

	@Column(name = "created_by", length = 255)
	private String createdBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "modify_by", length = 100)
	private String modifyBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "modify_dt")
	private Date modifyDt;

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDt() {
		return modifyDt;
	}

	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
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