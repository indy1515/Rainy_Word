package com.indyzalab.rainywords.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JPanel;

import com.indyzalab.rainywords.gameplay.Constants;
import com.indyzalab.rainywords.utils.GamePanelListener;

public class GamePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Word> words = new ArrayList<Word>();
	ArrayList<Word> pre_words = new ArrayList<Word>();
	
	ArrayList<String> dictionaryWordList = new ArrayList<String>();
	boolean isLoading = true;
	
	double rate_of_y_axis_speed = 5;
	double fall_speed = 100;
	double word_delay = Constants.WORD_DELAY;
	boolean isStart = false;
	Dimension d;
	GamePanelListener listener = new GamePanelListener() {
		
		@Override
		public void onTimerComplete() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onTick(int current_time) {
			// TODO Auto-generated method stub
			
		}
	};
	public GamePanel(Dimension d){
		this.d = d;
		setPreferredSize(d);
		setBackground(Color.WHITE);
//		setOpaque(true);
		setFallSpeed(Constants.FALL_SPEED);
		setOpaque(true);
		setMovingRateY(Constants.PIXEL_Y_AXIS_SPEED);
		
		final Thread t = new Thread(){
			public void run(){
				while(true){
					try {
						checkWordsState();
						Thread.sleep((long) fall_speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
				}
			}
		};
		t.start();
		
	}
	
	public void setFallSpeed(int milli){
		fall_speed = milli;
	}
	
	
	public void addGamePanelListener(GamePanelListener listener){
		this.listener = listener;
	}
	
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Iterator<Word> iter = words.iterator();
        for(int i = 0;i<words.size();i++){
//        	Word word = iter.next();
        	words.get(i).paintComponent(g);
        }
        if(isStart){
        	
        }
    }
	
	public void setMovingRateY(double y){
		rate_of_y_axis_speed = y;
	}
	
	
	public void moveWordDown(Word word){
		word.pos_y += rate_of_y_axis_speed;
	}
	
	public void checkWordsState(){
		ArrayList<Word> removedWord = new ArrayList<Word>();
		for(int i=0;i<words.size();i++){
			Word word = words.get(i);
        	if(word.getState() == Word.STATE_REMOVED){
        		removedWord.add(word);
        	}else{
        		moveWordDown(word);
        		Dimension d = new Dimension(Constants.GAMEUI_WIDTH, Constants.GAMEUI_HEIGHT);
        		if(word.pos_y > d.height){
//        			word.setAsRemovedState();
        			removedWord.add(word);
        		}
        	}
        }
		words.removeAll(removedWord);
	}
	
	public boolean removeWords(String word){
		for(Word w:words){
			if(w.name.equals(word)){
				w.setAsRemovedState();
				return true;
			}
		}
		return false;
					
	}
	Thread generateWordThread ;
	
	
	public void generateWordsWithTime(int millisec){
		int amount = (int)(millisec/word_delay);
		generateWords(amount);
	}
	
	public void generateWords(final int amount){
		generateWordThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(isLoading){
					try {
						generateWordThread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Add Word");
				for(int i = 0;i<amount;i++){
					addWords(getRandomWord());
				}
			}
		});
		generateWordThread.start();
		
	}
	
	public String getRandomWord(){
		Random Dice = new Random(); 
		int n = Dice.nextInt(dictionaryWordList.size()); 
		return dictionaryWordList.get(n);
	}
	
	public void addWords(String word){
		pre_words.add(new Word(word,(int)(Math.random()*d.width),0,d));
	}
	
	public void addWords(Word word){
		pre_words.add(word);
	}
	
	public void setPreWords(ArrayList<Word> words){
		pre_words = words;
	}
	
	
	private void add(Word word){
		words.add(word);
	}
	private void add(String word){
		words.add(new Word(word,(int)(Math.random()*d.width),0,d));
	}
	
	public ArrayList<Word> getWords() {
		return words;
	}

	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}
	
	int delay = 500; // in ms
	Thread polling_thread;
	public void startPolling(){
		polling_thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					try {
						polling_thread.sleep((int)word_delay);
						if(pre_words.isEmpty()) continue;
						Word word = pre_words.get(0);
						pre_words.remove(0);
						add(word);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		polling_thread.start();
	}
	
	Timer timerThread;
	int current_time = 0;
	public void startTimer(){
		isStart =true;
		current_time = ((int)Math.ceil(Constants.GAME_TIME/1000.0));
		timerThread = new Timer(1000,new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				current_time--;
				listener.onTick(current_time);
				if(current_time <= 0){
					// stop!
					// End timer
					listener.onTimerComplete();
					stopTimer();
					System.out.println("StopTimer");
				}
			}
		});
		timerThread.start();
	}
	public void stopTimer(){
		isStart = false;
		current_time = 0;
		timerThread.stop();
	}
	
	public void stopPolling(){
		polling_thread.stop();
	}
	
	
}


