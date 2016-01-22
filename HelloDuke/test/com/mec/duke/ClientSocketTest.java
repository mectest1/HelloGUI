package com.mec.duke;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

public class ClientSocketTest {

	@Ignore
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
	
	
	@Test	//client test failed, may need to check this later (after knowing more about the network theory)
	public void testNoBlockingClient() throws Exception{
		ByteBuffer buffer = ByteBuffer.allocateDirect(2 * 1024);
		ByteBuffer randomBuffer;
		CharBuffer charBuffer;
		
		CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
		
		//Open Selector and ServerSocketChannel by calling the open() method;
		try(Selector selector = Selector.open()){
			SocketChannel socketChannel = SocketChannel.open();
			
			//Check that both of them were successfully opened.
			if(!(selector.isOpen() &&  socketChannel.isOpen())){
				out.println("The socket channel or selector cannot be opened");
				return;
			}
			
			//Configure non-blocking mode
			socketChannel.configureBlocking(false);
			//set some options
			socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
			socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
			socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			
			//register the current channel with the given selector
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			out.printf("Localhost: %s\n", socketChannel.getLocalAddress());
			
			//waiting for the connection
			while(selector.select(1000) > 0){
				//get keys
				for(SelectionKey key : selector.selectedKeys()){
					
					try(SocketChannel keySocketChannel = (SocketChannel) key.channel()){
						//attempt a connection
						if(key.isConnectable()){
							//signal connection success
							out.println("I am connected.");
							
							//close pending connections
							if(keySocketChannel.isConnectionPending()){
								keySocketChannel.finishConnect();
							}
							
							//read/write from/to server
							while(-1 < keySocketChannel.read(buffer)){
								buffer.flip();
								
								charBuffer = decoder.decode(buffer);
								out.printf("Received message: %s\n", charBuffer.toString());
								
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
									randomBuffer = ByteBuffer.wrap("Random Number".concat(String.valueOf(r)).getBytes("UTF-8"));
									keySocketChannel.write(randomBuffer);
									
									Thread.sleep(1500);
								}
							}
						}
					}
				}
				selector.selectedKeys().clear();
			}
			
		}
		
	}
	

	
	final int DEFAULT_PORT = 5555;
	final String IP = "127.0.0.1";
	private static final PrintStream out = System.out;
	
	
}





























