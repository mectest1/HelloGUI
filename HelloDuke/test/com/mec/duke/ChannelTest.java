package com.mec.duke;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	@Ignore
	@Test
	public void testReadFile2() throws Exception{
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
	
	@Ignore
	@Test
	public void testWriteFile2() throws Exception{
		Path p = Paths.get("data/ChannelWrittenFileTest.txt");
		
		try(WritableByteChannel writableChannel = Files.newByteChannel(p, StandardOpenOption.CREATE
				, StandardOpenOption.APPEND
//				, StandardOpenOption.WRITE	//<- without this flag, the content will still be appended
				);){
			
			ByteBuffer content = ByteBuffer.wrap(("\nHello from WritableByteChannel @" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)).getBytes());
//			content.flip();	//<- After initialization, position = 0; limit = capacity;
							//in that case, ByteBuffer.flip() will set limit to 0;
							//which will result into 0 bytes to be written into file.
			int writtenBytes = writableChannel.write(content);
			out.printf("%s bytes have been written into file.\n", writtenBytes);
			out.println();
			
			out.printf("Read from file %s\n", p);
			Files.readAllLines(p).forEach(out::println);
		}
		
	}
	
	@Ignore
	@Test
	public void readCharFromDifferentPositions() throws Exception{
		Path p = Paths.get("test", getClass().getName().replaceAll("\\.", "/") + ".java");
//		Path p = Paths.get("data/ChannelWrittenFileTest.txt");
		String encoding = System.getProperty("file.encoding");
		Charset charset  = Charset.forName(encoding);
		//
		ByteBuffer buffer = ByteBuffer.allocate(1);	//read exact one character from different positions:
													//begin, middle, and end position;
		try(SeekableByteChannel seekableByteChannel = Files.newByteChannel(p, StandardOpenOption.READ)){
			//output the whole file
//			for(buffer.clear(); 0 < seekableByteChannel.read(buffer); buffer.clear()){
//				buffer.flip();
//				out.print(charset.decode(buffer));
//			}
			
			
			
			
			//The initial position
			out.printf("Read first character from file %s\n", p);
			
			//
			seekableByteChannel.read(buffer);
			buffer.flip();
			out.println(Charset.forName(encoding).decode(buffer));
			buffer.rewind();
			
			//get into the middle
			seekableByteChannel.position(seekableByteChannel.size() / 2);
			out.printf("Now read the middle character from file %s, position: %s\n", p, seekableByteChannel.position());
			
			seekableByteChannel.read(buffer);
			buffer.flip();	//<--Don't forget this
			out.println(Charset.forName(encoding).decode(buffer));
			buffer.clear();
			
			//get the last character
			seekableByteChannel.position(seekableByteChannel.size() - 1);
			out.printf("Now read the last character from file %s, position: %s\n", p, seekableByteChannel.position());
			
			seekableByteChannel.read(buffer);
			buffer.flip();	//<---Don't forget this, or you'll simply get an empty character
			out.println(Charset.forName(encoding).decode(buffer));
			buffer.clear();
			
			
			
		}
		
		
		//
	}
	
	@Ignore
	@Test
	public void testWriteInDifferentPositions() throws Exception{
		Path p = Paths.get("data/ChannelWrittenFileTest.txt");
		
		try(SeekableByteChannel seekableByteChannel = Files.newByteChannel(p
//				, StandardOpenOption.READ		//READ + APPEND  not allowed, LOL
				, StandardOpenOption.CREATE
				, StandardOpenOption.WRITE
//				, StandardOpenOption.APPEND		//When APPEND is set, the channel position cannot be set
				)){
			
			String content1 = "\nDerp Derp2  Derp";
			ByteBuffer buffer1 = ByteBuffer.wrap(content1.getBytes());
			
			String content2 = "Derpia";
			ByteBuffer buffer2 = ByteBuffer.wrap(content2.getBytes());
			
			//
			//locate position to the end;
			seekableByteChannel.position(seekableByteChannel.size());	//No need for APPEND flag
			
			while(buffer1.hasRemaining()){
				seekableByteChannel.write(buffer1);
			}
			
			int replaceIndex = content1.indexOf("Derp2");
			
			//Set the channel's position to the text that needs to be replaced
			long replacePosition = seekableByteChannel.size() - (buffer1.capacity() - replaceIndex);
			seekableByteChannel.position(replacePosition);
				//<- position set failed for APPEND
				//Q: position cannot be set for APPEND flag?
				//A: Yep. Add a breakpoint here and watch for seekableByteCHannel.position() before and after this line.
				//	Then clear the APPEND flag and try again.
			
			
			while(buffer2.hasRemaining()){
				seekableByteChannel.write(buffer2);
			}
			
			buffer1.clear();
			buffer2.clear();
			
			//
			out.printf("Read content from file %s\n", p);
			Files.readAllLines(p).forEach(out::println);
			
		}
	}
	
	@Ignore
	@Test
	public void testCopyBeginningContentToLast() throws Exception{
		Path p = Paths.get("data/ChannelWrittenFileTest.txt");
		
		ByteBuffer buffer = ByteBuffer.allocate(25);
		buffer.put("\n".getBytes());
		try(SeekableByteChannel seekableByteChannel = Files.newByteChannel(p
				, StandardOpenOption.READ
				, StandardOpenOption.WRITE
				)){
			for(int readedBytes = 0; -1 < readedBytes && buffer.hasRemaining();){
				readedBytes = seekableByteChannel.read(buffer);
			}
			
			//
			buffer.flip();
			
			//
			seekableByteChannel.position(seekableByteChannel.size());
			while(buffer.hasRemaining()){
				seekableByteChannel.write(buffer);
			}
			
			out.printf("Read from file %s\n", p);
			Files.readAllLines(p).forEach(out::println);
		}
	}
	
	@Ignore
	@Test
	public void testTruncateFile() throws Exception{
		Path p = Paths.get("data/ChannelWrittenFileTest.txt");
		
		String content = "\nHello fro from truncate@" + LocalDateTime.now();
		
		
		ByteBuffer buffer = ByteBuffer.wrap(content.getBytes());
		
		//
		try(SeekableByteChannel seekableByteChannel = Files.newByteChannel(p
//				, StandardOpenOption.READ	//This flag conflicts with APPEND
				, StandardOpenOption.WRITE
//				, StandardOpenOption.APPEND	//
				)){
			
			//Ref: API DOC:
//			An implementation of this interface may prohibit truncation when connected to an entity, 
//			typically a file, opened with the APPEND option.
			
			seekableByteChannel.truncate(seekableByteChannel.size() - buffer.capacity());
				//Seems in JDK 8 on Windows, the channel can still be truncated 
				//even when the APPEND flag is set.
			
			seekableByteChannel.position(seekableByteChannel.size());
			while(buffer.hasRemaining()){
				seekableByteChannel.write(buffer);
			}
			
			//
			out.printf("Read from file: %s\n", p);
			Files.readAllLines(p).forEach(out::println);
		}
	}
	
	
	@Ignore
	@Test
	public void testCopyThroughChannel() throws Exception{
		Path p = Paths.get("data/ChannelWrittenFileTest.txt");
		Path copyTo = p.resolveSibling("copy-to-file.txt");
		
		ByteBuffer buffer = ByteBuffer.allocate(4 * 1024);	//buffer capacity: 4KB
//		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 1024);
		
//		try(ReadableByteChannel reader = Files.newByteChannel(p, StandardOpenOption.READ);
//				WritableByteChannel writer = Files.newByteChannel(copyTo, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		try(FileChannel reader = FileChannel.open(p, StandardOpenOption.READ);
				FileChannel writer = FileChannel.open(copyTo, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
				){
			out.printf("Copy from %s to %s\n", p, copyTo);
			//
			for(buffer.rewind(); 0 < reader.read(buffer); buffer.clear()){
				buffer.flip();	//don't forget to flip the buffer after reading
				while(buffer.hasRemaining()){
					writer.write(buffer);
				}
			}
			
			//
			Thread.sleep(2 * 1000);
			out.printf("Read from file %s\n", copyTo);
			
			Files.readAllLines(copyTo).forEach(out::println);
			
			
			//
			Thread.sleep(2 * 1000);
			out.printf("Delete file %s\n", copyTo);
			Files.deleteIfExists(copyTo);
		}

	}
	
	
	@Ignore
	@Test
	public void testCopyThroughChannel2() throws Exception{
		Path p = Paths.get("data/ChannelWrittenFileTest.txt");
		Path copyTo = p.resolveSibling("copy-to-file.txt");
		
//		try(ReadableByteChannel reader = Files.newByteChannel(p, StandardOpenOption.READ);
//				WritableByteChannel writer = Files.newByteChannel(copyTo, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		try(FileChannel reader = FileChannel.open(p, StandardOpenOption.READ);
				FileChannel writer = FileChannel.open(copyTo, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
				){
			out.printf("Copy from %s to %s\n", p, copyTo);

			writer.transferFrom(reader, 0, reader.size());	//<-- transfer into writer from reader
//			reader.transferTo(0, reader.size(), writer);	//<-- or from the reader transfered to writer;
			
			//
			Thread.sleep(2 * 1000);
			out.printf("Read from file %s\n", copyTo);
			
			Files.readAllLines(copyTo).forEach(out::println);
			
			
			//
			Thread.sleep(2 * 1000);
			out.printf("Delete file %s\n", copyTo);
			Files.deleteIfExists(copyTo);
		}
		
	}
	
	
	@Test
	public void testCopyThroughChannel3() throws Exception{
		Path p = Paths.get("data/ChannelWrittenFileTest.txt");
		Path copyTo = p.resolveSibling("copy-to-file.txt");
		
//		try(ReadableByteChannel reader = Files.newByteChannel(p, StandardOpenOption.READ);
//				WritableByteChannel writer = Files.newByteChannel(copyTo, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		try(FileChannel reader = FileChannel.open(p, StandardOpenOption.READ);
				FileChannel writer = FileChannel.open(copyTo, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
				){
			out.printf("Copy from %s to %s\n", p, copyTo);
			
			MappedByteBuffer buffer = reader.map(MapMode.READ_ONLY, 0, reader.size());
			writer.write(buffer);
			
			
			//
			Thread.sleep(2 * 1000);
			out.printf("Read from file %s\n", copyTo);
			
			Files.readAllLines(copyTo).forEach(out::println);
			
			
			//
			Thread.sleep(2 * 1000);
			out.printf("Delete file %s\n", copyTo);
			Files.deleteIfExists(copyTo);
		}
		
	}
	
	private static final PrintStream out = System.out;
}




















//A QUICK BROWN FOX JUMPS OVER THE LAZY DOG