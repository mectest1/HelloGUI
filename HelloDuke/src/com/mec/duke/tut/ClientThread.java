package com.mec.duke.tut;

public class ClientThread implements Runnable {

	public ClientThread(Client client){
		this.client = client;
	}
	@Override
	public void run() {
		try {
			while (running) {
				client.receiveMessage();
				Thread.sleep(1000);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
//	public void setRunning(boolean running) {
//		this.running = running;
//	}
	public void stop(){
		this.running = false;
	}

	private boolean running = true;
	private Client client;
}
