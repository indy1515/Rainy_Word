package com.indyzalab.rainywords.components;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JLabel;

public class LineEffect extends JLabel{
	
	
	ArrayList<Line> lineList = new ArrayList<Line>();
	
	
	public LineEffect() {
		super();
		
	}


	public LineEffect(ArrayList<Line> lineList) {
		super();
		this.lineList = lineList;
	}


	public ArrayList<Line> getLineList() {
		return lineList;
	}


	public void setLineList(ArrayList<Line> lineList) {
		this.lineList = lineList;
	}

	public void addLineList(Line line){
		lineList.add(line);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		drawLine(g2);
	}
	
	public void drawLine(Graphics2D g2){
		checkLine();
		for(Line line:lineList){
			g2.setColor(line.color);
			g2.setStroke(new BasicStroke(5));
			g2.draw(new Line2D.Double(line.x1, line.y1, line.x2, line.y2));
			line.addTime(1);
		}
		
	}
	
	public void checkLine(){
		ArrayList<Line> removeList = new ArrayList<Line>();
		for(Line line:lineList){
			if(line.isExceedTime()) removeList.add(line);
		}
		removeLine(removeList);
	}
	
	public void removeLine(ArrayList<Line> removeList){
		lineList.removeAll(removeList);
	}
}
