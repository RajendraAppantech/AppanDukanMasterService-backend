package com.appan.serviceConfig.service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ServiceCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "categoryName cannot be null")
	@NotBlank(message = "categoryName cannot be blank")
	@Size(min = 2, max = 50, message = "categoryName must be between 2 and 50 characters")
	private String categoryName;
	
	@NotEmpty(message = "serviceName cannot be null")
	@NotBlank(message = "serviceName cannot be blank")
	@Size(min = 2, max = 50, message = "serviceName must be between 2 and 50 characters")
	private String serviceName;

	@NotEmpty(message = "serviceCode cannot be null")
	@NotBlank(message = "serviceCode cannot be blank")
	@Size(min = 2, max = 50, message = "serviceCode must be between 2 and 50 characters")
	private String serviceCode;
	
	@NotEmpty(message = "serviceType cannot be null")
	@NotBlank(message = "serviceType cannot be blank")
	@Size(min = 2, max = 50, message = "serviceType must be between 2 and 50 characters")
	private String serviceType;
	
	private String file;
	private Boolean isService;
	private Boolean isBbps;
	private Boolean isBbpsOff;
	private Boolean isOperatorWise;
	
	@NotEmpty(message = "wallet cannot be null")
	@NotBlank(message = "wallet cannot be blank")
	@Size(min = 2, max = 50, message = "wallet must be between 2 and 50 characters")
	private String wallet;
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
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
}
