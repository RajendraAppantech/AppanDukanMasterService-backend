package com.appan.planmaster.entity;

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
@Table(name = "circle_series_master", schema = "appan_dukan")
public class CircleSeriesMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "number_prefix")
	private String numberPrefix;

	@Column(name = "circle_name", nullable = false)
	private String circleName;

	@Column(name = "operator_name", nullable = false)
	private String operatorName;

	@Column(name = "is_update")
	private Boolean isUpdate;

	@Column(name = "is_called_for_update")
	private Boolean isCalledForUpdate;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDt;

	@Column(name = "modify_by")
	private String modifyBy;

	@Column(name = "modify_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDt;

	@Column(name = "auth_by")
	private String authBy;

	@Column(name = "auth_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date authDate;

	@Column(name = "auth_status")
	private String authStatus;

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumberPrefix() {
		return numberPrefix;
	}

	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Boolean getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public Boolean getIsCalledForUpdate() {
		return isCalledForUpdate;
	}

	public void setIsCalledForUpdate(Boolean isCalledForUpdate) {
		this.isCalledForUpdate = isCalledForUpdate;
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
