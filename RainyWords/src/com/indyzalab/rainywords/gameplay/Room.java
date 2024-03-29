package com.indyzalab.rainywords.gameplay;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.indyzalab.rainywords.components.Word;
import com.indyzalab.rainywords.gameplay.RWGame.Handler;

/**
 * Use to collect player an organize in a room
 * @author apple
 *
 */
public class Room {
	public ArrayList<Handler> handlers = new ArrayList<Handler>();
	public String id;
	public int max_player;
	public boolean game_end;
	
	List<Word> words = new ArrayList<Word>();
	ArrayList<Word> pre_words = new ArrayList<Word>();
	
	public Room(String id, int max_player) {
		super();
		this.id = id;
		this.max_player = max_player;
	}
	
	public Room(ArrayList<Handler> handlers, String id, int max_player) {
		super();
		this.handlers = handlers;
		this.id = id;
		this.max_player = max_player;
	}
	
	public void addHandler(Handler handler){
		if(handlers == null) handlers = new ArrayList<Handler>();
		handlers.add(handler);		
	}
	
	public boolean isFull(){
		if(handlers == null) return false;
		if(handlers.size() >= max_player) return true;
		return false;
	}
	
	public boolean isFilledName(){
		if(handlers == null) return false;
		boolean isFilledName = false;
		int i = 0;
		for(Handler handler:handlers){
			if(i==0) {
				isFilledName = (handler.player.name != null);
			}else{
				isFilledName = isFilledName && (handler.player.name != null);
			}
			i++;
		}
		return isFilledName;
	}
	
	public boolean isFullAndPlayerDataReady(){
		return isFull()&&isFilledName();
	}
	
	public void removeAllWords(){
		words = new ArrayList<Word>();
		pre_words = new ArrayList<Word>();
	}

	@Override
	public String toString() {
		String playerString = "";
		for(Handler handler: this.handlers){
			playerString += handler.player+", ";
		}
		
		String ready = "";
		for(Handler handler: this.handlers){
			ready += handler.isReady()+", ";
		}
		return "Room [players=" + playerString +", ready=["+ready+"]" +", id=" + id + ", max_player="
				+ max_player + ", game_end=" + game_end + ", words=" + words.size()
				+ ", pre_words=" + pre_words.size() + "]";
	}
	
	
}
