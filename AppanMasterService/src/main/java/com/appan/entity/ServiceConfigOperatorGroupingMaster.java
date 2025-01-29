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
@Table(name = "service_config_operator_grouping_master")
public class ServiceConfigOperatorGroupingMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "group_name", nullable = false, length = 255)
    private String groupName;

    @Column(name = "operator_name", nullable = false, length = 255)
    private String operatorName;

    @Column(name = "parameter_name", nullable = false, length = 255)
    private String parameterName;

    @Column(name = "value", nullable = false, length = 255)
    private String value;

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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