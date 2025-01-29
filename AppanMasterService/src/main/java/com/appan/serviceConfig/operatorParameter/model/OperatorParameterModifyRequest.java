package com.appan.serviceConfig.operatorParameter.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OperatorParameterModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotNull(message = "The ID value must not be null")
	@Min(value = 1, message = "ID must be a positive number")
	private Long id;

	@NotEmpty(message = "parameterName cannot be null")
	@NotBlank(message = "parameterName cannot be blank")
	@Size(min = 2, max = 50, message = "parameterName must be between 2 and 50 characters")
	private String parameterName;

	@NotEmpty(message = "categoryName cannot be null")
	@NotBlank(message = "categoryName cannot be blank")
	@Size(min = 2, max = 50, message = "categoryName must be between 2 and 50 characters")
	private String operatorName;

	private Integer minLenght;

	private Integer maxLenght;

	@NotEmpty(message = "fieldType cannot be null")
	@NotBlank(message = "fieldType cannot be blank")
	private String fieldType;

	private Integer sort;

	private Integer manualSort;

	private String pattern;
	private String apiName;
	private Boolean isActive;
	private Boolean isMandatory;
	private Boolean hasGrouping;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Integer getMinLenght() {
		return minLenght;
	}

	public void setMinLenght(Integer minLenght) {
		this.minLenght = minLenght;
	}

	public Integer getMaxLenght() {
		return maxLenght;
	}

	public void setMaxLenght(Integer maxLenght) {
		this.maxLenght = maxLenght;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getManualSort() {
		return manualSort;
	}

	public void setManualSort(Integer manualSort) {
		this.manualSort = manualSort;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Boolean getHasGrouping() {
		return hasGrouping;
	}

	public void setHasGrouping(Boolean hasGrouping) {
		this.hasGrouping = hasGrouping;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
