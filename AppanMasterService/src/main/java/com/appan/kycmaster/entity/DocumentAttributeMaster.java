package com.appan.kycmaster.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_attribute_master", schema = "appan_dukan")
public class DocumentAttributeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "attribute_name", nullable = false)
	private String attributeName;

	@Column(name = "document_name", nullable = false)
	private String documentName;

	@Column(name = "field_type", nullable = false)
	private String fieldType;

	@Column(name = "is_ekyc", nullable = false)
	private Boolean isEkyc;

	@Column(name = "ekyc_code")
	private String ekycCode;

	private String label;

	@Column(name = "max_size")
	private Integer maxSize;

	@Column(name = "max_width")
	private Integer maxWidth;

	private Integer priority;

	@Column(name = "min_width")
	private Integer minWidth;

	@Column(name = "web_regex")
	private String webRegex;

	@Column(name = "mobile_regex")
	private String mobileRegex;

	@Column(name = "is_camera_allowed")
	private Boolean isCameraAllowed;

	@Column(name = "is_upload_allowed")
	private Boolean isUploadAllowed;

	@Column(nullable = false)
	private String status;

	@Column(name = "supported_filetype")
	private String supportedFileType;

	@Column(name = "validation_message")
	private String validationMessage;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "modify_by")
	private String modifyBy;

	@Column(name = "modify_dt")
	private Date modifyDt;

	@Column(name = "auth_by")
	private String authBy;

	@Column(name = "auth_date")
	private Date authDate;

	@Column(name = "auth_status")
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
