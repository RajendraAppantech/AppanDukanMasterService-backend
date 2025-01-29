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
@Table(name = "profile_priority_master", schema = "appan_dukan")
public class ProfilePriorityMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_type", nullable = false)
	private String userType;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "allowed_profile_1")
	private String allowedProfile1;

	@Column(name = "allowed_profile_2")
	private String allowedProfile2;

	@Column(name = "allowed_profile_3")
	private String allowedProfile3;

	@Column(name = "allowed_profile_4")
	private String allowedProfile4;

	@Column(name = "allowed_profile_5")
	private String allowedProfile5;

	@Column(name = "status")
	private String status;

	@Column(name = "created_by")
	private String createdBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "modify_by")
	private String modifyBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "modify_dt")
	private Date modifyDt;

	@Column(name = "auth_by")
	private String authBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "auth_date")
	private Date authDate;

	@Column(name = "auth_status")
	private String authStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAllowedProfile1() {
		return allowedProfile1;
	}

	public void setAllowedProfile1(String allowedProfile1) {
		this.allowedProfile1 = allowedProfile1;
	}

	public String getAllowedProfile2() {
		return allowedProfile2;
	}

	public void setAllowedProfile2(String allowedProfile2) {
		this.allowedProfile2 = allowedProfile2;
	}

	public String getAllowedProfile3() {
		return allowedProfile3;
	}

	public void setAllowedProfile3(String allowedProfile3) {
		this.allowedProfile3 = allowedProfile3;
	}

	public String getAllowedProfile4() {
		return allowedProfile4;
	}

	public void setAllowedProfile4(String allowedProfile4) {
		this.allowedProfile4 = allowedProfile4;
	}

	public String getAllowedProfile5() {
		return allowedProfile5;
	}

	public void setAllowedProfile5(String allowedProfile5) {
		this.allowedProfile5 = allowedProfile5;
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

	public String getAuthBy() {
		return authBy;
	}

	public void setAuthBy(String authBy) {
		this.authBy = authBy;
	}

	public Date getAuthDate() {
		return authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

}
