package com.indyzalab.rainywords.gameplay;

import java.net.ServerSocket;

public class RainyWordsServer {
	public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(8901);
        System.out.println("Rainy Words Server is Running");
        try {
        	int i = 0;
        	RWGame game = new RWGame();
            while(true){
                RWGame.Handler player1 = game.new Handler(listener.accept(), "Test"+i);
                player1.start();
                i++;
            }
        } finally {
            listener.close();
        }
    }
}
