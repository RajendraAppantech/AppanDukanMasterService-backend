package com.appan.apimaster.models;

import java.util.Date;

import com.appan.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class FetchEmailApiMasterModel {

	private Long id;
	private String hostName;
	private String emailAddress;
	private String cc;
	private String bcc;
	private Integer port;
	private Boolean isMailActive;
	private Boolean mailSsl;
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

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Boolean getIsMailActive() {
		return isMailActive;
	}

	public void setIsMailActive(Boolean isMailActive) {
		this.isMailActive = isMailActive;
	}

	public Boolean getMailSsl() {
		return mailSsl;
	}

	public void setMailSsl(Boolean mailSsl) {
		this.mailSsl = mailSsl;
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
