package com.appan.usermanagement.models;

import java.util.ArrayList;
import java.util.List;

public class CommonKycResponse {

	private boolean status;
	private String message;
	private String respCode;
	private List<KycStatusCount> data = new ArrayList<>();
	private long totalCount;
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public List<KycStatusCount> getData() {
		return data;
	}
	public void setData(List<KycStatusCount> data) {
		this.data = data;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
}
