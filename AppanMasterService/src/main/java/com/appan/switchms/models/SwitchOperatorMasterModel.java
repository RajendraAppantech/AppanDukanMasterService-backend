package com.appan.switchms.models;

import java.util.Date;

import com.appan.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SwitchOperatorMasterModel {

	private Long id;
	private String userName;
	private String operatorName;
	private Integer apiCount;
	private String apiName1;
	private String apiName2;
	private String apiName3;
	private String apiName4;
	private String apiName5;
	private String apiName6;
	private String apiName7;
	private String status;
	private String createdBy;
	@JsonSerialize(using = DateSerializer.class)
	private Date createdDt;
	private String modifyBy;
	@JsonSerialize(using = DateSerializer.class)
	private Date modifyDt;
	private String authBy;
	@JsonSerialize(using = DateSerializer.class)
	private Date authDate;
	private String authStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
