package com.magdsoft.ws.form;

public class Follower {

	private String apiToken;
	private Integer chiefId;
	private Integer page;
	private String lang;
	private String videoName;
	private String dishName;
	
	
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public Integer getChiefId() {
		return chiefId;
	}
	public void setChiefId(Integer chiefId) {
		this.chiefId = chiefId;
	}
	
	
	
}
