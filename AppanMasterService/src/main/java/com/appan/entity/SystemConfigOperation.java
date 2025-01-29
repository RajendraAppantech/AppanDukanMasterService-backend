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
@Table(name = "system_config_operation_master")
public class SystemConfigOperation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "system_config_operation_id", nullable = false)
	private Long systemConfigOperationId;

	@Column(name = "operation_name", nullable = false, length = 200)
	private String operationName;

	@Column(name = "operation_type", nullable = false, length = 50)
	private String operationType;

	@Column(name = "operation_auth", nullable = false, length = 50)
	private String operationAuth;

	@Column(name = "user_type", nullable = false, length = 255)
	private String userType;

	@Column(name = "channel", nullable = false, length = 50)
	private String channel;

	@Column(name = "description", nullable = false, length = 200)
	private String description;

	@Column(name = "code", nullable = false, length = 255)
	private String code;

	@Column(name = "is_only_ui", nullable = false, length = 10)
	private String isOnlyUi;

	@Column(name = "is_maker_checker", nullable = false, length = 255)
	private String isMakerChecker;

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

	public Long getSystemConfigOperationId() {
		return systemConfigOperationId;
	}

	public void setSystemConfigOperationId(Long systemConfigOperationId) {
		this.systemConfigOperationId = systemConfigOperationId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getOperationAuth() {
		return operationAuth;
	}

	public void setOperationAuth(String operationAuth) {
		this.operationAuth = operationAuth;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsOnlyUi() {
		return isOnlyUi;
	}

	public void setIsOnlyUi(String isOnlyUi) {
		this.isOnlyUi = isOnlyUi;
	}

	public String getIsMakerChecker() {
		return isMakerChecker;
	}

	public void setIsMakerChecker(String isMakerChecker) {
		this.isMakerChecker = isMakerChecker;
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

	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}
}
