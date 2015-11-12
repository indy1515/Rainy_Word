package com.indyzalab.rainywords.components;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PlayerStatusUI {
	JLabel pointLabel = new JLabel("0", SwingConstants.CENTER);
	JLabel playerLabel = new JLabel("Player", SwingConstants.CENTER);
	
	
	public PlayerStatusUI(JLabel pointLabel, JLabel playerLabel) {
		super();
		this.pointLabel = pointLabel;
		this.playerLabel = playerLabel;
	}
	
	
}
