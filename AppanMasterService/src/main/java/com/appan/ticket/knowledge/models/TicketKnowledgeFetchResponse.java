package com.appan.ticket.knowledge.models;

import java.util.List;

import com.appan.countrymaster.region.models.PageDetails;
import com.appan.entity.TicketKnowledge;

public class TicketKnowledgeFetchResponse {

	private boolean status;
	private String message;
	private String respCode;
	private List<TicketKnowledge> data;
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

	public List<TicketKnowledge> getData() {
		return data;
	}

	public void setData(List<TicketKnowledge> data) {
		this.data = data;
	}

	public PageDetails getPageDetails() {
		return pageDetails;
	}

	public void setPageDetails(PageDetails pageDetails) {
		this.pageDetails = pageDetails;
	}
}
