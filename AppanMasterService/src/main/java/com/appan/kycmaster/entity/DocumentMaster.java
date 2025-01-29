package com.appan.kycmaster.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_master", schema = "appan_dukan")
public class DocumentMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "document_name", nullable = false)
	private String documentName;

	@Column(name = "has_expiry", nullable = false)
	private Boolean hasExpiry;

	@Column(name = "is_ekyc", nullable = false)
	private Boolean isEkyc;

	@Column(nullable = false)
	private Integer priority;

	@Column(name = "is_mandatory", nullable = false)
	private Boolean isMandatory;

	@Column(nullable = false)
	private String status;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "modify_by")
	private String modifyBy;

	@Column(name = "modify_dt")
	private Date modifyDt;

	@Column(name = "auth_by")
	private String authBy;

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

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Boolean getHasExpiry() {
		return hasExpiry;
	}

	public void setHasExpiry(Boolean hasExpiry) {
		this.hasExpiry = hasExpiry;
	}

	public Boolean getIsEkyc() {
		return isEkyc;
	}

	public void setIsEkyc(Boolean isEkyc) {
		this.isEkyc = isEkyc;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
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

	// Getters and Setters

}
