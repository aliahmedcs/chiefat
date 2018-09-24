package com.magdsoft.ws.socket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service("fcmSender")
public class FCMSender {
	
	private static String serverKey ="AAAAvuLidco:APA91bGcLFrcISz4RrOqvG9-AQ96tOVJdMcL_5FWDO8VVz6DQzyIeOO5M9h_6O45H43z9WYkpkq68OIJE2h-WwH2B5o1chf0Jtca8u-Gl13cdpEJvHp67Hnuq91FcKrCyD0yDWGh164S" ;
	private String priority;
	private String To;
	private String data;
	
	//map that contain "notification" key and map of values include "title","body","icon","sound"
	private Map<String ,Map<String , Object>> notification = new HashMap<>();
	RestTemplate rest=new RestTemplate();

	
    
    public Map<String, Map<String, Object>> getNotification() {
		return notification;
	}

	public void setNotification(Map<String, Map<String, Object>> notification) {
		this.notification = notification;
	}

	    

	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}


	public String getPriority() {
		return priority;
	}
	public void setPriority(String periority) {
		this.priority = periority;
	}
	public String getTo() {
		return To;
	}
	public void setTo(String to) {
		To = to;
	}
    @Async
	public void pushNotification( String to , String data, String title, String body){
    	//Add HEADER
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", serverKey);
		headers.add("Content-Type", "application/json");
		this.setPriority("high");
		this.setData(data);
		this.setTo(to);
		//notification map that contain title , body , sound 
	    Map<String , Object > notification = new HashMap<>();

	    notification.put("title",title);
	    notification.put("body", body);
	    notification.put("sound",1 );
	    //map that contain "notification" key and notification map as a values
	    Map<String ,Map<String , Object>> map = new HashMap<>();
	    map.put("notification",notification);
	    this.setNotification(map);    
		HttpEntity<FCMSender> request = new HttpEntity<FCMSender>(this, headers);
		FireBaseResponse result = new RestTemplate()
				.postForObject("https://fcm.googleapis.com/fcm/send"
						,request
						, FireBaseResponse.class);

	    }

   
	

}
