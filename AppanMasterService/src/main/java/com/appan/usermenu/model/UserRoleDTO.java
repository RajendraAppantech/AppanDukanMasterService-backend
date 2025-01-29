package com.appan.usermenu.model;

public class UserRoleDTO {

	private String userRole;
	private String roleName;

	public UserRoleDTO(String userRole, String roleName) {
		this.userRole = userRole;
		this.roleName = roleName;
	}

	// Getters and Setters

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
