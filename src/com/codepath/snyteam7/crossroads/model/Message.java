package com.codepath.snyteam7.crossroads.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
	public String getUserId() {
    	return getString("userId");
    }
    
    public String getBody() {
    	return getString("body");
    }
    
    public String getItemId() {
    	return getString("itemId");
    }
    
    public void setUserId(String userId) {
        put("userId", userId);	
    }
    
    public void setBody(String body) {
    	put("body", body);
    }
    
    public void setItemId(String itemId) {
    	put("itemId", itemId);
    }
}
