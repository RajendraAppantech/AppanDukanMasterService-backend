package com.appan.entity;
import java.util.Date;

import com.appan.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alert_type_master")
public class AlertTypeMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alert_name", nullable = false, length = 255)
    private String alertName;

    @Column(name = "parameters_name", length = 255)
    private String parametersName;

    @Column(name = "is_email")
    private Boolean isEmail;

    @Column(name = "is_sms")
    private Boolean isSms;

    @Column(name = "is_whatsapp")
    private Boolean isWhatsapp;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @JsonSerialize(using = DateSerializer.class)
    @Column(name = "created_dt")
    private Date createdDt;

    @Column(name = "modify_by", length = 100)
    private String modifyBy;

    @JsonSerialize(using = DateSerializer.class)
    @Column(name = "modify_dt")
    private Date modifyDt;

    @Column(name = "auth_by", length = 100)
    private String authBy;

    @JsonSerialize(using = DateSerializer.class)
    @Column(name = "auth_date")
    private Date authDate;

    @Column(name = "auth_status", length = 20)
    private String authStatus;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getParametersName() {
        return parametersName;
    }

    public void setParametersName(String parametersName) {
        this.parametersName = parametersName;
    }

    public Boolean getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(Boolean isEmail) {
        this.isEmail = isEmail;
    }

    public Boolean getIsSms() {
        return isSms;
    }

    public void setIsSms(Boolean isSms) {
        this.isSms = isSms;
    }

    public Boolean getIsWhatsapp() {
        return isWhatsapp;
    }

    public void setIsWhatsapp(Boolean isWhatsapp) {
        this.isWhatsapp = isWhatsapp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getAuthDate() {
        return authDate;
    }

    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }
}