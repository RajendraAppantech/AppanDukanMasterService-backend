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

@Entity(name = "registration_steps_master")
@Table(name = "registration_steps_master")
public class RegistrationSteps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_steps_id", nullable = false)
    private Long id;

    @Column(name = "registration_steps_name", nullable = false, length = 255)
    private String stepsName;

    @Column(name = "registration_steps_label", length = 255)
    private String label;

    @Column(name = "user_type", length = 50)
    private String userType;

    @Column(name = "entity_type", length = 50)
    private String entityType;

    @Column(name = "registration_steps_rank", length = 50)
    private String rank;

    @Column(name = "is_mandatory")
    private Boolean isMandatory;

    @Column(name = "is_signup")
    private Boolean isSignup;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		if(isMandatory == null) {
			this.isMandatory = false;
		}else {
			this.isMandatory = isMandatory;
		}
	}

	public Boolean getIsSignup() {
		return isSignup;
	}

	public void setIsSignup(Boolean isSignup) {
		if(isSignup == null) {
			this.isSignup = false;
		}else {
			this.isSignup = isSignup;
		}
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

	public String getStepsName() {
		return stepsName;
	}

	public void setStepsName(String stepsName) {
		this.stepsName = stepsName;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
}