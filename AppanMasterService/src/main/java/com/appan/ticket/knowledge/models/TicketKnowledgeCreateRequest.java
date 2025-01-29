package com.appan.ticket.knowledge.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class TicketKnowledgeCreateRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	
	@NotBlank(message = "childCategory cannot be Blank")
	@NotEmpty(message = "childCategory cannot be null")
    @Size(min = 2, max = 50, message = "childCategory must be between 2 and 50 characters")
    private String childCategory;
	
	@NotBlank(message = "knowledgeCategoryName cannot be Blank")
	@NotEmpty(message = "knowledgeCategoryName cannot be null")
    @Size(min = 2, max = 50, message = "knowledgeCategoryName must be between 2 and 50 characters")
    private String knowledgeCategoryName;
	
	@NotBlank(message = "description cannot be Blank")
	@NotEmpty(message = "description cannot be null")
    private String description;
	
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

	public String getChildCategory() {
		return childCategory;
	}

	public void setChildCategory(String childCategory) {
		this.childCategory = childCategory;
	}

	public String getKnowledgeCategoryName() {
		return knowledgeCategoryName;
	}

	public void setKnowledgeCategoryName(String knowledgeCategoryName) {
		this.knowledgeCategoryName = knowledgeCategoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
