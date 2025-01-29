package com.appan.serviceConfig.category.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryModifyRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotNull(message = "The ID value must not be null")
	@Min(value = 1, message = "ID must be a positive number")
	private Long id;
	
	@NotEmpty(message = "categoryName cannot be null")
	@NotBlank(message = "categoryName cannot be blank")
	@Size(min = 2, max = 50, message = "categoryName must be between 2 and 50 characters")
	private String categoryName;

	@NotEmpty(message = "categoryCode cannot be null")
	@NotBlank(message = "categoryCode cannot be blank")
	@Size(min = 2, max = 50, message = "categoryCode must be between 2 and 50 characters")
	private String categoryCode;
	private String image;
	private Boolean isTpin;
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getIsTpin() {
		return isTpin;
	}

	public void setIsTpin(Boolean isTpin) {
		this.isTpin = isTpin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
