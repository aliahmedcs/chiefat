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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Email;

@Entity
public class Chief {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	private String name;
	private String password;
	@Column(unique=true)
	private String phone;
	private String tempApiToken;
	private String tempPhone;
	private String pic;
	@Column(columnDefinition = "Boolean default false")
	private Boolean isOnline;
	private Integer verificationCode;
    @Email
    @Column(unique=true)
	private String email;
    @ManyToOne
	private City city_id;
	@Column(unique=true)
	private String apiToken;
	@Column(columnDefinition="enum('مقبول','انتظار','مرفوض')")
	private String isAccepted;
	private Boolean isActive;
	
	
	@OneToMany(mappedBy="chief_id")
	private List<Bill> bills=new ArrayList<>();
	@CreationTimestamp
	private Date createdAt=new Date();
	@UpdateTimestamp
	private Date updatedAt=new Date();
	@ManyToMany(cascade=CascadeType.ALL)
	private List<User> user_id=new ArrayList<>();
//	@OneToMany(mappedBy="chief_id")
//	private List<Dish> dishes=new ArrayList<>(); 
	@OneToMany(mappedBy="chief_id")
	private List<Message> messages=new ArrayList<>(); 
	
	
	
	
//	public List<Dish> getDishes() {
//		return dishes;
//	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public List<Bill> getBills() {
		return bills;
	}
	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Boolean getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	public Integer getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(Integer verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public City getCity_id() {
		return city_id;
	}
	public void setCity_id(City city_id) {
		this.city_id = city_id;
	}
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	
	public String getIsAccepted() {
		return isAccepted;
	}
	public void setIsAccepted(String isAccepted) {
		this.isAccepted = isAccepted;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
	public List<User> getUser_id() {
		return user_id;
	}
	public void setUser_id(List<User> user_id) {
		this.user_id = user_id;
	}
	public String getTempApiToken() {
		return tempApiToken;
	}
	public void setTempApiToken(String tempApiToken) {
		this.tempApiToken = tempApiToken;
	}
	public String getTempPhone() {
		return tempPhone;
	}
	public void setTempPhone(String tempPhone) {
		this.tempPhone = tempPhone;
	}
	
	
}
