package com.mec.duke;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Ignore;
import org.junit.Test;

public class ChannelTest {

	@Ignore
	@Test
	public void testReadFile() throws Exception{
//		fail("Not yet implemented");
		Path path = Paths.get("test", this.getClass().getName().replaceAll("\\.", "/") + ".java");
		
		try(SeekableByteChannel seekableByteChannel = Files.newByteChannel(path)){
			ByteBuffer buffer = ByteBuffer.allocate(12);
			String encoding = System.getProperty("file.encoding");
			
			
			for(buffer.clear(); 0 < seekableByteChannel.read(buffer) ; ){
				buffer.flip();
				out.print(Charset.forName(encoding).decode(buffer));
				buffer.clear();
			}
		}
	}
	
	@Ignore
	@Test
	public void testWriteFile() throws Exception{
		Path p = Paths.get("testFile.txt");
		
		//write a file using SeekableByteChannel
		try(SeekableByteChannel seekableByteChannle = Files.newByteChannel(p, StandardOpenOption.WRITE	
				, StandardOpenOption.CREATE	//Without this, will get "NoSuchFileException
//				, StandardOpenOption.APPEND	//Without this flag, new content will be written into beginning of the text 
				);){
			
			ByteBuffer str = ByteBuffer.wrap("Test Text".getBytes());
			
			final int size = 10;
//			ByteBuffer lineNumber = ByteBuffer.allocate(size);
			int[] lineNumber = new int[size];
			for(int i = 0; i < size; ++i){
//				lineNumber.putInt(i);
				lineNumber[i] = i;
			}
			ByteBuffer newline = ByteBuffer.wrap("\n".getBytes());
			
			for(int i = 0; i < size; ++i){
				seekableByteChannle.write(ByteBuffer.wrap(String.valueOf(i).getBytes()));
				seekableByteChannle.write(str);
				seekableByteChannle.write(newline);
			}
		}
		
		
		out.printf("Read from file: %s\n", p);
		Files.readAllLines(p).forEach(out::println);
		//Output: LOL
//		0Test Text
//		123456789

		
//		Thread.sleep(2*1000);
//		Files.deleteIfExists(p);
	}

//	@Ignore
	@Test
	public void testWriteFile2() throws Exception{
		Path p = Paths.get("data/ChannelWrittenFileTest.txt");
		
		out.printf("Read from file: %s\n", p);
		//write a file using SeekableByteChannel
		try(ReadableByteChannel readableChannel = Files.newByteChannel(p
				);){
			
			ByteBuffer buffer = ByteBuffer.allocate(12);
			buffer.clear();
			
			String encoding = System.getProperty("file.encoding");
			while(0 < readableChannel.read(buffer)){
				buffer.flip();
				out.print(Charset.forName(encoding).decode(buffer));
				buffer.clear();
			}
			
		}
		
		
//		out.printf("Read from file: %s\n", p);
//		Files.readAllLines(p).forEach(out::println);
		//Output: LOL
//		0Test Text
//		123456789
		
		
//		Thread.sleep(2*1000);
//		Files.deleteIfExists(p);
	}
	
	
//	@Test
//	public void 
	
	private static final PrintStream out = System.out;
}







































