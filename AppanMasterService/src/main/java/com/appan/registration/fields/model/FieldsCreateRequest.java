package com.appan.registration.fields.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class FieldsCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "fieldsName cannot be null")
	@NotBlank(message = "fieldsName cannot be blank")
	@Size(min = 2, max = 50, message = "fieldsName must be between 2 and 50 characters")
	private String fieldsName;

	@NotEmpty(message = "fieldsType cannot be null")
	@NotBlank(message = "fieldsType cannot be blank")
	@Size(min = 2, max = 50, message = "fieldsType must be between 2 and 50 characters")
	private String fieldsType;

	@NotEmpty(message = "stepsName cannot be null")
	@NotBlank(message = "stepsName cannot be blank")
	@Size(min = 2, max = 50, message = "stepsName must be between 2 and 50 characters")
	private String stepsName;

	@NotEmpty(message = "lable cannot be null")
	@NotBlank(message = "lable cannot be blank")
	@Size(min = 2, max = 50, message = "lable must be between 2 and 50 characters")
	private String label;

	@NotEmpty(message = "userType cannot be null")
	@NotBlank(message = "userType cannot be blank")
	private String userType;

	@NotEmpty(message = "entityType cannot be null")
	@NotBlank(message = "entityType cannot be blank")
	private String entityType;

	@NotEmpty(message = "rank cannot be null")
	@NotBlank(message = "rank cannot be blank")
	private String rank;

	private String webRegex;
	private String mobileRegex;
	private Boolean isGroup;
	private Boolean hasCaps;
	private Boolean isMandatory;
	private Boolean isSignup;
	private Boolean isVerified;
	private Boolean isDocumentGroup;
	private String status;
	private String validationMessage;
	private String groupType;
	private String documentGroup;
	private String minLength;
	private String maxLength;

	public String getUsername() {
		return username;
	}

	public String getStepsName() {
		return stepsName;
	}

	public void setStepsName(String stepsName) {
		this.stepsName = stepsName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Boolean getIsSignup() {
		return isSignup;
	}

	public void setIsSignup(Boolean isSignup) {
		this.isSignup = isSignup;
	}

	public String getFieldsName() {
		return fieldsName;
	}

	public void setFieldsName(String fieldsName) {
		this.fieldsName = fieldsName;
	}

	public String getFieldsType() {
		return fieldsType;
	}

	public void setFieldsType(String fieldsType) {
		this.fieldsType = fieldsType;
	}

	public String getWebRegex() {
		return webRegex;
	}

	public void setWebRegex(String webRegex) {
		this.webRegex = webRegex;
	}

	public String getMobileRegex() {
		return mobileRegex;
	}

	public void setMobileRegex(String mobileRegex) {
		this.mobileRegex = mobileRegex;
	}

	public Boolean getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}

	public Boolean getHasCaps() {
		return hasCaps;
	}

	public void setHasCaps(Boolean hasCaps) {
		this.hasCaps = hasCaps;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Boolean getIsDocumentGroup() {
		return isDocumentGroup;
	}

	public void setIsDocumentGroup(Boolean isDocumentGroup) {
		this.isDocumentGroup = isDocumentGroup;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getDocumentGroup() {
		return documentGroup;
	}

	public void setDocumentGroup(String documentGroup) {
		this.documentGroup = documentGroup;
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

}
