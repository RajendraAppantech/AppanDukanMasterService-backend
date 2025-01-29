package com.appan.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "user_master")
//@EntityListeners(AuditingEntityListener.class)
public class UserMaster {

	@Id
	@Column(name = "user_id", length = 50, nullable = false)
	private String userId;

	@Column(name = "name", length = 200)
	private String name;

	@Column(name = "mobile_no", length = 13)
	private String mobileNo;

	@Column(name = "email_id", length = 30)
	private String emailId;

	@Column(name = "user_role", length = 30)
	private String userRole;

	@Column(name = "user_profile", length = 30)
	private String userProfile;

	@Column(name = "status", length = 10)
	private String status;

	@Column(name = "user_code")
	private String userCode;

	@Column(name = "passwd", length = 64)
	private String passwd;

	@Column(name = "passwd_exp")
	private Date passwdExp;

	@Column(name = "last_login_dt")
	private Date lastLoginDt;

	@Column(name = "login_attempt")
	private Integer loginAttempt;

	@Column(name = "lock_time")
	private Date lockTime;

	@CreatedBy
	@Column(name = "created_by", length = 100)
	private String createdBy;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_dt")
	private Date createdDt;

	@LastModifiedBy
	@Column(name = "modify_by", length = 100)
	private String modifyBy;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_dt")
	private Date modifyDt;

	@Column(name = "auth_by", length = 100)
	private String authBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "auth_dt")
	private Date authDt;

	@Column(name = "auth_status", length = 10)
	private String authStatus;

	@Column(name = "logout_status", length = 10)
	private String logoutStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_logout_date")
	private Date lastLogoutDate;

	@Column(name = "user_menu", length = 100)
	private String userMenu;

	@Column(name = "session_id")
	private String sessionId;

	@Column(name = "tpin", length = 64)
	private String tpin;

	@Column(name = "tpin_exp")
	private Date tpinExp;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Date getPasswdExp() {
		return passwdExp;
	}

	public void setPasswdExp(Date passwdExp) {
		this.passwdExp = passwdExp;
	}

	public Date getLastLoginDt() {
		return lastLoginDt;
	}

	public void setLastLoginDt(Date lastLoginDt) {
		this.lastLoginDt = lastLoginDt;
	}

	public Integer getLoginAttempt() {
		return loginAttempt;
	}

	public void setLoginAttempt(Integer loginAttempt) {
		this.loginAttempt = loginAttempt;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDt() {
		return modifyDt;
	}

	public void setModifyDt(Date modifyDt) {
		this.modifyDt = modifyDt;
	}

	public String getAuthBy() {
		return authBy;
	}

	public void setAuthBy(String authBy) {
		this.authBy = authBy;
	}

	public Date getAuthDt() {
		return authDt;
	}

	public void setAuthDt(Date authDt) {
		this.authDt = authDt;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getLogoutStatus() {
		return logoutStatus;
	}

	public void setLogoutStatus(String logoutStatus) {
		this.logoutStatus = logoutStatus;
	}

	public Date getLastLogoutDate() {
		return lastLogoutDate;
	}

	public void setLastLogoutDate(Date lastLogoutDate) {
		this.lastLogoutDate = lastLogoutDate;
	}

	public String getUserMenu() {
		return userMenu;
	}

	public void setUserMenu(String userMenu) {
		this.userMenu = userMenu;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public Date getTpinExp() {
		return tpinExp;
	}

	public void setTpinExp(Date tpinExp) {
		this.tpinExp = tpinExp;
	}

	@Override
	public String toString() {
		return "UserMaster [userId=" + userId + ", name=" + name + ", mobileNo=" + mobileNo + ", emailId=" + emailId
				+ ", userRole=" + userRole + ", userProfile=" + userProfile + ", status=" + status + ", userCode="
				+ userCode + ", passwd=" + passwd + ", passwdExp=" + passwdExp + ", lastLoginDt=" + lastLoginDt
				+ ", loginAttempt=" + loginAttempt + ", lockTime=" + lockTime + ", createdBy=" + createdBy
				+ ", createdDt=" + createdDt + ", modifyBy=" + modifyBy + ", modifyDt=" + modifyDt + ", authBy="
				+ authBy + ", authDt=" + authDt + ", authStatus=" + authStatus + ", logoutStatus=" + logoutStatus
				+ ", lastLogoutDate=" + lastLogoutDate + ", userMenu=" + userMenu + "]";
	}

}