package com.indyzalab.rainywords;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

import com.indyzalab.rainywords.components.ListDemo;
import com.indyzalab.rainywords.components.LoginPanel;
import com.indyzalab.rainywords.components.ServiceLoginPanel2;


public class MainProcess {
	final static int MAIN_FRAME_HEIGHT = 1000;
	final static int MAIN_FRAME_WIDTH = 500;
	final static JFrame frame = new JFrame("New");
	static Thread hostThread;
	static Thread clientThread;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 createLoginLayout();
		// Show First UI
		
		// Added onClick for first UI
	}
	
	
	public static void createLoginLayout(){
		final String NOT_LOGGED_IN = "LoginPanel Test - Currently Logged Out";
        frame.getContentPane().add(new LoginPanel() {
            public boolean approveLogin(String uname) {
                // this is where to make the server call to approve or reject login attempt
                frame.setTitle("LoginPanel Test - Currently logged in as " + uname);
                removeLayout(this);
                createServiceLoginPanel();
                return true;
            }
            public void loggedOut(String uname) {
                frame.setTitle(NOT_LOGGED_IN);
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(MAIN_FRAME_HEIGHT, MAIN_FRAME_WIDTH);
        frame.setVisible(true);
	}
	
	public static void createServiceLoginPanel(){
		final String NOT_LOGGED_IN = "LoginPanel Test - Currently Logged Out";
        frame.getContentPane().add(new ServiceLoginPanel2() {
        	public void onClickHostBtn(){
        		System.out.println("Host Server");
        		final HostListener hl = new HostListener() {
					
					@Override
					public void startHosting() {
						// TODO Auto-generated method stub

						
						try {
							createClient();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

					}
				};
    			hostThread = new Thread(new Runnable() {
    				
    				@Override
    				public void run() {
    					// TODO Auto-generated method stub
    					try {
							hostServer(hl);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("Host Error");
							e.printStackTrace();
						}
    				}
    			});
    			hostThread.start();
    			
	
        	}

            public boolean approveLogin(String uname, String pswd) {
                // this is where to make the server call to approve or reject login attempt
                frame.setTitle("LoginPanel Test - Currently logged in as " + uname);
//                createList();
                try {
					createClient();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                return true;
            }
            public void loggedOut(String uname) {
                frame.setTitle(NOT_LOGGED_IN);
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(MAIN_FRAME_HEIGHT, MAIN_FRAME_WIDTH);
        frame.setVisible(true);
			
	}
	
	public static void createList(){
//		frame.setEnabled(false);
		JDialog newframe = new JDialog(frame,"List of Host");
//		newframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newframe.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		JComponent newContentPane = new ListDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        newframe.setContentPane(newContentPane);
        
        //Display the window.
        newframe.pack();
        newframe.setSize(450, 330);
        newframe.setVisible(true);
	}
	public static void removeLayout(Component comp){
		
		frame.remove(comp);
		frame.revalidate();
		frame.repaint();
		frame.setBackground(new Color(255,255,255));
	}

	

	public static void hostServer(HostListener hl) throws Exception{
		
		ServerSocket listener = new ServerSocket(8901);
        System.out.println("Tic Tac Toe Server is Running");
        try {
        	hl.startHosting();
            while (true) {
                Game game = new Game();
                Game.Player playerX = game.new Player(listener.accept(), 'X');
                Game.Player playerO = game.new Player(listener.accept(), 'O');
                playerX.setOpponent(playerO);
                playerO.setOpponent(playerX);
                game.currentPlayer = playerX;
                playerX.start();
                playerO.start();
            }
        } finally {
            listener.close();
        }
	}
	
	
	
	
	
	public static void createClient() throws Exception{
		clientThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
			            String serverAddress = "localhost";
			            TicTacToeClient client = new TicTacToeClient(serverAddress);
			            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			            client.frame.setSize(240, 160);
			            client.frame.setVisible(true);
			            client.frame.setResizable(true);
			            client.play();
			            if (!client.wantsToPlayAgain()) {
			                break;
			            }
			            try {
			    			clientThread.sleep(100);
			    		} catch (InterruptedException e) {
			    			// TODO Auto-generated catch block
			    			e.printStackTrace();
			    		}
			        }
				}catch(Exception e){
					
				}
			}
		});
		
		clientThread.start();;
				
				
	}
	
	
	

}
