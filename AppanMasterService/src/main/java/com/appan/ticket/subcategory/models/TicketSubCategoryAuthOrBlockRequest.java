package com.appan.ticket.subcategory.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketSubCategoryAuthOrBlockRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	
	@NotNull(message = "ticketSubCategoryId cannot be null")
    private Long ticketSubCategoryId;
	
	@NotBlank(message = "status cannot be Blank")
	@NotEmpty(message = "status cannot be null")
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

	public Long getTicketSubCategoryId() {
		return ticketSubCategoryId;
	}

	public void setTicketSubCategoryId(Long ticketSubCategoryId) {
		this.ticketSubCategoryId = ticketSubCategoryId;
	}
}
