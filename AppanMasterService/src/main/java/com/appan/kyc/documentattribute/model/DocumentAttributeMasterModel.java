package com.appan.kyc.documentattribute.model;

import java.util.Date;

import com.appan.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DocumentAttributeMasterModel {

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

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

}
