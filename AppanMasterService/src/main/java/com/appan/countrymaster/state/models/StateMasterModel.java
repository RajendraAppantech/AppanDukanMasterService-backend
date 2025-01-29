package com.appan.countrymaster.state.models;

import java.util.Date;

import com.appan.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class StateMasterModel {

	private Long id;
	private String stateName;
	private String countryName;
	private String fingpayStateId;
	private String giftCardStateId;
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

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getFingpayStateId() {
		return fingpayStateId;
	}

	public void setFingpayStateId(String fingpayStateId) {
		this.fingpayStateId = fingpayStateId;
	}

	public String getGiftCardStateId() {
		return giftCardStateId;
	}

	public void setGiftCardStateId(String giftCardStateId) {
		this.giftCardStateId = giftCardStateId;
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
