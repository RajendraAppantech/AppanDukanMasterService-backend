package com.appan.entity;

import java.math.BigDecimal;
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
@Table(name = "wallet_debit_master", schema = "appan_dukan")
public class WalletDebitMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "debit_username", nullable = false, length = 255)
	private String debitUsername;

	@Column(name = "credit_username", nullable = false, length = 255)
	private String creditUsername;

	@Column(name = "amount", nullable = false, precision = 16, scale = 2)
	private BigDecimal amount;

	@Column(name = "user_comment", columnDefinition = "TEXT")
	private String userComment;

	@Column(name = "status", length = 20)
	private String status;

	@Column(name = "remark", length = 100)
	private String remark;

	@Column(name = "update_remark", length = 100)
	private String updateRemark;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "verified_on", columnDefinition = "TIMESTAMP(6)")
	private Date verifiedOn;

	@Column(name = "verified_by", length = 100)
	private String verifiedBy;

	@Column(name = "created_by", length = 255)
	private String createdBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "created_dt", columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP")
	private Date createdDt;

	@Column(name = "modify_by", length = 100)
	private String modifyBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "modify_dt", columnDefinition = "TIMESTAMP(6)")
	private Date modifyDt;

	@Column(name = "auth_by", length = 100)
	private String authBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "auth_date", columnDefinition = "TIMESTAMP")
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

	public String getDebitUsername() {
		return debitUsername;
	}

	public void setDebitUsername(String debitUsername) {
		this.debitUsername = debitUsername;
	}

	public String getCreditUsername() {
		return creditUsername;
	}

	public void setCreditUsername(String creditUsername) {
		this.creditUsername = creditUsername;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdateRemark() {
		return updateRemark;
	}

	public void setUpdateRemark(String updateRemark) {
		this.updateRemark = updateRemark;
	}

	public Date getVerifiedOn() {
		return verifiedOn;
	}

	public void setVerifiedOn(Date verifiedOn) {
		this.verifiedOn = verifiedOn;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
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
