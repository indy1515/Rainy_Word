package com.indyzalab.rainywords.components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JLabel;

public class PointEffect extends JLabel{
	
	ArrayList<Point> pointList = new ArrayList<Point>();
	
	
	public PointEffect() {
		super();
		
	}


	public PointEffect(ArrayList<Point> pointList) {
		super();
		this.pointList = pointList;
	}


	

	public void addPointList(Point point){
		pointList.add(point);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		drawLine(g2);
	}
	
	public void drawLine(Graphics2D g2){
		checkPoint();
		for(Point point:pointList){
			int type = AlphaComposite.SRC_OVER; 
			AlphaComposite composite = 
			  AlphaComposite.getInstance(type, point.getAlpha());
			g2.setComposite(composite);
			g2.setColor(point.color);
			g2.setStroke(new BasicStroke(2));
			g2.drawString(point.getPointString(), point.getCalculatedX(), point.getCalculatedY());
			point.addTime(1);
		}
		
	}
	
	public void checkPoint(){
		ArrayList<Point> removeList = new ArrayList<Point>();
		for(Point point:pointList){
			if(point.isExceedTime()) removeList.add(point);
		}
		removeLine(removeList);
	}
	
	public void removeLine(ArrayList<Point> removeList){
		pointList.removeAll(removeList);
	}
}
