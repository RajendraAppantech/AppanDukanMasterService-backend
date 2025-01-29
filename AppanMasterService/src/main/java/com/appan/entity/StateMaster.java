package com.appan.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "state_master", schema = "appan_dukan")
public class StateMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "state_name", nullable = false, length = 255)
	private String stateName;

	@Column(name = "country_name", length = 255)
	private String countryName;

	@Column(name = "fingpay_state_id", length = 255)
	private String fingpayStateId;

	@Column(name = "gift_card_state_id", length = 255)
	private String giftCardStateId;

	@Column(name = "status", length = 50)
	private String status;

	@Column(name = "created_by", length = 255)
	private String createdBy;

	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "modify_by", length = 100)
	private String modifyBy;

	@Column(name = "modify_dt")
	private Date modifyDt;

	@Column(name = "auth_by", length = 100)
	private String authBy;

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
