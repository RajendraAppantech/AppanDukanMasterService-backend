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

@Entity
@Table(name = "service_config_service_master")
public class ServiceConfigServiceMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "service_name", nullable = false, length = 50)
	private String serviceName;

	@Column(name = "service_type", nullable = false, length = 50)
	private String serviceType;

	@Column(name = "service_code", nullable = false, length = 50)
	private String serviceCode;

	@Column(name = "category_name", nullable = false, length = 50)
	private String categoryName;

	@Column(name = "is_service")
	private Boolean isService;

	@Column(name = "is_bbps")
	private Boolean isBbps;

	@Column(name = "is_bbps_off")
	private Boolean isBbpsOff;

	@Column(name = "is_operator_wise")
	private Boolean isOperatorWise;

	@Column(name = "file", length = 500)
	private String file;

	@Column(name = "wallet", length = 50)
	private String wallet;

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

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Boolean getIsService() {
		return isService;
	}

	public void setIsService(Boolean isService) {
		this.isService = isService;
	}

	public Boolean getIsBbps() {
		return isBbps;
	}

	public void setIsBbps(Boolean isBbps) {
		this.isBbps = isBbps;
	}

	public Boolean getIsBbpsOff() {
		return isBbpsOff;
	}

	public void setIsBbpsOff(Boolean isBbpsOff) {
		this.isBbpsOff = isBbpsOff;
	}

	public Boolean getIsOperatorWise() {
		return isOperatorWise;
	}

	public void setIsOperatorWise(Boolean isOperatorWise) {
		this.isOperatorWise = isOperatorWise;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
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

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
}