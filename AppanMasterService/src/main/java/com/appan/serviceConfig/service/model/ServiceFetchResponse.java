package com.appan.serviceConfig.service.model;

import java.util.List;

import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.ServiceConfigServiceMaster;

public class ServiceFetchResponse {

	private boolean status;
	private String message;
	private String respCode;
	private List<ServiceConfigServiceMaster> data;
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

	public List<ServiceConfigServiceMaster> getData() {
		return data;
	}

	public void setData(List<ServiceConfigServiceMaster> data) {
		this.data = data;
	}

	public PageDetails getPageDetails() {
		return pageDetails;
	}

	public void setPageDetails(PageDetails pageDetails) {
		this.pageDetails = pageDetails;
	}

}
