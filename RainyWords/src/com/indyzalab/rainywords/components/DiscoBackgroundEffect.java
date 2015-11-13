package com.indyzalab.rainywords.components;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;

public class DiscoBackgroundEffect extends JLabel{
	Dimension d;
	float alpha = 0.06f;
	int line_amount = 5;
	ArrayList<DiscoLine> lineList = new ArrayList<DiscoLine>();
	
	Color[] colors = {Color.BLUE,Color.GREEN
			,Color.ORANGE,Color.MAGENTA
			,Color.BLACK,Color.CYAN
			,Color.GRAY,Color.PINK,Color.LIGHT_GRAY
			,Color.RED};
	
	
	public DiscoBackgroundEffect(Dimension d) {
		super();
		this.d = d;
		randomGenerate();
		
		
	}


	public DiscoBackgroundEffect(Dimension d,ArrayList<DiscoLine> lineList) {
		super();
		this.d = d;
		this.lineList = lineList;
	}


	public ArrayList<DiscoLine> getLineList() {
		return lineList;
	}


	public void setLineList(ArrayList<DiscoLine> lineList) {
		this.lineList = lineList;
	}

	public void addLineList(DiscoLine line){
		lineList.add(line);
	}
	
	public void randomGenerate(){
		lineList = new ArrayList<DiscoLine>();
		for(int i = 0;i<line_amount; i++){
			addLineList(createDiscoLine());
		}
	}
	
	public DiscoLine createDiscoLine(){
		int upper = 0;
		int left = 1;
		int bottom = 2;
		int right = 3;

		Random diceRoller = new Random();
		// First type
		int type = diceRoller.nextInt(4);
		Position pos1 = getRandomPositionFromType(type);
		// Second type
		int random = diceRoller.nextInt(2) == 0? -1:1;
		int type2 = type+random;
		Position pos2 = getRandomPositionFromType(type2);
		
		
		DiscoLine line = new DiscoLine(pos1.x,pos2.x,pos1.y,pos2.y
				,colors[diceRoller.nextInt(colors.length)]
				,(diceRoller.nextInt(4))*7+4);
		line.max_millisec = diceRoller.nextInt(2)*130+diceRoller.nextInt(7)*40+50;
//		line.max_millisec = 100;
		return line;
	}
	
	public Position getRandomPositionFromType(int type){
		int upper = 0;
		int left = 1;
		int bottom = 2;
		int right = 3;
		Random diceRoller = new Random();
		Position pos;
		int x1 = 0 ,y1 = 0;
		if(type==upper){
			x1 = diceRoller.nextInt(d.width+1);
			y1 = 0;
		}else if(type == left){
			x1 = 0;
			y1 = diceRoller.nextInt(d.height+1);
		}else if(type == bottom){
			x1 = diceRoller.nextInt(d.width+1);
			y1 = d.height;
		}else if(type == right){
			x1 = d.width;
			y1 = diceRoller.nextInt(d.height+1);
		}
		return new Position(x1, y1);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		drawLine(g2);
	}
	
	
	
	public void drawLine(Graphics2D g2){
		checkLine();
		for(DiscoLine line:lineList){
			int type = AlphaComposite.SRC_OVER; 
			AlphaComposite composite = 
			  AlphaComposite.getInstance(type,alpha);
			g2.setComposite(composite);
			g2.setColor(line.color);
			g2.setStroke(new BasicStroke(line.stroke_size));
			g2.draw(new Line2D.Double(line.x1, line.y1, line.x2, line.y2));
			line.addTime(1);
		}
		
	}
	
	public void checkLine(){
		ArrayList<Line> removeList = new ArrayList<Line>();
		for(Line line:lineList){
			if(line.isExceedTime()){
				removeList.add(line);
			}
		}
		removeLine(removeList);
		for(int i = 0;i<removeList.size();i++){
			addLineList(createDiscoLine());
		}
		
	}
	
	public void removeLine(ArrayList<Line> removeList){
		lineList.removeAll(removeList);
	}
}
