package com.appan.ticket.childcategory.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketChildCategoryModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	
	@NotNull(message = "ticketChildCategoryId cannot be null")
    private Long ticketChildCategoryId;
	
	@NotBlank(message = "subCategory cannot be Blank")
	@NotEmpty(message = "subCategory cannot be null")
    @Size(min = 2, max = 50, message = "subCategory must be between 2 and 50 characters")
    private String subCategory;
	
	@NotBlank(message = "childCategoryName cannot be Blank")
	@NotEmpty(message = "childCategoryName cannot be null")
    @Size(min = 2, max = 50, message = "childCategoryName must be between 2 and 50 characters")
    private String childCategoryName;
	
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

	public Long getTicketChildCategoryId() {
		return ticketChildCategoryId;
	}

	public void setTicketChildCategoryId(Long ticketChildCategoryId) {
		this.ticketChildCategoryId = ticketChildCategoryId;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getChildCategoryName() {
		return childCategoryName;
	}

	public void setChildCategoryName(String childCategoryName) {
		this.childCategoryName = childCategoryName;
	}
}
