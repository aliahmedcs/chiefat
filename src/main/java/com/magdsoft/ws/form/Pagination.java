package com.magdsoft.ws.form;

public class Pagination {
	
	private String apiToken;
	private Integer userId;
	private Integer page;
	private Integer chiefId;
	
	
	
	public Integer getChiefId() {
		return chiefId;
	}
	public void setChiefId(Integer chiefId) {
		this.chiefId = chiefId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	
	
	
}
