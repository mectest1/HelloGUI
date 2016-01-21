package com.mec.duke;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import org.junit.Test;

public class ServerSocketTest {

	@Test
	public void testBlockingEchoServer() throws Exception{
		
//		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		
		//
		//Create a new server socket channel
		try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
				){
			
			//continue if it was successfully created
			if(!serverSocketChannel.isOpen()){
//				out.printf("The server cannot be openned for %s:%s\n", IP, DEFAULT_PORT);
				out.println("The server channel cannot be opened");
				return;
			}
			
			
			//Set the blocking mode;
			serverSocketChannel.configureBlocking(true);
			
			//Set some options
			serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
			serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			
			//Bind the server socket channel to local address
			serverSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));
			
			//display a waiting message while waiting clients
			out.println("Waiting for connections...");
			
			//wait for incoming connections
//			ForkJoinPool.commonPool().execute(() -> acceptConnections(serverSocketChannel)); 
			acceptConnections(serverSocketChannel);
			
//			connectServer();
			//
			Thread.sleep(50 * 1000);
//			Arrays.asList("Hello", "World!").stream().forEach(out::println);
		}
		
		
		
	}

	private void acceptConnections(ServerSocketChannel serverSocketChannel){
		try {
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			while (true) {
				try (SocketChannel socketChannel = serverSocketChannel.accept()) { //<- this method will block
					out.printf("Incoming connection from %s\n", socketChannel.getRemoteAddress());

					
					//transmitting data
					while(-1 < socketChannel.read(buffer)){
						buffer.flip();	//reset position/limit to start/end of read content
						
						socketChannel.write(buffer);	//write 
						
						if(buffer.hasRemaining()){
							buffer.compact();	//move content to start of buffer, and set position/limit to end of content/capacity.
								//<-- What if false == buffer.hasRemaining()?
						}else{
							buffer.clear();
						}
					}
					
				}
			} 
		}catch (Exception e) {
			e.printStackTrace(out);
		}
	}
	
	
	
	final int DEFAULT_PORT = 5555;
	final String IP = "127.0.0.1";
	
	private static final PrintStream out = System.out;
}
































