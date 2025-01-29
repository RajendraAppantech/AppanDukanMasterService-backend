package com.appan.kyc.ekyc.model;

import java.util.List;

import com.appan.countrymaster.region.models.PageDetails;

public class FetchEkycResponse {

	private boolean status;
	private String message;
	private String respCode;
	private List<EkycMasterModel> data;
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

	public List<EkycMasterModel> getData() {
		return data;
	}

	public void setData(List<EkycMasterModel> data) {
		this.data = data;
	}

	public PageDetails getPageDetails() {
		return pageDetails;
	}

	public void setPageDetails(PageDetails pageDetails) {
		this.pageDetails = pageDetails;
	}

}
