package com.appan.usermanagement.models;

public class KycStatusCount {
	private String kycStatus;
	private Long totalCount;

	// Constructor
	public KycStatusCount(String kycStatus, Long totalCount) {
		this.kycStatus = kycStatus;
		this.totalCount = totalCount;
	}

	// Getters and Setters
	public String getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(String kycStatus) {
		this.kycStatus = kycStatus;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public String toString() {
		return "KycStatusCount{kycStatus='" + kycStatus + '\'' + ", totalCount=" + totalCount + '}';
	}
}
