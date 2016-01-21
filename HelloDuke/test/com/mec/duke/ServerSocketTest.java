package com.mec.duke;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import org.junit.Ignore;
import org.junit.Test;

public class ServerSocketTest {

	@Ignore
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
			ForkJoinPool.commonPool().execute(() -> acceptConnections(serverSocketChannel)); 
//			acceptConnections(serverSocketChannel);
			
//			connectServer();
			//
			out.println("Server ended.");
			Thread.sleep(50 * 1000);
//			Arrays.asList("Hello", "World!").stream().forEach(out::println);
		}
		
		
		
	}
	
	
	@Test
	public void testNonBlockingSever() throws Exception{
//		Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
//		ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);
		
		//Open Selector and Serer SocketChannel by calling the open() mehtod
		try(Selector selector = Selector.open(); 
				ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()
				){
			
			if(!(serverSocketChannel.isOpen() && selector.isOpen())){
				out.println("The server socket channel or selector cannot be opened.");
				return;
			}
			
			//Configure non-blocking mode
			serverSocketChannel.configureBlocking(false);
			
			//Set some options
			serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
			serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			
			//bind the server socket channel to port
			serverSocketChannel.bind(new InetSocketAddress(DEFAULT_PORT));
			
			//register the current channel with the given selector
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			//Display a waiting message while waiting
			out.println("Waiting for connections ...");
			
			ForkJoinPool.commonPool().execute(() -> processSelector(selector));
			
			Thread.sleep(50 * 1000);
		}
	}
	
	Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
	ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);
	
	void processSelector(Selector selector){
		try{
			while(true){
				//wait for incoming events.
				selector.select();
				
				//There is something to process on selected keys
//				Set<SelectionKey> keys = selector.selectedKeys();
				for(SelectionKey key : selector.selectedKeys()){
					
					
					if(!key.isValid()){
						continue;
					}

					if(key.isAcceptable()){
						acceptOP(key, selector);
					}else if(key.isReadable()){
						readOP(key);
					}else if(key.isWritable()){
						writeOP(key);
					}
				}
				
				selector.selectedKeys().clear();	//prevent the same key from coming up again.
				
			}
		}catch(Exception e){
			e.printStackTrace(out);
		}
	}
	
	//The key is acceptable;
	void acceptOP(SelectionKey key, Selector selector) throws IOException{
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverChannel.accept();
		socketChannel.configureBlocking(false);
		
		out.printf("Incoming connection from %s\n", socketChannel.getRemoteAddress());
		
		//write a welcome message
		socketChannel.write(ByteBuffer.wrap("Hello!\n".getBytes("UTF-8")));
		
		//register channel with selector for further I/O
		keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
		socketChannel.register(selector, SelectionKey.OP_READ);
	}
	
	
	//key.isWritable()
	void writeOP(SelectionKey key) throws IOException{
		SocketChannel socketChannel = (SocketChannel) key.channel();
		
		//
//		List<byte[]> channelData = keepDataTrack.get(socketChannel);
		
		for(byte[] data : keepDataTrack.get(socketChannel)){
			socketChannel.write(ByteBuffer.wrap(data));
		}
		keepDataTrack.remove(socketChannel);
	}
	
	//selectionKey.isReadable
	void readOP(SelectionKey key) throws IOException{
		try{
			SocketChannel socketChannel = (SocketChannel) key.channel();
			
			buffer.clear();
			
			int numRead = socketChannel.read(buffer);
			
			if(-1 >= numRead){
				keepDataTrack.remove(socketChannel);
				out.printf("Connection closed by : %s\n", socketChannel.getRemoteAddress());
				socketChannel.close();
				key.cancel();
				return;
			}
			
			buffer.flip();
			out.printf("Received message: %s from %s\n", Charset.defaultCharset().decode(buffer), socketChannel.getRemoteAddress());
			
			buffer.rewind();
			//write back to client
			doEchoJob(key, buffer.array());
		}catch(Exception e){
			e.printStackTrace(out);;
		}
	}
	
	void doEchoJob(SelectionKey key, byte[] data){
		SocketChannel socketChannel = (SocketChannel)key.channel();
		keepDataTrack.get(socketChannel).add(data);
		
		//
		key.interestOps(SelectionKey.OP_WRITE);
		
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
































