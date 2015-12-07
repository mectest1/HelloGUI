package com.mec.duke.tut;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

	public Server (int port) throws IOException{
		listen(port);
	}
	
	
	private void listen(int port) throws IOException{
		ss = new ServerSocket(port);
		
		out.printf("Listening on %s\n", ss);
		
		while(true){
			Socket s =  ss.accept();
			
			out.printf("Connection from %s\n", s);
			
			//
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			
			//
			outputStreams.put(s, dout);
			
			//
			new ServerThread(this, s);
			
		}
		
	}
	
	
	public void sendAll(String message){
		try{
			for(DataOutputStream os : outputStreams.values()){
				os.writeUTF(message);
			}
		}catch(IOException e){
			out.println("Send message failed");
			out.println(e);
		}
	}
	
	public void removeConnection(Socket clientSocket){
		outputStreams.remove(clientSocket);
	}
	
	private HashMap<Socket, DataOutputStream> outputStreams = new HashMap<>();
	private ServerSocket ss;
	private static final PrintStream out = System.out;
}
