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
@Table(name = "circles_master", schema = "appan_dukan")
public class CirclesMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "circle_name", nullable = false)
	private String circleName;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "circle_code1")
	private String circleCode1;

	@Column(name = "circle_code2")
	private String circleCode2;

	@Column(name = "circle_code3")
	private String circleCode3;

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

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCircleCode1() {
		return circleCode1;
	}

	public void setCircleCode1(String circleCode1) {
		this.circleCode1 = circleCode1;
	}

	public String getCircleCode2() {
		return circleCode2;
	}

	public void setCircleCode2(String circleCode2) {
		this.circleCode2 = circleCode2;
	}

	public String getCircleCode3() {
		return circleCode3;
	}

	public void setCircleCode3(String circleCode3) {
		this.circleCode3 = circleCode3;
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
