package com.appan.entity;
import java.math.BigDecimal;
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
@Table(name = "wallet_bulk_debit_credit")
public class WalletBulkDebitCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_name", nullable = false, length = 255)
    private String batchName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "total_txn")
    private Long totalTxn;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "success_txn")
    private Long successTxn;

    @Column(name = "fail_txn")
    private Long failTxn;

    @Column(name = "pending_txn")
    private Long pendingTxn;

    @Column(name = "batch_status", nullable = false, length = 50)
    private String batchStatus;

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

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getTotalTxn() {
        return totalTxn;
    }

    public void setTotalTxn(Long totalTxn) {
        this.totalTxn = totalTxn;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getSuccessTxn() {
        return successTxn;
    }

    public void setSuccessTxn(Long successTxn) {
        this.successTxn = successTxn;
    }

    public Long getFailTxn() {
        return failTxn;
    }

    public void setFailTxn(Long failTxn) {
        this.failTxn = failTxn;
    }

    public Long getPendingTxn() {
        return pendingTxn;
    }

    public void setPendingTxn(Long pendingTxn) {
        this.pendingTxn = pendingTxn;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
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
}