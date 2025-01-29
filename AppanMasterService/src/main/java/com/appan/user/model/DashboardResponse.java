package com.appan.user.model;

import java.util.List;

public class DashboardResponse {

	private boolean status;
	private String message;
	private String respCode;
	private String smsBalance;
	private OverviewModel overview;
	private ProgressCircleModel progressCircle;
	private List<SmsMonthlyData> smsMonthlyData;
	private List<CustomerSummaryModel> customerSummaryModel;

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

	public OverviewModel getOverview() {
		return overview;
	}

	public void setOverview(OverviewModel overview) {
		this.overview = overview;
	}

	public ProgressCircleModel getProgressCircle() {
		return progressCircle;
	}

	public void setProgressCircle(ProgressCircleModel progressCircle) {
		this.progressCircle = progressCircle;
	}

	public List<SmsMonthlyData> getSmsMonthlyData() {
		return smsMonthlyData;
	}

	public void setSmsMonthlyData(List<SmsMonthlyData> smsMonthlyData) {
		this.smsMonthlyData = smsMonthlyData;
	}

	public String getSmsBalance() {
		return smsBalance;
	}

	public void setSmsBalance(String smsBalance) {
		this.smsBalance = smsBalance;
	}

	public List<CustomerSummaryModel> getCustomerSummaryModel() {
		return customerSummaryModel;
	}

	public void setCustomerSummaryModel(List<CustomerSummaryModel> customerSummaryModel) {
		this.customerSummaryModel = customerSummaryModel;
	}
}