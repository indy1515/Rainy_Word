package com.indyzalab.rainywords.gameplay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.indyzalab.rainywords.components.GamePanel;
import com.indyzalab.rainywords.components.Word;
import com.indyzalab.rainywords.utils.GamePanelListener;

public class RainyWordsClient {
	BufferedReader in;
    PrintWriter out;
    Player currentPlayer; // the client
    ArrayList<Player> otherPlayer = new ArrayList<Player>(); // other client
    JFrame frame = new JFrame("Rainy Words");
    JTextField textField = new JTextField(40);
    JPanel upperPanel = new JPanel(new GridBagLayout());
    JLabel myPoints = new JLabel("0", SwingConstants.CENTER);
    JLabel myLabel = new JLabel("Player1", SwingConstants.CENTER);
    JLabel timeLabel = new JLabel("3:00", SwingConstants.CENTER);
    JLabel opPoints = new JLabel("0", SwingConstants.CENTER);
    JLabel opLabel = new JLabel("Player2", SwingConstants.CENTER);
    JTextArea messageArea = new JTextArea(8, 40);
    GamePanel gamePanel;
    Dimension size = new Dimension(Constants.GAMEUI_WIDTH,Constants.GAMEUI_HEIGHT);
    
    private static boolean useCustomIpPort =true;
    private static JMenuBar menuBar;
	private static JMenu menu;
	private static JMenuItem menuItem;
	private static JMenuItem menuItemMute;
	private static JMenuItem menuItemUnmute;
	private static JMenuItem menuExit;
    
	static AudioInputStream audioInputStream;
	static Clip clip;
	private File file;
	
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Return in the
     * listener sends the textfield contents to the server.  Note
     * however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED
     * message from the server.
     */
    public RainyWordsClient() {

        // Layout GUI
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = 1;
    	c.gridy = 0;
    	upperPanel.add(myPoints,c);

    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = 2;
    	c.gridy = 0;
    	upperPanel.add(myLabel,c);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = 3;
    	c.gridy = 0;
    	upperPanel.add(timeLabel,c);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = 4;
    	c.gridy = 0;
    	upperPanel.add(opLabel,c);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = 5;
    	c.gridy = 0;
    	upperPanel.add(opPoints,c);
        textField.setEditable(false);
        messageArea.setEditable(false);
        
//		frame.setPreferredSize(size);
        frame.getContentPane().add(upperPanel,"North");
        frame.getContentPane().add(textField, "South");
        gamePanel = new GamePanel(size);
        gamePanel.addGamePanelListener(new GamePanelListener() {
			
			@Override
			public void onTimerComplete() {
				// TODO Auto-generated method stub
				gamePanel.stopPolling();
				// Game end 
				JSONObject jObj = CommandHelper
            			.getCommandDataJSON(CommandConstants.GAME_END
            					, currentPlayer.getCurrentJson());
        		out.println(jObj.toString());
        		textField.setText("");
        		
				
			}
			
			@Override
			public void onTick(int current_time) {
				// TODO Auto-generated method stub
				setCurrentTimeLabel(current_time);
			}
		});
        frame.getContentPane().add(gamePanel, "Center");
//        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();
//        gamePanel.generateWords(100);
        
        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server.    Then clear
             * the text area in preparation for the next message.
             */
            public void actionPerformed(ActionEvent e) {
            	if(textField.getText().equals("Start")){
            		gamePanel.startPolling();
            	}
            	
            	// If delay you may consider using this?
//            	boolean foundWordToRemoved = gamePanel.removeWords)(textField.getText());
            	boolean foundWordToRemoved = gamePanel.containWords(textField.getText());
            	// Check if it is feasible
            	if(foundWordToRemoved){
            		JSONObject jObj = CommandHelper
                			.getCommandDataJSON(CommandConstants.PLAYER_COMPLETE, textField.getText());
            		out.println(jObj.toString());
            		textField.setText("");
            	}
            
            }
        });
        createMenu();
        playSong();
        setCurrentTimeLabel(Constants.GAME_TIME/1000);
