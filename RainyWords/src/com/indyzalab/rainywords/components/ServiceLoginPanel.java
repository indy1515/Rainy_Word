package com.indyzalab.rainywords.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ServiceLoginPanel extends JPanel{
	private JLabel labelUsername = new JLabel("Enter username: ");
	private JLabel labelPassword = new JLabel("Enter password: ");
	private JTextField textUsername = new JTextField(20);
	private JPasswordField fieldPassword = new JPasswordField(20);
	private JButton buttonLogin = new JButton("Login");
	public ServiceLoginPanel(){
		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		// add components to the panel
		constraints.gridx = 0;
		constraints.gridy = 0;		
		add(labelUsername, constraints);

		constraints.gridx = 1;
		add(textUsername, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;		
		add(labelPassword, constraints);
		
		constraints.gridx = 1;
		add(fieldPassword, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		add(buttonLogin, constraints);
		
		// set border for the panel	
//		setBorder(BorderFactory.createTitledBorder(
//				BorderFactory.createEtchedBorder(), "Login Panel"));
		
	}
	
	
	
}
