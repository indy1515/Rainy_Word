package com.indyzalab.rainywords.components;

import java.awt.Color;

public class Line {
	int x1,x2,y1,y2;
	int millisec = 0;
	int max_millisec = 10;
	Color color = Color.BLACK;
	public Line(int x1, int y1, Word word){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = (int)word.pos_x+word.getWidth()/2;
		this.y2 = (int)word.pos_y-word.getHeight()/2;
		this.color = word.destroyColor;
	}
	
	public Line(int x1, int y1, Word word,Color color){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = (int)word.pos_x+word.getWidth()/2;
		this.y2 = (int)word.pos_y-word.getHeight()/2;
	}
	public Line(int x1, int x2, int y1, int y2) {
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}


	public Line(int x1, int x2, int y1, int y2, int max_millisec) {
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.max_millisec = max_millisec;
	}
	
	public void addTime(int time){
		millisec += time;
	}
	
	public boolean isExceedTime(){
		return millisec >= max_millisec;
	}
	
}
