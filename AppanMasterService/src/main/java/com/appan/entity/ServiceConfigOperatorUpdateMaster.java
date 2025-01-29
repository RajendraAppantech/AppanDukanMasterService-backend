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
@Table(name = "service_config_operator_update_master")
public class ServiceConfigOperatorUpdateMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "operator_name", nullable = false, length = 200)
	private String operatorName;

	@Column(name = "api_operator_code1", nullable = false, length = 100)
	private String apiOperatorCode1;

	@Column(name = "api_operator_code2", length = 100)
	private String apiOperatorCode2;

	@Column(name = "api_operator_code3", length = 100)
	private String apiOperatorCode3;

	@Column(name = "api_operator_code4", length = 100)
	private String apiOperatorCode4;

	@Column(name = "api_operator_code5", length = 100)
	private String apiOperatorCode5;

	@Column(name = "api_operator_code6", length = 100)
	private String apiOperatorCode6;

	@Column(name = "api_operator_code7", length = 100)
	private String apiOperatorCode7;

	@Column(name = "api_operator_code8", length = 100)
	private String apiOperatorCode8;

	@Column(name = "state", length = 100)
	private String state;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getApiOperatorCode3() {
		return apiOperatorCode3;
	}

	public void setApiOperatorCode3(String apiOperatorCode3) {
		this.apiOperatorCode3 = apiOperatorCode3;
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

	public String getApiOperatorCode6() {
		return apiOperatorCode6;
	}

	public void setApiOperatorCode6(String apiOperatorCode6) {
		this.apiOperatorCode6 = apiOperatorCode6;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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