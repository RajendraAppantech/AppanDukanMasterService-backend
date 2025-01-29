package com.appan.countrymaster.region.models;

public class PageDetails {

	private int pageNo;
	private long totalRecords;
	private long noOfPages;
	private long pageSize;
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	public long getNoOfPages() {
		return noOfPages;
	}
	public void setNoOfPages(long noOfPages) {
		this.noOfPages = noOfPages;
	}
	public long getPageSize() {
		return pageSize;
	}
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
}
