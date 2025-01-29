package com.appan.usermanagement.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class VerificationDetailsRequest {

	@NotBlank(message = "ID proof cannot be blank")
	private String idProof;

//	@NotBlank(message = "PAN/Aadhar upload file cannot be blank")
	private String panAadharUploadFile;

	@NotBlank(message = "PAN/Aadhar card number cannot be blank")
	private String panAadharCardNo;

	@NotBlank(message = "PAN/Aadhar name cannot be blank")
	@Size(max = 100, message = "PAN/Aadhar name cannot exceed 100 characters")
	private String panAadharName;

	@NotBlank(message = "Bank verification cannot be blank")
	private String bankVerification;

	@NotBlank(message = "Account holder name cannot be blank")
	@Size(max = 100, message = "Account holder name cannot exceed 100 characters")
	private String accountHolderName;

	@NotBlank(message = "Bank name cannot be blank")
	@Size(max = 100, message = "Bank name cannot exceed 100 characters")
	private String bankName;

	@NotBlank(message = "IFSC code cannot be blank")
	private String ifsc;

	@NotBlank(message = "Account number cannot be blank")
	private String accountNumber;

	private String bankAccountUploadFile;

	@NotBlank(message = "Address proof cannot be blank")
	private String addressProof;

	private String aadharFrontUploadFile;

	private String aadharBackUploadFile;

	@NotBlank(message = "Aadhar number cannot be blank")
	private String aadharNo;

	@NotBlank(message = "Name as per Aadhar cannot be blank")
	@Size(max = 100, message = "Name as per Aadhar cannot exceed 100 characters")
	private String nameAsPerAadhar;

	@NotBlank(message = "paymentMethod cannot be blank")
	@Size(max = 100, message = "paymentMethod cannot exceed 100 characters")
	private String paymentMethod;
	
	@NotBlank(message = "upiId cannot be blank")
	@Size(max = 100, message = "upiId cannot exceed 100 characters")
	private String upiId;
	
	@NotBlank(message = "accountType cannot be blank")
	@Size(max = 100, message = "accountType cannot exceed 100 characters")
	private String accountType;

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
