package com.magdsoft.ws.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Column(columnDefinition="text")
	private String msgContent;
	private Boolean isSeen;
	private Boolean isUserSender;
	@ManyToOne
	private Chief chief_id;
	@ManyToOne
	private User user_id;
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
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public Boolean getIsSeen() {
		return isSeen;
	}
	public void setIsSeen(Boolean isSeen) {
		this.isSeen = isSeen;
	}
	public Boolean getIsUserSender() {
		return isUserSender;
	}
	public void setIsUserSender(Boolean isUserSender) {
		this.isUserSender = isUserSender;
	}
	public Chief getChief_id() {
		return chief_id;
	}
	public void setChief_id(Chief chief_id) {
		this.chief_id = chief_id;
	}
	public User getUser_id() {
		return user_id;
	}
	public void setUser_id(User user_id) {
		this.user_id = user_id;
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
