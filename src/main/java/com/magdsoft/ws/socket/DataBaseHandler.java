package com.magdsoft.ws.socket;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.magdsoft.ws.model.User;


@Service
public class DataBaseHandler {

	
	@Autowired
	public EntityManager entityManager;
	
	@Transactional
	public int getUserId(String apiToken) {
		Query q=entityManager.createQuery("from User where apiToken=:api");
		q.setParameter("api", apiToken);
		User user1=(User) q.getSingleResult();
		User u = entityManager.find(User.class, user1.getId());
		return u.getId();
	}
}
