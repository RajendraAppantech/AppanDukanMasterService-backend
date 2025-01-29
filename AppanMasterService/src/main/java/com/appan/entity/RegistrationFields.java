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
@Table(name = "registration_fileds_master")
public class RegistrationFields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "registration_fileds_id", nullable = false)
	private Long id;

	@Column(name = "registration_fileds_name", nullable = false, length = 255)
	private String fieldsName;

	@Column(name = "registration_fileds_type", length = 255)
	private String fieldsType;

	@Column(name = "registration_steps_name", length = 255)
	private String stepsName;

	@Column(name = "user_type", length = 50)
	private String userType;

	@Column(name = "entity_type", length = 50)
	private String entityType;

	@Column(name = "web_regex", length = 50)
	private String webRegex;

	@Column(name = "mobile_regex", length = 50)
	private String mobileRegex;

	@Column(name = "min_length", length = 50)
	private String minLength;

	@Column(name = "max_length", length = 50)
	private String maxLength;

	@Column(name = "registration_fileds_label", length = 50)
	private String label;

	@Column(name = "registration_fileds_rank", length = 50)
	private String rank;

	@Column(name = "is_group")
	private Boolean isGroup;

	@Column(name = "has_caps")
	private Boolean hasCaps;

	@Column(name = "is_mandatory")
	private Boolean isMandatory;

	@Column(name = "is_verified")
	private Boolean isVerified;

	@Column(name = "is_signup")
	private Boolean isSignup;

	@Column(name = "is_document_group")
	private Boolean isDocumentGroup;

	@Column(name = "validation_message")
	private String validationMessage;

	@Column(name = "group_type")
	private String groupType;

	@Column(name = "document_group")
	private String documentGroup;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getStepsName() {
		return stepsName;
	}

	public void setStepsName(String stepsName) {
		this.stepsName = stepsName;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Boolean getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(Boolean isGroup) {
		if (isGroup == null) {
			this.isGroup = false;
		} else {
			this.isGroup = isGroup;
		}
	}

	public Boolean getHasCaps() {
		return hasCaps;
	}

	public void setHasCaps(Boolean hasCaps) {
		if (hasCaps == null) {
			this.hasCaps = false;
		} else {
			this.hasCaps = hasCaps;
		}
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		if (isMandatory == null) {
			this.isMandatory = false;
		} else {
			this.isMandatory = isMandatory;
		}
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		if (isVerified == null) {
			this.isVerified = false;
		} else {
			this.isVerified = isVerified;
		}
	}

	public Boolean getIsSignup() {
		return isSignup;
	}

	public void setIsSignup(Boolean isSignup) {
		if (isSignup == null) {
			this.isSignup = false;
		} else {
			this.isSignup = isSignup;
		}
	}

	public Boolean getIsDocumentGroup() {
		return isDocumentGroup;
	}

	public void setIsDocumentGroup(Boolean isDocumentGroup) {
		if (isDocumentGroup == null) {
			this.isDocumentGroup = false;
		} else {
			this.isDocumentGroup = isDocumentGroup;
		}
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

}