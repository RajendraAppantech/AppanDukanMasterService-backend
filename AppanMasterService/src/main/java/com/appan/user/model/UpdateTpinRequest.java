package com.appan.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateTpinRequest {

	@NotNull(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	private String username;

	@NotNull(message = "old tpin cannot be null")
	@NotBlank(message = "old tpin cannot be Blank")
	private String oldTpin;

	@NotNull(message = "newTpin cannot be null")
	@NotBlank(message = "newTpin cannot be Blank")
	private String newTpin;

	@NotNull(message = "confirmTpin cannot be null")
	@NotBlank(message = "confirmTpin cannot be Blank")
	private String confirmTpin;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldTpin() {
		return oldTpin;
	}

	public void setOldTpin(String oldTpin) {
		this.oldTpin = oldTpin;
	}

	public String getNewTpin() {
		return newTpin;
	}

	public void setNewTpin(String newTpin) {
		this.newTpin = newTpin;
	}

	public String getConfirmTpin() {
		return confirmTpin;
	}

	public void setConfirmTpin(String confirmTpin) {
		this.confirmTpin = confirmTpin;
	}
}