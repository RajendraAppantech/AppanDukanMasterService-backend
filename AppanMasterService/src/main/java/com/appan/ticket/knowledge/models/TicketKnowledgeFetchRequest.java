package com.appan.ticket.knowledge.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class TicketKnowledgeFetchRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	private String knowledgeCategoryName;
	private String childCategory;
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
	public String getKnowledgeCategoryName() {
		return knowledgeCategoryName;
	}
	public void setKnowledgeCategoryName(String knowledgeCategoryName) {
		this.knowledgeCategoryName = knowledgeCategoryName;
	}
	public String getChildCategory() {
		return childCategory;
	}
	public void setChildCategory(String childCategory) {
		this.childCategory = childCategory;
	}
}
