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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "country_master", schema = "appan_dukan", uniqueConstraints = @UniqueConstraint(columnNames = "country_name"))
public class CountryMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_name", nullable = false, length = 255)
    private String countryName;

    @Column(name = "region_name", length = 255)
    private String regionName;

    @Column(name = "regex", length = 255)
    private String regex;

    @Column(name = "isd_code", length = 50)
    private String isdCode;

    @Column(name = "nationality", length = 255)
    private String nationality;

    @Column(name = "flag", length = 255)
    private String flag;

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
