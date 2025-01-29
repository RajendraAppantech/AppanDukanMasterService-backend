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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "username_format", schema = "appan_dukan")
public class UsernameFormat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "username_format")
	private String usernameFormat;

	@Column(name = "user_type")
	private String userType;

	@Column(name = "prefix")
	private String prefix;

	@Column(name = "suffix")
	private String suffix;

	@Column(name = "status")
	private String status;

	@Column(name = "created_by")
	private String createdBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "created_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDt;

	@Column(name = "modify_by")
	private String modifyBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "modify_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDt;

	@Column(name = "auth_by")
	private String authBy;

	@JsonSerialize(using = DateSerializer.class)
	@Column(name = "auth_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date authDt;

	@Column(name = "auth_status")
	private String authStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsernameFormat() {
		return usernameFormat;
	}

	public void setUsernameFormat(String usernameFormat) {
		this.usernameFormat = usernameFormat;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
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

	public Date getAuthDt() {
		return authDt;
	}

	public void setAuthDt(Date authDt) {
		this.authDt = authDt;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

}
