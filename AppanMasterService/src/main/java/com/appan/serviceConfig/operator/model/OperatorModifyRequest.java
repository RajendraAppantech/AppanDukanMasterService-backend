package com.appan.serviceConfig.operator.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OperatorModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "serviceName cannot be null")
	@NotBlank(message = "serviceName cannot be blank")
	@Size(min = 2, max = 50, message = "serviceName must be between 2 and 50 characters")
	private String serviceName;

	@NotEmpty(message = "categoryName cannot be null")
	@NotBlank(message = "categoryName cannot be blank")
	@Size(min = 2, max = 50, message = "categoryName must be between 2 and 50 characters")
	private String operatorName;

	private String allowType;
	private String amountPlan;

	@NotEmpty(message = "apiCode cannot be null")
	@NotBlank(message = "apiCode cannot be blank")
	@Size(min = 2, max = 50, message = "apiCode must be between 2 and 50 characters")
	private String apiCode;

	@NotEmpty(message = "taxType cannot be null")
	@NotBlank(message = "taxType cannot be blank")
	@Size(min = 2, max = 50, message = "taxType must be between 2 and 50 characters")
	private String taxType;

	@NotNull(message = "The ID value must not be null")
	@Min(value = 1, message = "ID must be a positive number")
	private Long id;

	private String file;
	private String rejectedAmount;
	private String minLength;
	private String maxLength;
	private String minCommPer;
	private String maxCommPer;
	private String minCommVal;
	private String maxCommVal;
	private String minChargePer;
	private String maxChargePer;
	private String minChargeVal;
	private String maxChargeVal;
	private String state;
	private String walletName;
	private String channels;
	private String paymentMode;
	private String image;
	private String status;
	private BigDecimal minAmount;
	private BigDecimal maxAmount;
	private Boolean isBbps;
	private Boolean isValidate;
	private Boolean isPartial;
	private Boolean isRandom;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
