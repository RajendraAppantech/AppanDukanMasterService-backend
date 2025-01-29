package com.appan.countrymaster.region.models;

import java.util.HashMap;
import java.util.Map;

public class CommonResponse {

	private boolean status;
	private String message;
	private String respCode;
	private Map<String, Object> data = new HashMap<String, Object>();
	
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
	
	public Map<String, Object> getData() {
		return this.data;
	}

	public void setData(String name, Object value) {
		this.data.put(name, value);
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String toString() {
	    return "{\"status\":\"" + status + "\", \"message\":\"" + message + "\", \"respCode\":\"" + respCode + "\"}";
	}
}
