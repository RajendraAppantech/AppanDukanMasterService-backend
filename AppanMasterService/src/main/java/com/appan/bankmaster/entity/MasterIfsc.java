package com.appan.bankmaster.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "master_ifsc", schema = "appan_dukan")
public class MasterIfsc {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "bank_code")
	private String bankCode;

	@Column(name = "ifsc_code")
	private String ifscCode;

	@Column(name = "aeps_iin")
	private String aeps;

	@Column(name = "dmtm")
	private String dmtm;

	@Column(name = "dmte")
	private String dmte;

	@Column(name = "dmtb")
	private String dmtb;

	@Column(name = "is_credit_card")
	private Boolean isCreditCard;

	@Column(name = "priority")
	private Integer priority;

	@Column(name = "is_settlement")
	private Boolean isSettlement;

	@Column(name = "file")
	private String file;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "modify_by")
	private String modifyBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_dt")
	private Date modifyDt;

	@Column(name = "auth_by")
	private String authBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "auth_date")
	private Date authDate;

	@Column(name = "auth_status")
	private String authStatus;

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getAeps() {
		return aeps;
	}

	public void setAeps(String aeps) {
		this.aeps = aeps;
	}

	public String getDmtm() {
		return dmtm;
	}

	public void setDmtm(String dmtm) {
		this.dmtm = dmtm;
	}

	public String getDmte() {
		return dmte;
	}

	public void setDmte(String dmte) {
		this.dmte = dmte;
	}

	public String getDmtb() {
		return dmtb;
	}

	public void setDmtb(String dmtb) {
		this.dmtb = dmtb;
	}

	public Boolean getIsCreditCard() {
		return isCreditCard;
	}

	public void setIsCreditCard(Boolean isCreditCard) {
		this.isCreditCard = isCreditCard;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getIsSettlement() {
		return isSettlement;
	}

	public void setIsSettlement(Boolean isSettlement) {
		this.isSettlement = isSettlement;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
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
