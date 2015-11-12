package com.indyzalab.rainywords.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.Highlighter;

import com.indyzalab.rainywords.gameplay.RainyWordsClient;
import com.indyzalab.rainywords.gameplay.RainyWordsServer;

public class MainPage extends JFrame implements ActionListener{
	static JPanel header;
	static JPanel singPart;
	static JPanel multiPart;
	static JButton sButton;
	static JButton mButton;
	
	public MainPage(String title){
		super(title);
	}
	
	public static void main (String []args){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		// TODO Auto-generated method stub
		MainPage frame = new MainPage("Rainy Words");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(3, 1));
		frame.setPreferredSize(new Dimension(700,500));
		
		
		header = new JPanel();
        //header.setPreferredSize(new Dimension(700,300));
        header.setBackground(new Color (137, 189, 227));
        JLabel title = new JLabel(" Rainy Words ");
        
        singPart = new JPanel(new GridBagLayout());
        singPart.setBackground(new Color (137, 189, 227));
        sButton = new JButton("Single Player");
        sButton.setActionCommand("single");
        sButton.addActionListener(new ActionListener() {
           
        	@Override public void actionPerformed(ActionEvent e) {
        		String command = e.getActionCommand();

                if (command.equals("single")) {
//                    GamePanel gamePanel = new GamePanel();
                    try {
						RainyWordsClient.main(new String[] {"123"});
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
          });
        
        
        multiPart = new JPanel(new GridBagLayout());
        multiPart.setBackground(new Color (137, 189, 227));
        //multiPart = new JPanel(new BorderLayout(0,400));
        mButton = new JButton("Multiplayer");
        //mButton.setPreferredSize(new Dimension (400,100));
        
        header.add(title);
        singPart.add(sButton);
        multiPart.add(mButton);
        
        
        frame.add(header, BorderLayout.PAGE_START);
        frame.add(singPart, BorderLayout.CENTER);
        frame.add(multiPart, BorderLayout.SOUTH );
        
        //frame.add(page);  
        frame.pack();
        frame.setVisible(true);
        
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}