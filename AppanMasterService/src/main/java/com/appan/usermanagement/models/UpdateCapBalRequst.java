package com.appan.usermanagement.models;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateCapBalRequst {

	@NotEmpty(message = "Username cannot be null or empty")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotEmpty(message = "UserId cannot be null or empty")
	@NotBlank(message = "UserId cannot be blank")
	private String userId;

	@NotNull(message = "Main wallet balance cannot be null")
	@Min(value = 0, message = "Main wallet balance cannot be negative")
	private BigDecimal mainWalletBalance;

	@NotNull(message = "AEPS wallet balance cannot be null")
	@Min(value = 0, message = "AEPS wallet balance cannot be negative")
	private BigDecimal aepsWalletBalance;

	@NotEmpty(message = "Tpin cannot be null or empty")
	@NotBlank(message = "Tpin cannot be blank")
	private String tpin;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}
}
