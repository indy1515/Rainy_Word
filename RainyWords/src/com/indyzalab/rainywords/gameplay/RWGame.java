package com.indyzalab.rainywords.gameplay;


import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.indyzalab.rainywords.components.Word;
import com.indyzalab.rainywords.utils.FileReader;



public class RWGame {

	private static AtomicLong playerIdCounter = new AtomicLong();
	
	private static AtomicLong roomIdCounter = new AtomicLong();
	
	/**
     * The port that the server listens on.
     */
    private static final int PORT = 8901;

    public void printServer(String s){
    	System.out.println(s);
    }
    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

	List<Word> words = new ArrayList<Word>();
	ArrayList<Word> pre_words = new ArrayList<Word>();
	ArrayList<String> dictionaryWordList = new ArrayList<String>();
	
	public static ArrayList<Handler> handlers = new ArrayList<Handler>();
	int max_player = 2;
	public static ArrayList<Room> rooms = new ArrayList<Room>();
    
    public boolean isEnd(){
    	return false;
    }
	
    public boolean isEnd(String room_id){
    	boolean isEnd = false;
    	Room room = rooms.get(findRoomIndex(room_id));
    	if(room.handlers.size() < 2) return false;
    	int i = 0;
    	for(Handler handler:room.handlers){
    		if(i==0) isEnd = handler.isGameEnd;
    		isEnd = isEnd && handler.isGameEnd;
    	}
    	return isEnd;
    }
    
    public boolean isAllReady(){
    	boolean ready = false;
    	if(handlers.size() < 2) return false;
    	int i = 0;
    	for(Handler handler:handlers){
    		if(i==0) ready = handler.ready;
    		ready = ready && handler.ready;
    	}
    	return ready;
    }
    
    public boolean isAllReady(String room_id){
    	boolean ready = false;
    	Room room = rooms.get(findRoomIndex(room_id));
    	if(room.handlers.size() < 2) return false;
    	int i = 0;
    	for(Handler handler:room.handlers){
    		if(i==0) ready = handler.ready;
    		ready = ready && handler.ready;
    	}
    	return ready;
    }
    
    
    /**
     * Called by the player threads when a player tries to make a
     * move.  This method checks to see if the move is legal: that
     * is, the player requesting the move must be the current player
     * and the square in which she is trying to move must not already
     * be occupied.  If the move is legal the game state is updated
     * (the square is set and the next player becomes current) and
     * the other player is notified of the move so it can update its
     * client.
     */
    public synchronized boolean wordExist(String word) {
        if (words.contains(word)){
        	return true;
        }
        return true;
    }
    
    public static String createPlayerID()
    {
        return String.valueOf(playerIdCounter.getAndIncrement());
    }
    
    public static String createRoomID()
    {
        return String.valueOf(roomIdCounter.getAndIncrement());
    }
     
    
    public RWGame() {
    	super();
    	System.out.println("Start Reading File");
		// TODO Auto-generated method stub
		FileReader parser = new FileReader("corncob_lowercase.txt");
	    try {
	    	dictionaryWordList = parser.processLineByLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("Done Reading File size: "+dictionaryWordList.size());
    }
    Thread generateWordThread;

    
    public void generateWordsWithTime(int millisec,String room_id){
		int amount = millisec/Constants.WORD_DELAY;
		generateWords(amount+20,room_id);
	}
    
    public void generateWords(final int amount,final String room_id){
    	for(int i = 0;i<amount;i++){
			addWords(getRandomWord(),room_id);
		}
		generateWordThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Add Word");
				for(int i = 0;i<amount;i++){
					addWords(getRandomWord(),room_id);
				}
				
			}
		});
