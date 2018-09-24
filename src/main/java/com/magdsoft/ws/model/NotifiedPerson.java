package com.magdsoft.ws.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class NotifiedPerson {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private Boolean isSeen;
	@ManyToOne
	private Chief chief_id;
	@ManyToOne
	private User user_id;
//	@ManyToOne
//	private NotificationType notificationType_id;
	@ManyToOne
	private Notification notification_id;
	@CreationTimestamp
	private Date createdAt=new Date();
	@UpdateTimestamp
	private Date updatedAt=new Date();
	
	
	public Notification getNotification_id() {
		return notification_id;
	}
	public void setNotification_id(Notification notification_id) {
		this.notification_id = notification_id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getIsSeen() {
		return isSeen;
	}
	public void setIsSeen(Boolean isSeen) {
		this.isSeen = isSeen;
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
