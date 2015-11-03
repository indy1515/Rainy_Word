package com.indyzalab.rainywords.components;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import java.awt.Label;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextField;
import java.awt.Dimension;
import java.awt.CardLayout;

public class LoginPanelV2 extends JPanel {

	/**
	 * Create the panel.
	 */
	public LoginPanelV2() {
		setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

	}
}
