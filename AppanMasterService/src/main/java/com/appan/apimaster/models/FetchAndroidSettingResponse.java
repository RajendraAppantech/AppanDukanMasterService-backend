package com.appan.apimaster.models;

import java.util.List;

import com.appan.countrymaster.region.models.PageDetails;

public class FetchAndroidSettingResponse {

	private boolean status;
	private String message;
	private String respCode;
	private List<AndroidSettingMasterModel> data;
	private PageDetails pageDetails;

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

	public List<AndroidSettingMasterModel> getData() {
		return data;
	}

	public void setData(List<AndroidSettingMasterModel> data) {
		this.data = data;
	}

	public PageDetails getPageDetails() {
		return pageDetails;
	}

	public void setPageDetails(PageDetails pageDetails) {
		this.pageDetails = pageDetails;
	}

}
