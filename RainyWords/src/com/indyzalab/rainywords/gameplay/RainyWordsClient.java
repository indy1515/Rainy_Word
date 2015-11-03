package com.indyzalab.rainywords.gameplay;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
            	
            	boolean foundWordToRemoved = gamePanel.removeWords(textField.getText());
            	// Check if it is feasible
            	if(foundWordToRemoved){
            		JSONObject jObj = CommandHelper
                			.getCommandDataJSON(CommandConstants.PLAYER_COMPLETE, textField.getText());
            		out.println(jObj.toString());
            		textField.setText("");
            	}
            
            }
        });
        
        setCurrentTimeLabel(Constants.GAME_TIME/1000);
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
     * Prompt for and return the desired screen name.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {

        // Make connection and initialize streams
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 8901);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Process all messages from server, according to the protocol.
        while (true) {
        	System.out.println("Recieving Response");
            String line = in.readLine();
            Object obj=JSONValue.parse(line);
            JSONObject jObj=(JSONObject)obj;
            
            System.out.println("Line: "+line);
            String command = jObj.get(CommandConstants.COMMAND).toString();
            if (command.equals(CommandConstants.SUBMITNAME)) {
            	System.out.println("Submit Name");
                out.println(getName());
            } else if (command.equals(CommandConstants.NAMEACCEPTED)) {
            	// Create current Player
            	currentPlayer = new Player((JSONObject)jObj.get(CommandConstants.DATA));
            	System.out.println("Current Player: "+currentPlayer);
                textField.setEditable(true);
                
            } else if(command.equals(CommandConstants.WORDS_DATA)){
            	JSONArray wordJsonArray = (JSONArray)jObj.get(CommandConstants.DATA);
            	for(Object object:wordJsonArray){
            		JSONObject jsonObject = (JSONObject)object;
            		gamePanel.addWords(new Word(jsonObject,size));
            	}
            	
            	
        	} else if(command.equals(CommandConstants.PLAYER_LIST)){
        		// register opponent player
        		JSONArray playerJSONArray = (JSONArray)jObj.get(CommandConstants.DATA);
        		for(Object pObj:playerJSONArray){
        			JSONObject playerJsonObj = (JSONObject) pObj;
        			Player player = new Player(playerJsonObj);
        			if(currentPlayer == null) break;
        			if(player.uniqueID.equals(currentPlayer.uniqueID)) continue;
        			otherPlayer.add(player);
        		}
        		
        		// Assign name to opponent and the player
        		if(!otherPlayer.isEmpty()){
        			Player opponent = otherPlayer.get(0);
        		}
        		
        	} else if(command.equals(CommandConstants.ALL_READY)){
            	gamePanel.startPolling();
            	gamePanel.startTimer();
            
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
            	gamePanel.removeWords(removed_word);
            	
            	messageArea.append(jObj.get(CommandConstants.DATA) + "\n");
            } else{
            	messageArea.append(line + "\n");
            }
            refreshData();
        }
    }

    
    public void refreshData(){
    	if(currentPlayer == null) return;
    	System.out.println("RefreshData: "+currentPlayer);
    	myLabel.setText(currentPlayer.name);
    	myPoints.setText(currentPlayer.points+"");
    	if(!otherPlayer.isEmpty()){
    		Player opponent = otherPlayer.get(0);
    		opLabel.setText(opponent.name);
    		opPoints.setText(opponent.points+"");
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

    
    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
    	RainyWordsClient client = new RainyWordsClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
    
    

}
