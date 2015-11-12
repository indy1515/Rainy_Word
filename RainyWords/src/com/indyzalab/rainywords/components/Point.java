package com.indyzalab.rainywords.components;

import java.awt.Color;

public class Point {
	int point;
	int x1,y1;
	int millisec = 0;
	int max_millisec = 50;
	Color color = Color.BLACK;
	public Point(int point,int x1, int y1, Word word){
		this.point = point;
		this.x1 = (int)word.pos_x+word.getWidth()/2;
		this.y1 = (int)word.pos_y-word.getHeight()/2;
		this.color = word.destroyColor;
	}
	
	public Point(int x1, int y1, Word word,Color color){
		this.point = point;
		this.x1 = (int)word.pos_x+word.getWidth()/2;
		this.y1 = (int)word.pos_y-word.getHeight()/2;
		this.color = color;
	}



	
	public void addTime(int time){
		millisec += time;
	}
	
	public boolean isExceedTime(){
		return millisec >= max_millisec;
	}
	
	public float getAlpha(){
		return (max_millisec*1.0f-millisec*1.0f)/(max_millisec*1.0f);
	}
	
	public String getPointString(){
		String sign = point<0? "-":"+";
		
		return sign+point;
	}
	
	public int getCalculatedX(){
		return x1+millisec;
	}
	
	public int getCalculatedY(){
		return y1;
	}
}
