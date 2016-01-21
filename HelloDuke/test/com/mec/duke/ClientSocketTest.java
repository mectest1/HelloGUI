package com.mec.duke;

import static org.junit.Assert.fail;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

public class ClientSocketTest {

	@Test
	public void testConnecToServer() throws Exception{
//		fail("Not yet implemented");
		
		connectServer();
		
	}

	

	private void connectServer() throws Exception{
		
		ByteBuffer helloBuffer = ByteBuffer.wrap("Hello!".getBytes());
		
		//
		
		//Create a new socket channel
		try(SocketChannel socketChannel = SocketChannel.open()){
			//continue if it was successfully created
			if(socketChannel.isOpen()){
				
				//Set the blocking mode
				socketChannel.configureBlocking(true);
				//Set some options
				socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
				socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
				socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
				socketChannel.setOption(StandardSocketOptions.SO_LINGER, 5);
				//Connect this channel's socket
				socketChannel.connect(new InetSocketAddress(IP, DEFAULT_PORT));
				
				//Check if the connection was successfully accomplished
				if(!socketChannel.isConnected()){
					out.println("The connection to server cannot be established");
					return;
				}
				
				//transmitting data
				socketChannel.write(helloBuffer);
				
//				ForkJoinPool.commonPool().execute(() -> sendMsg(socketChannel));
				sendMsg(socketChannel);
				//
				
			}else{
				out.println("The socket cannot be opened");
			}
		}
	}
	
	

	private void sendMsg(SocketChannel socketChannel){
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		ByteBuffer randomBuffer;
		CharBuffer charBuffer;
		Charset charset = Charset.defaultCharset();
		CharsetDecoder decoder = charset.newDecoder();
		try{
			while(-1 < socketChannel.read(buffer) || 0 < buffer.position()){
				buffer.flip();
				
				charBuffer = decoder.decode(buffer);
				out.printf("Received message: %s\n", charBuffer);
				
				//
				if(buffer.hasRemaining()){
					buffer.compact();
				}else{
					buffer.clear();
				}
				
				//
				int r = new Random().nextInt(100);
				if(50 == r){
					out.println("50 was generated! Close the socket channel");
					break;
				}else{
					String sendMsg = "Random number:".concat(String.valueOf(r));
					randomBuffer = ByteBuffer.wrap(sendMsg.getBytes());
					out.printf("Send message: %s\n", sendMsg);
					socketChannel.write(randomBuffer);
				}
			}
		}catch(Exception e){
			e.printStackTrace(out);
		}
	}
	
	

	
	final int DEFAULT_PORT = 5555;
	final String IP = "127.0.0.1";
	private static final PrintStream out = System.out;
	
	
}
