package com.indyzalab.rainywords.gameplay;

import java.awt.Color;
import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONObject;

public class Player{
	String uniqueID;
	String name;
	boolean isReady = false;
	boolean isReset = false;
	int points = 0;
	Color color = Color.BLACK;
	Color[] colors = {Color.BLUE,Color.GREEN
			,Color.ORANGE,Color.MAGENTA
			,Color.BLACK,Color.CYAN
			,Color.GRAY,Color.PINK};
	
	
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
		if(jsonObject.get("name") == null){
			this.name = null;
		}else{
			this.name = jsonObject.get("name").toString();
		}
		if(jsonObject.containsKey("isReady")){
			this.isReady = Boolean.parseBoolean(jsonObject.get("isReady").toString());
		}
		if(jsonObject.containsKey("isReset")){
			this.isReset = Boolean.parseBoolean(jsonObject.get("isReset").toString());
		}
		this.points = Integer.parseInt(jsonObject.get("points").toString());
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getCurrentJson(){
		JSONObject obj = new JSONObject();
		obj.put("uniqueID", this.uniqueID);
		obj.put("name", this.name);
		obj.put("isReady", this.isReady);
		obj.put("isReset", this.isReset);
		obj.put("points",this.points);
		return obj;
		
	}

	public Color getColor(){
		return colors[Integer.parseInt(uniqueID)%colors.length];
	}

	

	@Override
	public String toString() {
		return "Player [uniqueID=" + uniqueID + ", name=" + name + ", isReady=" + isReady + ", isReset=" + isReset
				+ ", points=" + points + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isReady ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + points;
		result = prime * result
				+ ((uniqueID == null) ? 0 : uniqueID.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (uniqueID == null) {
			if (other.uniqueID != null)
				return false;
		} else if (!uniqueID.equals(other.uniqueID))
			return false;
		return true;
	}
	
	
	
	
}
