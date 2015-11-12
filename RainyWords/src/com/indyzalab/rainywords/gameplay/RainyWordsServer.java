package com.indyzalab.rainywords.gameplay;

import java.io.IOException;
import java.net.ServerSocket;

public class RainyWordsServer {
	public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(8901);
        System.out.println(listener.getInetAddress().getHostAddress().toString());
//        ServerSocket listener2 = new Ser
        System.out.println("Rainy Words Server is Running");
        try {
        	int i = 0;
        	RWGame game = new RWGame(true);
            while(true){
                RWGame.Handler player1 = game.new Handler(listener.accept());
                player1.start();
                i++;
            }
        } finally {
            listener.close();
        }
    }
	
	public static void startServer(boolean withUI){
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(8901);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(listener.getInetAddress().getHostAddress().toString());
//        ServerSocket listener2 = new Ser
        System.out.println("Rainy Words Server is Running");
        try {
        	int i = 0;
        	RWGame game = new RWGame(withUI);
            while(true){
                RWGame.Handler player1 = null;
				try {
					player1 = game.new Handler(listener.accept());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                player1.start();
                i++;
            }
        } finally {
            try {
				listener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
