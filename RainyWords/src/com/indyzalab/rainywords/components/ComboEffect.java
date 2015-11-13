package com.indyzalab.rainywords.components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class ComboEffect extends JLabel{

	int counter = 0;
	Dimension d;
	int pos_x;
	int pos_y;
	int current_font_size = 50;
	float minimum_multiplier = 1.0f;
	float maximum_multiplier = 1.3f;
	float expand_multiplier = 1.0f;
	float incremental_multiplier = 0.05f;
	boolean isAnimated = false;
	boolean isExpand = false;
	
	public ComboEffect(Dimension d) {
		super();
		this.d = d;
		pos_x = d.width;
		pos_y = d.height;
	}
	
	public void incremental(int amount){
		counter += amount;
		isAnimated = true;
		isExpand = true;
	}
	
	public void incremental(){
		incremental(1);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		drawLabel(g2);
	}
	
	public void drawLabel(Graphics2D g2){
		checkPoint();
		int type = AlphaComposite.SRC_OVER; 
		AlphaComposite composite = 
		  AlphaComposite.getInstance(type, 1.0f);
		g2.setComposite(composite);
		g2.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, getGraphicFontSize()));
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2));
		g2.setColor(getColor());
		int width = g2.getFontMetrics().stringWidth(counter+"");
		int height = g2.getFontMetrics().stringWidth(counter+"");
//		System.out.println("Draw x: "+pos_x+" y: "+(pos_y+width));
		pos_x = d.width;
		pos_y = d.height;
		
		while(true){
			if(pos_x+width > d.width){
				pos_x -= 1;
			}else{
				break;
			}
		}
		while(true){
			if(pos_y+height > d.height){
				pos_y -= 1;
			}else{
				break;
			}
		}
		g2.drawString(counter+"x", pos_x-5, pos_y-5);
//		point.addTime(1);
		
	}
	
	public Color getColor(){
		if(counter < 10){
			return Color.ORANGE;
		}else if(counter < 30){
			return Color.RED;
		}else if(counter < 50){
			return new Color(15, 155, 246);
		}
		return Color.ORANGE;
	}
	
	public void checkPoint(){
		if(isAnimated){
			if(expand_multiplier >= maximum_multiplier){
				isExpand = false;
				expand_multiplier = maximum_multiplier;
			}else if(expand_multiplier < minimum_multiplier){
				isAnimated = false;
				expand_multiplier = minimum_multiplier;
			}
			if(isExpand){
				expand_multiplier += incremental_multiplier;
			}else{
				expand_multiplier -= incremental_multiplier;
			}
		}
//		System.out.println("Graphic Font Size: "+(int)(current_font_size*expand_multiplier)+toString());
	}
	
	@Override
	public String toString() {
		return "ComboEffect [counter=" + counter + ", minimum_multiplier=" + minimum_multiplier
				+ ", maximum_multiplier=" + maximum_multiplier + ", expand_multiplier=" + expand_multiplier
				+ ", incremental_multiplier=" + incremental_multiplier + ", isAnimated=" + isAnimated + ", isExpand="
				+ isExpand + "]";
	}

	public int getGraphicFontSize(){
		
		return (int)(current_font_size*expand_multiplier);
	}
	

	
}
