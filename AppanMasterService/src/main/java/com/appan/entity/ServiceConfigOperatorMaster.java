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
@Table(name = "service_config_operator_master")
public class ServiceConfigOperatorMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "operator_name", nullable = false, length = 200)
	private String operatorName;

	@Column(name = "service_name", nullable = false, length = 50)
	private String serviceName;

	@Column(name = "allow_type", length = 10)
	private String allowType;

	@Column(name = "amount_plan", length = 100)
	private String amountPlan;

	@Column(name = "api_code", length = 10)
	private String apiCode;

	@Column(name = "file", length = 500)
	private String file;

	@Column(name = "tax_type", length = 50)
	private String taxType;

	@Column(name = "rejected_amount", length = 100)
	private String rejectedAmount;

	@Column(name = "min_lenght", length = 10)
	private String minLength;

	@Column(name = "max_lenght", length = 10)
	private String maxLength;

	@Column(name = "min_comm_per", length = 100)
	private String minCommPer;

	@Column(name = "max_comm_per", length = 100)
	private String maxCommPer;

	@Column(name = "min_comm_val", length = 100)
	private String minCommVal;

	@Column(name = "max_comm_val", length = 100)
	private String maxCommVal;

	@Column(name = "min_charge_per", length = 100)
	private String minChargePer;

	@Column(name = "max_charge_per", length = 100)
	private String maxChargePer;

	@Column(name = "min_charge_val", length = 100)
	private String minChargeVal;

	@Column(name = "max_charge_val", length = 100)
	private String maxChargeVal;

	@Column(name = "state", length = 100)
	private String state;

	@Column(name = "wallet_name", length = 50)
	private String walletName;

	@Column(name = "channels", length = 100)
	private String channels;

	@Column(name = "payment_mode", length = 100)
	private String paymentMode;

	@Column(name = "image", length = 500)
	private String image;

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

	@Column(name = "min_amount", length = 20)
	private BigDecimal minAmount;

	@Column(name = "max_amount", length = 20)
	private BigDecimal maxAmount;

	@Column(name = "is_bbps")
	private Boolean isBbps;

	@Column(name = "is_validate")
	private Boolean isValidate;

	@Column(name = "is_partial")
	private Boolean isPartial;

	@Column(name = "is_random")
	private Boolean isRandom;

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAllowType() {
		return allowType;
	}

	public void setAllowType(String allowType) {
		this.allowType = allowType;
	}

	public String getAmountPlan() {
		return amountPlan;
	}

	public void setAmountPlan(String amountPlan) {
		this.amountPlan = amountPlan;
	}

	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getRejectedAmount() {
		return rejectedAmount;
	}

	public void setRejectedAmount(String rejectedAmount) {
		this.rejectedAmount = rejectedAmount;
	}

	public String getMinLength() {
		return minLength;
	}

	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getMinCommPer() {
		return minCommPer;
	}

	public void setMinCommPer(String minCommPer) {
		this.minCommPer = minCommPer;
	}

	public String getMaxCommPer() {
		return maxCommPer;
	}

	public void setMaxCommPer(String maxCommPer) {
		this.maxCommPer = maxCommPer;
	}

	public String getMinCommVal() {
		return minCommVal;
	}

	public void setMinCommVal(String minCommVal) {
		this.minCommVal = minCommVal;
	}

	public String getMaxCommVal() {
		return maxCommVal;
	}

	public void setMaxCommVal(String maxCommVal) {
		this.maxCommVal = maxCommVal;
	}

	public String getMinChargePer() {
		return minChargePer;
	}

	public void setMinChargePer(String minChargePer) {
		this.minChargePer = minChargePer;
	}

	public String getMaxChargePer() {
		return maxChargePer;
	}

	public void setMaxChargePer(String maxChargePer) {
		this.maxChargePer = maxChargePer;
	}

	public String getMinChargeVal() {
		return minChargeVal;
	}

	public void setMinChargeVal(String minChargeVal) {
		this.minChargeVal = minChargeVal;
	}

	public String getMaxChargeVal() {
		return maxChargeVal;
	}

	public void setMaxChargeVal(String maxChargeVal) {
		this.maxChargeVal = maxChargeVal;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Boolean getIsBbps() {
		return isBbps;
	}

	public void setIsBbps(Boolean isBbps) {
		this.isBbps = isBbps;
	}

	public Boolean getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(Boolean isValidate) {
		this.isValidate = isValidate;
	}

	public Boolean getIsPartial() {
		return isPartial;
	}

	public void setIsPartial(Boolean isPartial) {
		this.isPartial = isPartial;
	}

	public Boolean getIsRandom() {
		return isRandom;
	}

	public void setIsRandom(Boolean isRandom) {
		this.isRandom = isRandom;
	}

}