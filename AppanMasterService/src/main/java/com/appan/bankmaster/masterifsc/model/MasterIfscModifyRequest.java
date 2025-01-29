package com.appan.bankmaster.masterifsc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MasterIfscModifyRequest {

	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 10, message = "Username must be between 5 and 10 characters")
	private String username;

	private Long id;
	private String bankName;
	private String bankCode;
	private String ifscCode;
	private String aeps;
	private String dmtm;
	private String dmte;
	private String dmtb;
	private Boolean isCreditCard;
	private Integer priority;
	private Boolean isSettlement;
	private String file;

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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getAeps() {
		return aeps;
	}

	public void setAeps(String aeps) {
		this.aeps = aeps;
	}

	public String getDmtm() {
		return dmtm;
	}

	public void setDmtm(String dmtm) {
		this.dmtm = dmtm;
	}

	public String getDmte() {
		return dmte;
	}

	public void setDmte(String dmte) {
		this.dmte = dmte;
	}

	public String getDmtb() {
		return dmtb;
	}

	public void setDmtb(String dmtb) {
		this.dmtb = dmtb;
	}

	public Boolean getIsCreditCard() {
		return isCreditCard;
	}

	public void setIsCreditCard(Boolean isCreditCard) {
		this.isCreditCard = isCreditCard;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getIsSettlement() {
		return isSettlement;
	}

	public void setIsSettlement(Boolean isSettlement) {
		this.isSettlement = isSettlement;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
