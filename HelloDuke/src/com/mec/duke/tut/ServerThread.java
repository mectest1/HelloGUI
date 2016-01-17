package com.mec.duke.tut;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ServerThread extends Thread {

	public ServerThread(Server server, Socket socket){
		this.server = server;
		this.socket = socket;
		
//		start();
	}
	
	
	
	@Override
	public void run() {
		try {
			DataInputStream din = new DataInputStream(socket.getInputStream());
			//
			while (true) {
				String message = din.readUTF();

				out.printf("Sending %s\n", message);
				
				server.sendAll(message);
				
			} 
		} catch (EOFException ie) {
			out.printf("Connection closed. Received exception is %s\n", ie);
		}catch(IOException ie){
			out.println(ie);
		}finally{
			out.printf("Connection from %s closed.\n", socket);
			server.removeConnection(socket);
		}
	}



	private Server server;
	private Socket socket;
	private static final PrintStream out = System.out;
}
