package com.appan.ticket.category.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketCategoryModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
	@Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;

	@NotNull(message = "ticketCategoryId cannot be null")
	private Long ticketCategoryId;

	@NotBlank(message = "categoryName cannot be Blank")
	@NotEmpty(message = "categoryName cannot be null")
	@Size(min = 2, max = 50, message = "categoryName must be between 2 and 50 characters")
	private String categoryName;

	@NotBlank(message = "userType cannot be Blank")
	@NotEmpty(message = "userType cannot be null")
	@Size(min = 2, max = 50, message = "userType must be between 2 and 50 characters")
	private String userType;

	@NotBlank(message = "priority cannot be Blank")
	@NotEmpty(message = "priority cannot be null")
	private String priority;

	private String icon;
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTicketCategoryId() {
		return ticketCategoryId;
	}

	public void setTicketCategoryId(Long ticketCategoryId) {
		this.ticketCategoryId = ticketCategoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
