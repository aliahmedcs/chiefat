package com.magdsoft.ws.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.ManyToMany;




@Entity
public class Dish {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String name;
	@Column(nullable=false)
	private Double price;
	@Column(columnDefinition="text")
	private String description;
	private String pic;
	@ManyToMany(mappedBy="dish_id")
	private List<User> user_id=new ArrayList<>();
	@ManyToOne(cascade=CascadeType.ALL)
	private Chief chief_id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	
	public List<User> getUser_id() {
		return user_id;
	}
	public void setUser_id(List<User> user_id) {
		this.user_id = user_id;
	}
	public Chief getChief_id() {
		return chief_id;
	}
	public void setChief_id(Chief chief_id) {
		this.chief_id = chief_id;
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
