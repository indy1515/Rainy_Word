package com.indyzalab.rainywords.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

import org.json.simple.JSONObject;

import com.indyzalab.rainywords.gameplay.Constants;

public class Word extends JLabel{

	String name;
	int state = 0;
	int helper_state = 0;
	double pos_x = -1;
	double pos_y = -1;
	Dimension d;
	
	Color destroyColor = Color.BLACK;
	final static int STATE_EXIST = 0;
	final static int STATE_DESTROYING = 1;
	final static int STATE_REMOVED = 2;
	
	final static int STATE_HELPER_EXIST_CREATED = 0;
	final static int STATE_HELPER_EXIST_READY = 1;
	
	public Word(String name,Dimension d) {
		this(name,STATE_EXIST,d);
	}
	public Word(String name, int state,Dimension d) {
		this(name,STATE_EXIST,-1,-1,d);
	}
	public Word(String name, double pos_x, double pos_y,Dimension d) {
		this(name,STATE_EXIST,pos_x,pos_y,d);
	}
	
	public Word(JSONObject jsonObject,Dimension d){
		this(jsonObject.get("name").toString(),Integer.parseInt(jsonObject.get("state").toString()),
				Double.parseDouble(jsonObject.get("x").toString()),Double.parseDouble(jsonObject.get("y").toString()),d);
	}
	
	public Word(String name, int state, double pos_x, double pos_y,Dimension d) {
		super();
		this.name = name;
		this.state = state;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.d = d;
		
	}
	public void setAsExistState(){
		this.state = STATE_EXIST;
	}
	public void setAsDestroyingState(){
		this.state = STATE_DESTROYING;
	}
	
	public void setAsRemovedState(){
		this.state = STATE_REMOVED;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if(getState() == STATE_EXIST){ 
			g2.setColor(Color.BLACK);
//			g2.drawString(this.name,(int)( d.width*pos_x-(3.75*this.name.length())), (int)(d.height*pos_y));
			int width = g.getFontMetrics().stringWidth(this.name);
//			System.out.println("Draw x: "+pos_x+" y: "+(pos_y+width));
			while(true){
				if(pos_x+width > d.width){
					pos_x -= 1;
				}else{
					break;
				}
			}
			g2.drawString(this.name,(int)pos_x,(int)pos_y);
		}
		else if(getState() == STATE_DESTROYING){
			
		}else if(getState() == STATE_REMOVED){
			
		}
		
		
//		Ellipse2D.Double circle = new Ellipse2D.Double(d.width*x-(width/2),d.height*y-(height/2),width, height);
//		g2.setColor(Color.WHITE);
//		g2.fill(circle);
//		g2.draw(circle);
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getCurrentJson(){
		JSONObject obj = new JSONObject();
		obj.put("name", this.name);
		obj.put("state", this.state);
		obj.put("x",this.pos_x);
		obj.put("y", this.pos_y);
		return obj;
		
	}

	
	
	
}
