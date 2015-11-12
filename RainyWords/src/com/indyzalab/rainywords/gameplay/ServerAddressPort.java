package com.indyzalab.rainywords.gameplay;

public class ServerAddressPort {
	String serverAdress;
	int serverPort;
	public ServerAddressPort(String serverAdress, int serverPort) {
		super();
		this.serverAdress = serverAdress;
		this.serverPort = serverPort;
	}
	public String getServerAdress() {
		return serverAdress;
	}
	public void setServerAdress(String serverAdress) {
		this.serverAdress = serverAdress;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	

	

}
