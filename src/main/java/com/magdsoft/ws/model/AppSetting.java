package com.magdsoft.ws.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class AppSetting {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String welcomeMsgAr;
	private String welcomeMsgEn;
	private String appMsgAr;
	private String appMsgEn;
	private String conteractMail;
	private String conteractPhone;
	private Integer percentage;
	@CreationTimestamp
	private Date createdAt=new Date();
	@UpdateTimestamp
	private Date updatedAt=new Date();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPercentage() {
		return percentage;
	}
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	public String getWelcomeMsgAr() {
		return welcomeMsgAr;
	}
	
	public void setWelcomeMsgAr(String welcomeMsgAr) {
		this.welcomeMsgAr = welcomeMsgAr;
	}
	public String getWelcomeMsgEn() {
		return welcomeMsgEn;
	}
	public void setWelcomeMsgEn(String welcomeMsgEn) {
		this.welcomeMsgEn = welcomeMsgEn;
	}
	public String getAppMsgAr() {
		return appMsgAr;
	}
	public void setAppMsgAr(String appMsgAr) {
		this.appMsgAr = appMsgAr;
	}
	public String getAppMsgEn() {
		return appMsgEn;
	}
	public void setAppMsgEn(String appMsgEn) {
		this.appMsgEn = appMsgEn;
	}
	public String getConteractMail() {
		return conteractMail;
	}
	public void setConteractMail(String conteractMail) {
		this.conteractMail = conteractMail;
	}
	public String getConteractPhone() {
		return conteractPhone;
	}
	public void setConteractPhone(String conteractPhone) {
		this.conteractPhone = conteractPhone;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
