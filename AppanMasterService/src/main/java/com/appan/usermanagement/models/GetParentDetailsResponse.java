package com.appan.usermanagement.models;

import java.util.List;

public class GetParentDetailsResponse {

	private boolean status;
	private String message;
	private String respCode;
	private List<ParentDetailModel> data;

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

	public List<ParentDetailModel> getData() {
		return data;
	}

	public void setData(List<ParentDetailModel> data) {
		this.data = data;
	}
}
