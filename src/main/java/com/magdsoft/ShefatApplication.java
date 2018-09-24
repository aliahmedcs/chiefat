package com.magdsoft;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.magdsoft.ws.controller.ListenerBean;
import com.magdsoft.ws.form.EmptyObject;
import com.magdsoft.ws.model.Bill;
import com.magdsoft.ws.model.Chief;
import com.magdsoft.ws.model.Theorder;
import com.magdsoft.ws.model.Video;
import com.magdsoft.ws.socket.WebSocketChiefController;
import com.magdsoft.ws.socket.WebSocketUserController;

@Configuration
@EnableWebSocket
@EnableAsync
class WebSocketConfigurer implements
		org.springframework.web.socket.config.annotation.WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketChiefController(), "/chiefSocket");
		registry.addHandler(webSocketUserController(), "/userSocket");
	}
	
	@Bean
	public WebSocketChiefController webSocketChiefController() {
		return new WebSocketChiefController();
	}
	
	@Bean
	public WebSocketUserController webSocketUserController() {
		return new WebSocketUserController();
	}
	

}



@SpringBootApplication
public class ShefatApplication  {

	
	
	public static void main(String[] args) {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		SpringApplication.run(ShefatApplication.class, args);
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//		Date date=new Date();
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		int day = cal.get(Calendar.DAY_OF_MONTH);
//		//if(day==1) {
//		float percentage=0;
//		Query q = entityManager.createQuery("select percentage from AppSetting");
//	    percentage = (float) q.getSingleResult();
//		Long currentDate =Long.valueOf( new Timestamp(date.getTime()).toString());
//	    Long before= (long) (currentDate-76032000.0);
//		Query q1 = entityManager.createQuery("select sum(totalPrice),chiefId from Theorder t where t.chiefId=(select id from Chief) and t.createdAt "
//		 		+ "between"+ before+ " and "+ currentDate);
//		List<EmptyObject> orders = q1.getResultList();
//        for(EmptyObject e:orders) {
//        	Bill bill=null;
//        	Chief chie=entityManager.find(Chief.class, e.getChiefIds());
//        	bill.setChief_id(chie);
//        	bill.setCost((int) (e.getSumCost()*percentage));
//        	bill.setIsPaid(false);
//        	bill.setEndDate(new Date());
//        	bill.setStartDate(new Date(before));
//        	entityManager.persist(bill);
//        	 
//       //  } 
//         }
		
		
	}
}
