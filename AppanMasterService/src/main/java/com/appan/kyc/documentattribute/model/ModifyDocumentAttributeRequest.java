package com.appan.kyc.documentattribute.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ModifyDocumentAttributeRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	private Long id;
	private String attributeName;
	private String documentName;
	private String fieldType;
	private Boolean isEkyc;
	private String ekycCode;
	private String label;
	private Integer maxSize;
	private Integer maxWidth;
	private Integer priority;
	private Integer minWidth;
	private String webRegex;
	private String mobileRegex;
	private Boolean isCameraAllowed;
	private Boolean isUploadAllowed;
	private String status;
	private String supportedFileType;
	private String validationMessage;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Boolean getIsEkyc() {
		return isEkyc;
	}

	public void setIsEkyc(Boolean isEkyc) {
		this.isEkyc = isEkyc;
	}

	public String getEkycCode() {
		return ekycCode;
	}

	public void setEkycCode(String ekycCode) {
		this.ekycCode = ekycCode;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public Integer getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
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

	public Boolean getIsCameraAllowed() {
		return isCameraAllowed;
	}

	public void setIsCameraAllowed(Boolean isCameraAllowed) {
		this.isCameraAllowed = isCameraAllowed;
	}

	public Boolean getIsUploadAllowed() {
		return isUploadAllowed;
	}

	public void setIsUploadAllowed(Boolean isUploadAllowed) {
		this.isUploadAllowed = isUploadAllowed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupportedFileType() {
		return supportedFileType;
	}

	public void setSupportedFileType(String supportedFileType) {
		this.supportedFileType = supportedFileType;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

}
