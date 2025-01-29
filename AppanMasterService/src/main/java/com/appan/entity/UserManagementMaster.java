package com.appan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "user_management_master")
public class UserManagementMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id", nullable = false, length = 50)
	private String userId;

	@Column(name = "user_type", nullable = false, length = 50)
	private String userType;

	@Column(name = "code", length = 50)
	private String code;

	@Column(name = "entity_type", nullable = false, length = 50)
	private String entityType;

	@Column(name = "full_name", nullable = false, length = 100)
	private String fullName;

	@Column(name = "mobile_number", nullable = false, length = 15)
	private String mobileNumber;

	@Column(name = "email_id", nullable = false, length = 100)
	private String emailId;

	@Column(name = "address", nullable = false, length = 200)
	private String address;

	@Column(name = "profile", nullable = false, length = 100)
	private String profile;

	@Column(name = "parent", nullable = false, length = 100)
	private String parent;

	@Column(name = "pincode", nullable = false, length = 10)
	private String pincode;

	@Column(name = "state", nullable = false, length = 50)
	private String state;

	@Column(name = "city", nullable = false, length = 50)
	private String city;

	@Column(name = "block_po", length = 50)
	private String blockPo;

	@Column(name = "firm_shop_name", length = 100)
	private String firmShopName;

	@Column(name = "business_address", nullable = false, length = 100)
	private String businessAddress;

	@Column(name = "id_proof", nullable = false, length = 50)
	private String idProof;

	@Column(name = "pan_aadhar_upload_file", nullable = false, length = 1000)
	private String panAadharUploadFile;

	@Column(name = "pan_aadhar_card_no", nullable = false, length = 20)
	private String panAadharCardNo;

	@Column(name = "pan_aadhar_name", nullable = false, length = 20)
	private String panAadharName;

	@Column(name = "bank_verification", nullable = false, length = 100)
	private String bankVerification;

	@Column(name = "account_holder_name", nullable = false, length = 100)
	private String accountHolderName;

	@Column(name = "bank_name", nullable = false, length = 100)
	private String bankName;

	@Column(name = "ifsc", nullable = false, length = 11)
	private String ifsc;

	@Column(name = "account_number", nullable = false, length = 30)
	private String accountNumber;

	@Column(name = "bank_account_upload_file", nullable = false, length = 1000)
	private String bankAccountUploadFile;

	@Column(name = "address_proof", nullable = false, length = 100)
	private String addressProof;

	@Column(name = "aadhar_front_upload_file", nullable = false, length = 1000)
	private String aadharFrontUploadFile;

	@Column(name = "aadhar_back_upload_file", nullable = false, length = 1000)
	private String aadharBackUploadFile;

	@Column(name = "aadhar_no", nullable = false, length = 20)
	private String aadharNo;

	@Column(name = "name_as_per_aadhar", length = 50)
	private String nameAsPerAadhar;

	@Column(name = "user_agreement")
	private Boolean userAgreement;

	@Column(name = "main_wallet_balance", precision = 10, scale = 2)
	private BigDecimal mainWalletBalance;

	@Column(name = "aeps_wallet_balance", precision = 10, scale = 2)
	private BigDecimal aepsWalletBalance;

	@Column(name = "wallet_status", length = 50)
	private String walletStatus;

	@Column(name = "kyc_status", length = 50)
	private String kycStatus;

	@Column(name = "channel", length = 50)
	private String channel;

	@Column(name = "created_by", length = 100)
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
	@Column(name = "auth_dt")
	private Date authDt;

	@Column(name = "auth_status", length = 10)
	private String authStatus;

	@Column(name = "status", length = 20)
	private String status;

	@Column(name = "status_modify_remark", length = 500)
	private String statusModifyRemark;

	@Column(name = "is_mobile_verified", nullable = true, columnDefinition = "BOOLEAN DEFAULT false")
	private Boolean isMobileVerified = false;

	@Column(name = "is_email_verified", nullable = true, columnDefinition = "BOOLEAN DEFAULT false")
	private Boolean isEmailVerified = false;

	@Column(name = "parent_id", nullable = true)
	private String parentId;

	@Column(name = "parent_name", length = 100)
	private String parentName;

	@Column(name = "payment_method", length = 50)
	private String paymentMethod;

	@Column(name = "upi_id", length = 100)
	private String upiId;

	@Column(name = "account_type", length = 50)
	private String accountType;

	@Column(name = "remark", length = 500)
	private String remark;

	@Column(name = "pan_document_upload_file", length = 1000)
	private String panDocumentUploadFile;

	@Column(name = "pancard_no", length = 50)
	private String pancardNo;

	@Column(name = "passport_number", length = 50)
	private String passportNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBlockPo() {
		return blockPo;
	}

	public void setBlockPo(String blockPo) {
		this.blockPo = blockPo;
	}

	public String getFirmShopName() {
		return firmShopName;
	}

	public void setFirmShopName(String firmShopName) {
		this.firmShopName = firmShopName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getIdProof() {
		return idProof;
	}

	public void setIdProof(String idProof) {
		this.idProof = idProof;
	}

	public String getPanAadharUploadFile() {
		return panAadharUploadFile;
	}

	public void setPanAadharUploadFile(String panAadharUploadFile) {
		this.panAadharUploadFile = panAadharUploadFile;
	}

	public String getPanAadharCardNo() {
		return panAadharCardNo;
	}

	public void setPanAadharCardNo(String panAadharCardNo) {
		this.panAadharCardNo = panAadharCardNo;
	}

	public String getPanAadharName() {
		return panAadharName;
	}

	public void setPanAadharName(String panAadharName) {
		this.panAadharName = panAadharName;
	}

	public String getBankVerification() {
		return bankVerification;
	}

	public void setBankVerification(String bankVerification) {
		this.bankVerification = bankVerification;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankAccountUploadFile() {
		return bankAccountUploadFile;
	}

	public void setBankAccountUploadFile(String bankAccountUploadFile) {
		this.bankAccountUploadFile = bankAccountUploadFile;
	}

	public String getAddressProof() {
		return addressProof;
	}

	public void setAddressProof(String addressProof) {
		this.addressProof = addressProof;
	}

	public String getAadharFrontUploadFile() {
		return aadharFrontUploadFile;
	}

	public void setAadharFrontUploadFile(String aadharFrontUploadFile) {
		this.aadharFrontUploadFile = aadharFrontUploadFile;
	}

	public String getAadharBackUploadFile() {
		return aadharBackUploadFile;
	}

	public void setAadharBackUploadFile(String aadharBackUploadFile) {
		this.aadharBackUploadFile = aadharBackUploadFile;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getNameAsPerAadhar() {
		return nameAsPerAadhar;
	}

	public void setNameAsPerAadhar(String nameAsPerAadhar) {
		this.nameAsPerAadhar = nameAsPerAadhar;
	}

	public Boolean getUserAgreement() {
		return userAgreement;
	}

	public void setUserAgreement(Boolean userAgreement) {
		this.userAgreement = userAgreement;
	}

	public BigDecimal getMainWalletBalance() {
		return mainWalletBalance;
	}

	public void setMainWalletBalance(BigDecimal mainWalletBalance) {
		this.mainWalletBalance = mainWalletBalance;
	}

	public BigDecimal getAepsWalletBalance() {
		return aepsWalletBalance;
	}

	public void setAepsWalletBalance(BigDecimal aepsWalletBalance) {
		this.aepsWalletBalance = aepsWalletBalance;
	}

	public String getWalletStatus() {
		return walletStatus;
	}

	public void setWalletStatus(String walletStatus) {
		this.walletStatus = walletStatus;
	}

	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
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

	public Date getAuthDt() {
		return authDt;
	}

	public void setAuthDt(Date authDt) {
		this.authDt = authDt;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusModifyRemark() {
		return statusModifyRemark;
	}

	public void setStatusModifyRemark(String statusModifyRemark) {
		this.statusModifyRemark = statusModifyRemark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsMobileVerified() {
		return isMobileVerified;
	}

	public Boolean getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public void setIsMobileVerified(Boolean isMobileVerified) {
		this.isMobileVerified = isMobileVerified;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getUpiId() {
		return upiId;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPanDocumentUploadFile() {
		return panDocumentUploadFile;
	}

	public void setPanDocumentUploadFile(String panDocumentUploadFile) {
		this.panDocumentUploadFile = panDocumentUploadFile;
	}

	public String getPancardNo() {
		return pancardNo;
	}

	public void setPancardNo(String pancardNo) {
		this.pancardNo = pancardNo;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

}
