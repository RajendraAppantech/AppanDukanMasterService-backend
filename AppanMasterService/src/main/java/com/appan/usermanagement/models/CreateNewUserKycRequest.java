package com.appan.usermanagement.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateNewUserKycRequest {

	@NotBlank(message = "UserId name cannot be blank")
	@Size(max = 100, message = "UserId cannot exceed 100 characters")
	private String userId;

	private String idProof;
	private String panDocumentUploadFile;
	private String pancardNo;
	private String passportNumber;
	private String bankVerification;
	private String accountHolderName;
	private String bankName;
	private String ifsc;
	private String accountNumber;
	private String bankAccountUploadFile;
	private String addressProof;
	private String aadharFrontUploadFile;
	private String aadharBackUploadFile;
	private String aadharNo;
	private String paymentMethod;
	private String upiId;
	private String accountType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIdProof() {
		return idProof;
	}

	public void setIdProof(String idProof) {
		this.idProof = idProof;
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

}
