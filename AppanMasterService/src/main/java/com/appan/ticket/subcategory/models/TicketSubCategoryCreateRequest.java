package com.appan.ticket.subcategory.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class TicketSubCategoryCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	
	@NotBlank(message = "categoryName cannot be Blank")
	@NotEmpty(message = "categoryName cannot be null")
    @Size(min = 2, max = 50, message = "categoryName must be between 2 and 50 characters")
    private String subCategoryName;
	
	@NotBlank(message = "category cannot be Blank")
	@NotEmpty(message = "category cannot be null")
    @Size(min = 2, max = 50, message = "category must be between 2 and 50 characters")
    private String category;
	
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

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
