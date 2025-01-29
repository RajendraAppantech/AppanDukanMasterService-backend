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
@Table(name = "switch_operator_master")
public class SwitchOperatorMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name", nullable = false, length = 255)
	private String userName;

	@Column(name = "operator_name", length = 255)
	private String operatorName;

	@Column(name = "api_count")
	private Integer apiCount;

	@Column(name = "api_name1", length = 255)
	private String apiName1;

	@Column(name = "api_name2", length = 255)
	private String apiName2;

	@Column(name = "api_name3", length = 255)
	private String apiName3;

	@Column(name = "api_name4", length = 255)
	private String apiName4;

	@Column(name = "api_name5", length = 255)
	private String apiName5;

	@Column(name = "api_name6", length = 255)
	private String apiName6;

	@Column(name = "api_name7", length = 255)
	private String apiName7;

	@Column(name = "status", length = 50)
	private String status;

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

	@Column(name = "auth_by", length = 100)
	private String authBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "auth_date")
	private Date authDate;

	@Column(name = "auth_status", length = 20)
	private String authStatus;

	// Getters and Setters

	public String getUserName() {
		return userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getApiCount() {
		return apiCount;
	}

	public void setApiCount(Integer apiCount) {
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
