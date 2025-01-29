package com.appan.ticket.category.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketCategoryAuthOrBlockRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be Blank")
    @Size(min = 2, max = 50, message = "Username must be between 5 and 50 characters")
	private String username;
	
	@NotNull(message = "ticketCategoryId cannot be null")
    private Long ticketCategoryId;
	
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

	public Long getTicketCategoryId() {
		return ticketCategoryId;
	}

	public void setTicketCategoryId(Long ticketCategoryId) {
		this.ticketCategoryId = ticketCategoryId;
	}
}
