package com.magdsoft.ws.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	@ManyToOne(cascade=CascadeType.ALL)
	private NotificationType notificationType_id;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Dish dish_id;
	@ManyToOne(cascade=CascadeType.ALL)
	private Theorder order_id;
	@ManyToOne
	private Video video_id;
	@ManyToOne
	private User user_id;
	@ManyToOne
	private Chief chief_id;
	@CreationTimestamp
	private Date createdAt=new Date();
	@UpdateTimestamp
	private Date updatedAt=new Date();
	
	
	
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public NotificationType getNotificationType_id() {
		return notificationType_id;
	}
	public void setNotificationType_id(NotificationType notificationType_id) {
		this.notificationType_id = notificationType_id;
	}
	public Dish getDish_id() {
		return dish_id;
	}
	public void setDish_id(Dish dish_id) {
		this.dish_id = dish_id;
	}
	public Theorder getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Theorder order_id) {
		this.order_id = order_id;
	}
	public Video getVideo_id() {
		return video_id;
	}
	public void setVideo_id(Video video_id) {
		this.video_id = video_id;
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