//        showGameResult();
    }

    
    
    /**
     * Position start at 0 to x
     * Row amount are state by max_player_on_line
     * @param position
     */
    public void addElement(int position){
    	int max_player_on_line = 3;
    	int row = max_player_on_line/3;
    	JLabel myPoints = new JLabel("0", SwingConstants.CENTER);
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = position*2;
    	c.gridy = row;
    	upperPanel.add(myPoints,c);
    	
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = position*2+1;
    	c.gridy = row;
    	upperPanel.add(myLabel,c);
    }
    
    private int showDisconnected(Player player){
    	
    	String result = player.name+" has disconnected! Press Ok to find new opponent";
    	
    	Object[] options = {"OK"};
        int n = JOptionPane.showOptionDialog(frame,
        		result,"Notice",
                       JOptionPane.PLAIN_MESSAGE,
                       JOptionPane.QUESTION_MESSAGE,
                       null,
                       options,
                       options[0]);
        if (n == JOptionPane.OK_OPTION) {
            System.out.println("OK!"); // do something
            
        }
    	return n;
    }
    /**
     * Prompt for and return the address of the server.
     */
    private int showGameResult() {
    	// Check if we win/lose
    	String result = "YOU WIN!";
    	if(currentPlayer == null) return 0;
    	System.out.println("Result Other Player: "+otherPlayer.size());
    	if(currentPlayer.points > otherPlayer.get(0).points){
    		// more than WIN
    		result = "YOU WIN!";
    	}else if(currentPlayer.points < otherPlayer.get(0).points){
    		// less than LOSE
    		result = "YOU LOSE!";
    	}else{
    		// equal
    		result = "DRAW!";
    	}
    	
    	result += "\nYour score: "+currentPlayer.points;
    	Object[] options = {"EXIT","RESET GAME"};
        int n = JOptionPane.showOptionDialog(frame,
        		result,"Result",
                       JOptionPane.PLAIN_MESSAGE,
                       JOptionPane.QUESTION_MESSAGE,
                       null,
                       options,
                       options[1]);
        if (n == 0) {
            System.out.println("EXIT!"); // do something
            System.exit(1);
        }else if(n== 1){
        	System.out.println("RESET");
        	sendResetRequest();
        }
        System.out.println("Int n: "+n);
    	return n;
    }
    
    private int showWelcomeMessage(){
    	Object[] options = {"Let's Go!"};
    	String content = "Welcome "+currentPlayer.name+"!";
    	return JOptionPane.showOptionDialog(frame,
        		content,"Welcome to Rainy Word!",
                       JOptionPane.PLAIN_MESSAGE,
                       JOptionPane.QUESTION_MESSAGE,
                       null,
                       options,
                       options[0]);
    }
    
    private int getReadyStatus(){
    	if(currentPlayer.isReady) return -1;
    	Object[] options = {"I'm ready!"};
    	String content = "Are you ready?";
    	return JOptionPane.showOptionDialog(frame,
        		content,"Ready?",
                       JOptionPane.PLAIN_MESSAGE,
                       JOptionPane.QUESTION_MESSAGE,
                       null,
                       options,
                       options[0]);
    }
    
    /**
     * Prompt for and return the address of the server.
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Chatter",
            JOptionPane.QUESTION_MESSAGE);
    }
    
    /**
     * 
     */
    private ServerAddressPort getServerAndPortAddress(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        JLabel lbUsername = new JLabel("Server IP: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);
 
        JTextField xField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(xField, cs);
 
        JLabel lbPassword = new JLabel("Port (0-65536): ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);
 
        JTextField yField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(yField, cs);
//        panel.setBorder(new LineBorder(Color.GRAY));

        int result = JOptionPane.showConfirmDialog(null, panel, 
                 "Please Enter Server and Port", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
        	ServerAddressPort serverAddressPort = null;
        	String serverAddress = xField.getText();
        	if(serverAddress == null || serverAddress.equals("")) serverAddress = "localhost";
        	String portString = yField.getText();
        	int port = 8901;
        	if(portString == null || portString.equals("")){
        		// if not found
        	}else{
	        	try{
	        		port = Integer.parseInt(portString);
	        		serverAddressPort = new ServerAddressPort(serverAddress, port);
	        	}catch(NumberFormatException e){
	        		serverAddressPort = getServerAndPortAddress();
	        	}
        	}
        	return new ServerAddressPort(serverAddress, port);
        }
        return new ServerAddressPort("localhost",8901);
    }

    /**
     * Prompt for and return the desired screen name.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

    
    private void serverError() {
    	// Check if we win/lose
    	String result = "The server may not be open or not available right now.\nPlease connect again later or check the IP and port number.";
    	Object[] options = {"OK"};
        int n = JOptionPane.showOptionDialog(frame,
        		result,"Error",
                       JOptionPane.PLAIN_MESSAGE,
                       JOptionPane.QUESTION_MESSAGE,
                       null,
                       options,
                       options[0]);
        if (n == JOptionPane.OK_OPTION) {
            System.out.println("OK!"); // do something
            System.exit(1);
        }
    }
    
    /**
     * Connects to the server then enters the processing loop.
     */
	private void run() throws IOException {

        // Make connection and initialize streams
//        String serverAddress = getServerAddress();
		ServerAddressPort serverAddressPort = new ServerAddressPort("192.168.43.200", 8901);
		if(useCustomIpPort){
			serverAddressPort = getServerAndPortAddress();
		}
        Socket socket = null;
        try{
        	socket = new Socket(serverAddressPort.getServerAdress(), serverAddressPort.getServerPort());
        }catch(ConnectException e){
        	serverError();
        }
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
//        192.168.43.200
        out = new PrintWriter(socket.getOutputStream(), true);
        // Process all messages from server, according to the protocol.
        while (true) {
        	System.out.println("Recieving Response");
            String line = in.readLine();
            System.out.println("Line :"+line);
            if(line == null){
            	continue;
            }
            Object obj=JSONValue.parse(line);
            JSONObject jObj=(JSONObject)obj;
            
            System.out.println("Line: "+line);
            String command = jObj.get(CommandConstants.COMMAND).toString();
            if (command.equals(CommandConstants.SUBMITNAME)) {
            	System.out.println("Submit Name");
            	String name = getName();
                out.println(name == null?"":name);
                out.flush();
            } else if (command.equals(CommandConstants.NAMEACCEPTED)) {
            	// Create current Player
            	currentPlayer = new Player((JSONObject)jObj.get(CommandConstants.DATA));
            	System.out.println("Current Player: "+currentPlayer);
                textField.setEditable(true);
                showWelcomeMessage();
                
            } else if (command.equals(CommandConstants.CHECK_READY)){
            	System.out.println("Check ready?");
            	out.println(getReadyStatus());
            	out.flush();
            } else if(command.equals(CommandConstants.WORDS_DATA)){
            	// Clear word_data
            	JSONArray wordJsonArray = (JSONArray)jObj.get(CommandConstants.DATA);
            	ArrayList<Word> newWord = new ArrayList<Word>();
            	for(Object object:wordJsonArray){
            		JSONObject jsonObject = (JSONObject)object;
            		newWord.add(new Word(jsonObject,size));
            		
            	}
            	gamePanel.setPreWords(newWord);
            	
            	
        	} else if(command.equals(CommandConstants.PLAYER_LIST)){
        		// register opponent player
        		JSONArray playerJSONArray = (JSONArray)jObj.get(CommandConstants.DATA);
        		for(Object pObj:playerJSONArray){
        			JSONObject playerJsonObj = (JSONObject) pObj;
        			Player player = new Player(playerJsonObj);
        			if(currentPlayer == null) break;
        			if(player.uniqueID.equals(currentPlayer.uniqueID)) {
        				// set data
        				currentPlayer = player;
        				continue;
        			}
        			boolean found = false;
        			for(Player op : otherPlayer){
        				if(player.uniqueID.equals(op.uniqueID)){
        					// set data
        					op.points = player.points;
        					op.name = player.name;
        					op.isReady = player.isReady;
        					found = true;
        					continue;
        				}
        			}
        			if(!found) otherPlayer.add(player);
        		}
        		
        	} else if(command.equals(CommandConstants.ALL_READY)){
            	startGame();
            
        	} else if(command.equals(CommandConstants.PLAYER)){ 
        		JSONObject playerJSON = (JSONObject)jObj.get(CommandConstants.DATA);
        		Player player = new Player(playerJSON);
        		System.out.println(player);
        		if(player.uniqueID.equals(currentPlayer.uniqueID)){
        			currentPlayer.points = player.points;
        		}else if(!otherPlayer.isEmpty()){
        			if(player.uniqueID.equals(otherPlayer.get(0).uniqueID))
        				otherPlayer.get(0).points = player.points;
        		}
        	} else if (command.equals(CommandConstants.MESSAGE)) {
                messageArea.append(line.substring(CommandConstants.MESSAGE.length()+1) + "\n");
            } else if (command.equals(CommandConstants.WORD_REMOVED)){
            	JSONObject word_remove = (JSONObject)jObj.get(CommandConstants.DATA);
            	String removed_word = (String)word_remove.get(CommandConstants.WORD);
            	Player player = new Player((JSONObject)word_remove.get(CommandConstants.PLAYER));
            	int point = Integer.parseInt(word_remove.get(CommandConstants.GAIN_POINTS).toString());
            	gamePanel.removeWords(removed_word,player.getColor(),point);
//            	messageArea.append(jObj.get(CommandConstants.DATA) + "\n");
            }else if (command.equals(CommandConstants.SERVER_RESET_REQUEST)){
            	sendConfirmReset();
            }
            else if (command.equals(CommandConstants.PLAYER_DISCONNECTED)){
            	JSONObject disconnectPlayerJSONObj = (JSONObject)jObj.get(CommandConstants.DATA);
            	Player disconnectedPlayer = new Player(disconnectPlayerJSONObj);
            	removePlayer(disconnectedPlayer);
            	resetGame();
            	showDisconnected(disconnectedPlayer);
            	
            }
            else if (command.equals(CommandConstants.GAME_END_RESULT)){
            	JSONArray playerJSONArray = (JSONArray)jObj.get(CommandConstants.DATA);
        		for(Object pObj:playerJSONArray){
        			JSONObject playerJsonObj = (JSONObject) pObj;
        			Player player = new Player(playerJsonObj);
            		System.out.println(player);
            		if(player.uniqueID.equals(currentPlayer.uniqueID)){
            			currentPlayer.points = player.points;
            		}else if(!otherPlayer.isEmpty()){
            			if(player.uniqueID.equals(otherPlayer.get(0).uniqueID))
            				otherPlayer.get(0).points = player.points;
            		}
        		}
        		
        		// Show who win/lose
        		showGameResult();
        		
            } else if (command.equals(CommandConstants.FORCE_RESET)){
            	// Force reset game
            	System.out.println("Force Reset");
            	resetGame();
            }else{
            	
            }
            refreshData();
        }
    }

    public void addPlayer(){
    	
    }
    
    public void removePlayer(Player player){

    	boolean removed = otherPlayer.remove(player);
    	if(removed){
    		System.out.println("Player Disconnected: "+player.name);
    	}else{
    		System.out.println("Player Disconnection not removed");
    	}
    	
    }
    
    public void startGame(){
    	if(gamePanel == null) return;
    	gamePanel.startPolling();
    	gamePanel.startTimer();
    	
    }
    
    public void resetGame(){
    	if(gamePanel == null) return;
    	gamePanel.stopPolling();
    	gamePanel.stopTimer();
    }
    
    public void refreshData(){
    	if(currentPlayer == null) return;
    	System.out.println("RefreshData: "+currentPlayer);
    	String current_status = "Not Ready";
    	if(currentPlayer.isReady){
    		current_status = "Ready";
    	}
    	myLabel.setText(currentPlayer.name+"("+current_status+")");
    	myLabel.setForeground(currentPlayer.getColor());
    	myPoints.setText(currentPlayer.points+"");
    	if(!otherPlayer.isEmpty()){
    		Player opponent = otherPlayer.get(0);
    		String opp_status = "Not Ready";
    		if(opponent.isReady){
    			opp_status = "Ready";
        	}
    		opLabel.setText(opponent.name+"("+opp_status+")");
    		opLabel.setForeground(opponent.getColor());
    		opPoints.setText(opponent.points+"");
    	}else{
    		opLabel.setText("Wait for player");
    	}
    }
    
    public void setCurrentTimeLabel(int time){
    	int minutes = time/60;
		int seconds = time - minutes*60;
		String timeString = customFormat("00", minutes)+":"+customFormat("00", seconds);
		timeLabel.setText(timeString);
    }
    
    public String customFormat(String pattern, double value ) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        return output;
     }

    public void playSong() {
    	JFileChooser choose_song = new JFileChooser();
		try {
			audioInputStream = AudioSystem.
					getAudioInputStream(new File("src/LOVE ME RIGHT.wav").getAbsoluteFile());
			try {
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (LineUnavailableException e1){
				e1.printStackTrace();
			}
		} catch (UnsupportedAudioFileException e2){
			e2.printStackTrace();
		} catch (IOException e3){
			e3.printStackTrace();
		}
    }
    
    private void createMenu(){
		//Where the GUI is created:
		
		//Create a file chooser
		final JFileChooser fc = new JFileChooser();
		
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(menu);

		//a group of JMenuItems
		menuItem = new JMenuItem("Reset Game",
		                         KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "This doesn't really do anything");
		
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//Handle open button action.
			    if (e.getSource() == menuItem) {
			        // Reset button
			    	sendResetRequest();
			   }
			}
		});
		menuItemMute = new JMenuItem("Mute",
                KeyEvent.VK_T);
		menuItemMute.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//Handle open button action.
			    if (e.getSource() == menuItemMute) {
			    	// Reset button 
			    	clip.stop();
			    }
			}
		});
		
		menuItemUnmute = new JMenuItem("Unmute",
                KeyEvent.VK_T);
		menuItemUnmute.addActionListener(new ActionListener() {
		   
		   @Override
		   public void actionPerformed(ActionEvent e) {
		    // TODO Auto-generated method stub
		    //Handle open button action.
		       if (e.getSource() == menuItemUnmute) {
		        // Reset button 
		    	   clip.start();
		       }
		   }
		});
		
		menuExit = new JMenuItem("Exit");
		menuExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
//		jLabel.setIcon(icon);
		menu.add(menuItem);
		menu.add(menuItemMute);
		menu.add(menuItemUnmute);
		menu.addSeparator();
		menu.add(menuExit);
		
		frame.setJMenuBar(menuBar);
	}
    
    
    public void sendResetRequest(){
    	
    	JSONObject jObj = CommandHelper
    			.getCommandDataJSON(CommandConstants.RESET_REQUEST, currentPlayer.getCurrentJson());
		out.println(jObj.toString());
		out.flush();
		textField.setText("");
    }
    
    public void sendConfirmReset(){
    	if(currentPlayer.isReset) return;
    	System.out.println("Send Confirm Reset");
    	JSONObject jObj = CommandHelper
    			.getCommandDataJSON(CommandConstants.CONFIRM_RESET, currentPlayer.getCurrentJson());
    	
		out.println(jObj.toString());
		out.flush();
		textField.setText("");
    	
    	
    }
    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
    	RainyWordsClient client = new RainyWordsClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
    
    public static void startClient(boolean useNewCustomIpPort){
    	useCustomIpPort = useNewCustomIpPort;
    	RainyWordsClient client = new RainyWordsClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        try {
			client.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

}
