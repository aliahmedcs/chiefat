package com.magdsoft.ws.socket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Component
public class WebSocketUserController extends TextWebSocketHandler{

	@Autowired
    MessageSender messageSender;
	
    @Autowired
    FCMSender fcmSender;
    
    String apiToken; // apiToken for user that recieve from socket
    Map<String , Object> sessionValues = new HashMap<>(); // mapping apiToken with session value
    
    Set<Object> allSessionValues = new HashSet<>();//all Sessions that created
    
    public void sendNotification(Map<String, Object> obj){  	
    	String data = obj.get("data").toString();
		String title = obj.get("title").toString();
		String body = obj.get("body").toString();
		String to = obj.get("to").toString();
		fcmSender.pushNotification(to, data, title, body);
    }
    
    
    public void removeSession(WebSocketSession session){  	
    	Iterator<Map.Entry<String,Object>> iter = sessionValues.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String,Object> entry = iter.next();
		    if (entry.getValue().equals(session)) {
		        String key = entry.getKey();		        
		        sessionValues.remove(key);
		        
		    }
		}
	
    }
    
    public void addSessions(Map<String, Object> obj, WebSocketSession session){
		apiToken = obj.get("apiToken").toString();
		if(!sessionValues.containsKey(apiToken))  // if this apiToken is already exist
		sessionValues.put(apiToken, session); // put on map that contain apiToken and session
    }
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionEstablished(session);				
		allSessionValues.add(session);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		super.handleTextMessage(session, message);
		
		Map<String , Object> obj = new HashMap<>(); // obj for message
  
		try{
			
			obj = new JacksonJsonParser().parseMap(message.getPayload()); //parsing message
		}
		catch(Exception e){
			
		}
		
		if(obj.containsKey("apiToken")){
			addSessions(obj, session);
		}
		/**
		 * firstly : check if this user open or not 
		 * if this user open send notification instead of message 
		 * 
		 * */
		if(obj.containsKey("new_message") && obj.containsKey("to") && obj.containsKey("data")
				&& obj.containsKey("title") && obj.containsKey("body")
				&& obj.containsKey("chatMessage")){
			
			    String chatMessage = obj.get("message").toString();
				String to = obj.get("to").toString();
				
				if(!sessionValues.containsKey(to)){// check if user session not found
					sendNotification(obj);
				
			     }
			else{
				
				synchronized(session){
					messageSender.sendJsonToWebSocketSessionAsync(session, chatMessage);
			     }	
				
			}
		}
		
		if(obj.containsKey("new_notification") 
				&& obj.containsKey("to") && obj.containsKey("data")
				&& obj.containsKey("title") && obj.containsKey("body")){
			
		    	sendNotification(obj);
			
		}
	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionClosed(session, status);
		
		removeSession(session);
	}
	
}