//		generateWordThread.start();
		
	}
    
    
   
    
    public String getRandomWord(){
		Random Dice = new Random(); 
		int n = Dice.nextInt(dictionaryWordList.size()); 
		return dictionaryWordList.get(n);
	}
    

    
    public void addWords(String word,String room_id){
    	Dimension d = new Dimension(Constants.GAMEUI_WIDTH,Constants.GAMEUI_HEIGHT);
		rooms.get(findRoomIndex(room_id)).pre_words.add(new Word(word,(int)(Math.random()*d.width),0,d));
	}
	

    Thread polling_thread;
    int delay = 500; // in ms
    
    public void startPolling(Room room){
    	while(true){
			if(room.pre_words.isEmpty()) break;
			Word word = room.pre_words.get(0);
			room.pre_words.remove(0);
			room.words.add(word);
		
    	}
    	
    }
    
    
    public JSONArray getCurrentPlayerJSONArray(){
    	JSONArray jsonArray = new JSONArray();
    	
    	for(Handler handler:handlers){
    		jsonArray.add(handler.player.getCurrentJson());
    	}
    	return jsonArray;
    }
    
    public JSONArray getCurrentPlayerJSONArray(String roomId){
    	JSONArray jsonArray = new JSONArray();
    	
    	for(Handler handler:rooms.get(findRoomIndex(roomId)).handlers){
    		jsonArray.add(handler.player.getCurrentJson());
    	}
    	return jsonArray;
    }

    public void clientDisconnected(Handler handler){
    	handlers.remove(handler);
        Room room = rooms.get(findRoomIndex(handler.room_id));
        room.handlers.remove(handler);
        if(room.handlers.isEmpty()){
        	rooms.remove(room);
        }else{
        	//TODO: Disconnection
        	// Alert other player that player has disconnected
        }
    }

    public String assignPlayerToRoom(Handler handler){
    	// Check for available room
    	if(haveEmptyRoom()){
    		//find empty room and added to the player
    		for(Room room:rooms){
        		if(!room.isFull()){
        			room.addHandler(handler);
        			return room.id;
        		}
        	}
    	}else{
    		//no empty room create new room
    		Room room = new Room(createRoomID(), max_player);
    		//add player
    		room.addHandler(handler);
    		//add room to rooms list
    		rooms.add(room);
    		return room.id;
    	}
    	return null;
    }
    public int findRoomIndex(String id){
    	int i = 0;
    	for(Room room:rooms){
    		if(room.id.equals(id)){
    			return i;
    		}
    		i++;
    	}
    	return -1;
    	
    }
    public boolean haveEmptyRoom(){
    	if(rooms == null) return false;
    	if(rooms.isEmpty()) return false;
    	for(Room room:rooms){
    		if(!room.isFull()){
    			return true;
    		}
    	}
    	return false;
    }
    
	/**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for a dealing with a single client
     * and broadcasting its messages.
     */
    public class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private Player player;
        private boolean ready = false;
        private String room_id;
        private boolean isGameEnd = false;

        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(Socket socket,String playerName) {
            this.socket = socket;
            this.player = new Player(createPlayerID(),playerName);
            // Add player to list bucket
            handlers.add(this);
            // Add to room
            room_id = assignPlayerToRoom(this);
            
        }

        
        
        public boolean isReady() {
			return ready;
		}



		/**
         * Services this thread's client by repeatedly requesting a
         * screen name until a unique one has been submitted, then
         * acknowledges the name and registers the output stream for
         * the client in a global set, then repeatedly gets inputs and
         * broadcasts them.
         */
        @SuppressWarnings("unchecked")
		public void run() {
            try {

                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                	// Register player
                	JSONObject submitName = 
                			CommandHelper.getCommandDataJSON(
                					CommandConstants.SUBMITNAME, null);
                    out.println(submitName.toString());
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    this.player.name = name;
                    break;
                }

                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                JSONObject nameAccepted = 
                		CommandHelper.getCommandDataJSON(
                				CommandConstants.NAMEACCEPTED
                				, player.getCurrentJson());
                out.println(nameAccepted);
                
                rooms.get(findRoomIndex(room_id)).writers.add(out);
                ready = true;
                if(isAllReady(room_id)){
                	// All is ready so start the game
                	// send initial data
                	ArrayList<String> arrString = new ArrayList<String>();
                	// Initialize word
                	generateWordsWithTime(Constants.GAME_TIME,room_id);
                	startPolling(rooms.get(findRoomIndex(room_id)));
                	
                	// Boardcast all player data
                	JSONArray wordJsonArray = new JSONArray();
                	for(Word word:rooms.get(findRoomIndex(room_id)).words){
                		wordJsonArray.add(word.getCurrentJson());
                	}
                	JSONObject wordsJson = CommandHelper
                			.getCommandDataJSON(CommandConstants.WORDS_DATA
                			, wordJsonArray);
                	arrString.add(wordsJson.toString());
                	
                	JSONObject readyJson = CommandHelper
                			.getCommandDataJSON(CommandConstants.ALL_READY
                			, null);
                	arrString.add(readyJson.toString());
                	
                	// Send info of all player
                	
                	JSONObject playerList = CommandHelper
                			.getCommandDataJSON(CommandConstants.PLAYER_LIST
                			, getCurrentPlayerJSONArray(room_id));
                	arrString.add(playerList.toString());
                	for (PrintWriter writer : rooms.get(findRoomIndex(room_id)).writers) {
                    	// This will broadcast command to all player
                    	for(String s:arrString){
                    		writer.println(s);
                    	}
                    }
                }
                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while (true) {
                    String command = in.readLine();
                    Object obj = null;
                    try{
                    	obj=JSONValue.parse(command);
                    }catch(NullPointerException e){
                    	// When the game is disconnected this is trigger!
                    	// Remove from room
                    	printServer("Error");
                    }
                    JSONObject jObj=(JSONObject)obj;
                    System.out.println(command);
                    if (command == null) {
                        return;
                    }
                    String commandString = jObj.get(CommandConstants.COMMAND).toString();
                    ArrayList<String> arrString = new ArrayList<String>();
                    if (commandString.equals(CommandConstants.PLAYER_COMPLETE)){
                    	String removedWord = jObj.get(CommandConstants.DATA).toString();
                    	if(wordExist(removedWord)){
                    		int points = removedWord.length();
                    		JSONObject removeDataJObj = new JSONObject();
                    		removeDataJObj.put(CommandConstants.PLAYER,player.getCurrentJson());
                    		removeDataJObj.put(CommandConstants.GAIN_POINTS, points);  
                    		removeDataJObj.put(CommandConstants.WORD, removedWord);

                    		JSONObject removeJObj = CommandHelper
                    				.getCommandDataJSON(CommandConstants.WORD_REMOVED, removeDataJObj);
                  
                    		String wordRemove = removeJObj.toString();
                    		player.points += points;
                    		
                    		JSONObject playerJObj = CommandHelper
                    				.getCommandDataJSON(CommandConstants.PLAYER,player.getCurrentJson());	
                    		String currentPoints = playerJObj.toString();
                    		arrString.add(wordRemove);
                    		arrString.add(currentPoints);
                    	}else{
                    		// No word exist send false message
                    	}
                    }
                    else if (commandString.equals(CommandConstants.GAME_END)){
                    	isGameEnd = true;
                    	if(isEnd(room_id)){
	                    	// Notice all player that the game end and show result
	                    	JSONObject playerList = CommandHelper
	                    			.getCommandDataJSON(CommandConstants.GAME_END_RESULT
	                    			, getCurrentPlayerJSONArray(room_id));
	                    	arrString.add(playerList.toString());
                    	}
                    }
                    else if (commandString.equals(CommandConstants.RESET_REQUEST)){
                    	
                    	
                    }
                    else if (commandString.equals(CommandConstants.QUIT)){
                    	return;
                    }
                    for (PrintWriter writer : rooms.get(findRoomIndex(room_id)).writers) {
                    	// This will broadcast command to all player
                    	for(String s:arrString){
                    		writer.println(s);
                    	}
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (out != null) {
                	rooms.get(findRoomIndex(room_id)).writers.remove(out);
                }
                printServer("Remove Handler from list and room");
                handlers.remove(this);
                rooms.get(findRoomIndex(room_id)).handlers.remove(this);
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    

}
