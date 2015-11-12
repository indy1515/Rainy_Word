package com.indyzalab.rainywords.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.indyzalab.rainywords.gameplay.RainyWordsClient;

public class MainPage extends JFrame implements ActionListener{
	static JPanel header;
	static JPanel choicePart;
	static JPanel singPart;
	static JPanel hostPart;
	static JPanel joinPart;
	static JPanel jservePart;
	static JButton sButton;
	static JButton hButton;
	static JButton jButton;
	static JButton jsButton;

	
	
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
		frame.setLayout(new GridLayout(2, 1));
		frame.setPreferredSize(new Dimension(700,500));
		
		
		header = new JPanel();
		header.setLayout(new GridBagLayout());
        //header.setPreferredSize(new Dimension(700,300));
        header.setBackground(new Color (137, 189, 227));
        JLabel title = new JLabel(" Rainy Words ");
        title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 64));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        
        singPart = new JPanel(new GridBagLayout());
        singPart.setBackground(new Color (137, 189, 227));
        sButton = new JButton("Single Player");
        sButton.addActionListener(new ActionListener() { //Single
        	@Override 
        	public void actionPerformed(ActionEvent e) {
        		onClickSinglePlayer();
            }
        });
        singPart.add(sButton);
        
        hostPart = new JPanel(new GridBagLayout());
        hostPart.setBackground(new Color (137, 189, 227));
        hButton = new JButton("Host");
        hostPart.add(hButton);
        hButton.addActionListener(new ActionListener() { //host
        	@Override 
        	public void actionPerformed(ActionEvent e) {
        		onClickHostGame();
            }
        });
        
        
        joinPart = new JPanel(new GridBagLayout());
        joinPart.setBackground(new Color (137, 189, 227));
        //multiPart = new JPanel(new BorderLayout(0,400));
        jButton = new JButton("Join");
        //mButton.setPreferredSize(new Dimension (400,100));
        jButton.addActionListener(new ActionListener() { //join
            
        	@Override 
        	public void actionPerformed(ActionEvent e) {
        		onClickJoinGame();
                
        	}
         });
        
        joinPart.add(jButton);
        
        jservePart = new JPanel(new GridBagLayout());
        jservePart.setBackground(new Color (137, 189, 227));
        jsButton = new JButton("Join Server");
        jsButton.addActionListener(new ActionListener() { //join server
        	@Override 
        	public void actionPerformed(ActionEvent e) {
        		onClickJoinServerGame();
            }
        });
        jservePart.add(jsButton);
        
        
        choicePart = new JPanel(new GridLayout(2,2));
        choicePart.add(singPart);
        choicePart.add(hostPart);
        choicePart.add(joinPart);
        choicePart.add(jservePart);
        
        //frame.getContentPane().setBackground(new Color (137, 189, 227));
        frame.add(header);
        frame.add(choicePart);
        //frame.add(page);  
        frame.pack();
        frame.setVisible(true);
        
    }

	
	public static void onClickSinglePlayer(){
		
	}
	
	public static void onClickHostGame(){
		
	}
	
	public static void onClickJoinGame(){
		
	}
	
	public static void onClickJoinServerGame(){
		try {
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						RainyWordsClient.main(new String[] {"123"});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}