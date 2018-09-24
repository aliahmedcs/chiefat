package com.magdsoft.ws.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Theorder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private Integer quantity;
	private Double totalPrice;
	@Column(columnDefinition="enum('مقبول','انتظار','مرفوض')")
	private String isAccepted;
	@ManyToOne(cascade=CascadeType.ALL)
	private Dish dish_id;
	@ManyToOne(cascade=CascadeType.ALL)
	private Chief chief_id;
	@ManyToOne(cascade=CascadeType.ALL)
	private User user_id;
	@CreationTimestamp
	private Date createdAt=new Date();
	@UpdateTimestamp
	private Date updatedAt=new Date();
//	@ManyToOne(cascade=CascadeType.ALL)
//	private AcceptanceStatus acceptanceStatus_id;

	
//	public AcceptanceStatus getAcceptanceStatus_id() {
//		return acceptanceStatus_id;
//	}
//	public void setAcceptanceStatus_id(AcceptanceStatus acceptanceStatus_id) {
//		this.acceptanceStatus_id = acceptanceStatus_id;
//	}
	
	public Integer getId() {
		return id;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public String getIsAccepted() {
		return isAccepted;
	}
	public void setIsAccepted(String isAccepted) {
		this.isAccepted = isAccepted;
	}
	public Dish getDish_id() {
		return dish_id;
	}
	public void setDish_id(Dish dish_id) {
		this.dish_id = dish_id;
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
	public Chief getChief_id() {
		return chief_id;
	}
	public void setChief_id(Chief chief_id) {
		this.chief_id = chief_id;
	}
	
	
}
