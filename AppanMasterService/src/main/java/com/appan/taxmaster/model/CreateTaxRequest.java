package com.appan.taxmaster.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CreateTaxRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	private String type;
	private String taxTypeName;
	private BigDecimal primaryTax;
	private BigDecimal secondaryTax;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaxTypeName() {
		return taxTypeName;
	}

	public void setTaxTypeName(String taxTypeName) {
		this.taxTypeName = taxTypeName;
	}

	public BigDecimal getPrimaryTax() {
		return primaryTax;
	}

	public void setPrimaryTax(BigDecimal primaryTax) {
		this.primaryTax = primaryTax;
	}

	public BigDecimal getSecondaryTax() {
		return secondaryTax;
	}

	public void setSecondaryTax(BigDecimal secondaryTax) {
		this.secondaryTax = secondaryTax;
	}

}
