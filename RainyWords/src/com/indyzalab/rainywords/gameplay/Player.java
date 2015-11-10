package com.indyzalab.rainywords.gameplay;

import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONObject;

public class Player{
	String uniqueID;
	String name;
	boolean isReady = false;
	int points = 0;

	
	public Player(String uniqueID,String name) {
		super();
		this.uniqueID = uniqueID;
		this.name = name;
	}
	
	
	public Player(String uniqueID, String name, int points) {
		super();
		this.uniqueID = uniqueID;
		this.name = name;
		this.points = points;
	}
	

	public Player(String uniqueID, String name, boolean isReady, int points) {
		super();
		this.uniqueID = uniqueID;
		this.name = name;
		this.isReady = isReady;
		this.points = points;
	}


	public Player(JSONObject jsonObject){
		this.uniqueID = jsonObject.get("uniqueID").toString();
		this.name = jsonObject.get("name").toString();
		if(jsonObject.containsKey("isReady")){
			this.isReady = Boolean.parseBoolean(jsonObject.get("isReady").toString());
		}
		this.points = Integer.parseInt(jsonObject.get("points").toString());
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getCurrentJson(){
		JSONObject obj = new JSONObject();
		obj.put("uniqueID", this.uniqueID);
		obj.put("name", this.name);
		obj.put("isReady", this.isReady);
		obj.put("points",this.points);
		return obj;
		
	}


	@Override
	public String toString() {
		return "Player [uniqueID=" + uniqueID + ", name=" + name + ", points="
				+ points + "]";
	}
	
	
	
}
