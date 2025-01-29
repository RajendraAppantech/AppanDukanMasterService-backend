package com.appan.countrymaster.country.models;

import com.appan.ValidRegex;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateCountryRequest {

	@NotEmpty(message = "Username cannot be null")
	@NotBlank(message = "Username cannot be blank")
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;

	@NotBlank(message = "Country name cannot be blank")
	@Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters")
	private String countryName;

	@NotBlank(message = "Region name cannot be blank")
	@Size(min = 2, max = 50, message = "Region name must be between 2 and 50 characters")
	private String regionName;

	@NotBlank(message = "Regex cannot be blank")
	@ValidRegex(message = "Invalid regex format")
	private String regex;

	@NotBlank(message = "ISD code cannot be blank")
	@Pattern(regexp = "^\\+\\d{1,4}$", message = "ISD code must be in the format +[1-4 digits]")
	private String isdCode;

	@NotBlank(message = "Nationality cannot be blank")
	@Size(min = 2, max = 50, message = "Nationality must be between 2 and 50 characters")
	private String nationality;

	// @NotBlank(message = "Flag URL cannot be blank")
	private String flag;

	@NotBlank(message = "Status Code cannot be Blank")
	private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getIsdCode() {
		return isdCode;
	}

	public void setIsdCode(String isdCode) {
		this.isdCode = isdCode;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
