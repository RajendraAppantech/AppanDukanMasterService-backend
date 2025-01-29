package com.appan.entity;

import java.util.Date;

public class PasswordHistoryId {

	private String userid;
	private String password_type;
	private String old_passwd;
	private String new_passwd;
	private Date changetime;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword_type() {
		return password_type;
	}
	public void setPassword_type(String password_type) {
		this.password_type = password_type;
	}
	public String getOld_passwd() {
		return old_passwd;
	}
	public void setOld_passwd(String old_passwd) {
		this.old_passwd = old_passwd;
	}
	public String getNew_passwd() {
		return new_passwd;
	}
	public void setNew_passwd(String new_passwd) {
		this.new_passwd = new_passwd;
	}
	public Date getChangetime() {
		return changetime;
	}
	public void setChangetime(Date changetime) {
		this.changetime = changetime;
	}
}
