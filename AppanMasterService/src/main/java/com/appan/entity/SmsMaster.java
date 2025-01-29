package com.appan.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name = "sms_master")
@DynamicInsert
@DynamicUpdate
@Table(name = "sms_master")
public class SmsMaster implements java.io.Serializable {

	private static final long serialVersionUID = -4689292691085871235L;

	// Primary Key
	private Long smsid;

	// Other Fields
	private String mobileNo;
	private String username;
	private String sms;
	private String otp;
	private String status;
	private Date otpDate;
	private String smsResponse;
	private String smsKey;
	private String smsFrom;
	private String templateId;
	private String entityId;
	private Date responseDate;
	private String sendTxnId;
	private String responseTxnId;

	public SmsMaster() {
	}

	public SmsMaster(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public SmsMaster(String mobileNo, String sms, String otp, String status, Date otpDate, String smsResponse,
			String smsKey, String smsFrom, String templateId, String entityId, Date responseDate, String sendTxnId,
			String responseTxnId) {
		this.mobileNo = mobileNo;
		this.sms = sms;
		this.otp = otp;
		this.status = status;
		this.otpDate = otpDate;
		this.smsResponse = smsResponse;
		this.smsKey = smsKey;
		this.smsFrom = smsFrom;
		this.templateId = templateId;
		this.entityId = entityId;
		this.responseDate = responseDate;
		this.sendTxnId = sendTxnId;
		this.responseTxnId = responseTxnId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "smsid", unique = true, nullable = false)
	public Long getSmsid() {
		return this.smsid;
	}

	public void setSmsid(Long smsid) {
		this.smsid = smsid;
	}

	@Column(name = "mobile_no", nullable = false, length = 13)
	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name = "username", length = 100)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "sms", length = 1024)
	public String getSms() {
		return this.sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	@Column(name = "otp", length = 32)
	public String getOtp() {
		return this.otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Column(name = "status", length = 10)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "otp_date", length = 23)
	public Date getOtpDate() {
		return this.otpDate;
	}

	public void setOtpDate(Date otpDate) {
		this.otpDate = otpDate;
	}

	@Column(name = "sms_response", length = 500)
	public String getSmsResponse() {
		return this.smsResponse;
	}

	public void setSmsResponse(String smsResponse) {
		this.smsResponse = smsResponse;
	}

	@Column(name = "sms_key", length = 20)
	public String getSmsKey() {
		return smsKey;
	}

	public void setSmsKey(String smsKey) {
		this.smsKey = smsKey;
	}

	@Column(name = "sms_from", length = 100)
	public String getSmsFrom() {
		return smsFrom;
	}

	public void setSmsFrom(String smsFrom) {
		this.smsFrom = smsFrom;
	}

	@Column(name = "template_id", length = 50)
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	@Column(name = "entity_id", length = 50)
	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "response_date")
	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	@Column(name = "send_txn_id", length = 50)
	public String getSendTxnId() {
		return sendTxnId;
	}

	public void setSendTxnId(String sendTxnId) {
		this.sendTxnId = sendTxnId;
	}

	@Column(name = "response_txn_id", length = 50)
	public String getResponseTxnId() {
		return responseTxnId;
	}

	public void setResponseTxnId(String responseTxnId) {
		this.responseTxnId = responseTxnId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
