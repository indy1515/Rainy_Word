package com.indyzalab.rainywords.components;

import java.awt.Color;

public class DiscoLine extends Line{
	
	public int stroke_size = 2;
	
	public DiscoLine(int x1, int x2, int y1, int y2, Color color) {
		super(x1, x2, y1, y2, color);
		// TODO Auto-generated constructor stub
	}
	
	public DiscoLine(int x1, int x2, int y1, int y2, Color color,int stroke_size) {
		super(x1, x2, y1, y2, color);
		this.stroke_size = stroke_size;
		// TODO Auto-generated constructor stub
	}

	public DiscoLine(int x1, int x2, int y1, int y2, int max_millisec) {
		super(x1, x2, y1, y2, max_millisec);
		// TODO Auto-generated constructor stub
	}

	public DiscoLine(int x1, int x2, int y1, int y2) {
		super(x1, x2, y1, y2);
		// TODO Auto-generated constructor stub
	}

	public DiscoLine(int x1, int y1, Word word, Color color) {
		super(x1, y1, word, color);
		// TODO Auto-generated constructor stub
	}

	public DiscoLine(int x1, int y1, Word word) {
		super(x1, y1, word);
		// TODO Auto-generated constructor stub
	}
	
}
