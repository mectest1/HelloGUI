package com.mec.app.plugin.grammar;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.regex.Pattern;

//import com.mec.app.plugin.grammar.Grammar5DB2DDLAnalyzer.FileLexer;
//import com.mec.app.plugin.grammar.Grammar5DB2DDLAnalyzer.Lexer;
//import com.mec.app.plugin.grammar.Grammar5DB2DDLAnalyzer.StringLexer;

public interface Lexer{
	
	static Lexer scanFile(Path file){
//		Objects.requireNonNull(file);
//		Lexer retval = new Lexer();
//		try {
//			retval.setScanner(new Scanner(file));
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//		return retval;
		return new FileLexer(file);
	}
	
	static Lexer scanStr(String strToParse){
//		Lexer retval = new Lexer();
//		retval.setScanner(new Scanner(strToParse));
//		return retval;
		return new StringLexer(strToParse);
	}
	
	static SQLFileLexer scanSQLFile(Path sqlFile){
		return new SQLFileLexer(sqlFile);
	}
	
	
	String nextToken();
	boolean hasNext();


	/**
	 * @author MEC
	 *
	 */
	/**
	 * @author MEC
	 *
	 */
	class SQLFileLexer extends FileLexer{
		private SQLFileLexer(Path sqlFile){
			super(sqlFile);
		}
		
		@Override
		protected void updateLineScanner(String line){
			super.updateLineScanner(line);
//			lineScanner.useDelimiter(CHAR_DELIMITER);	//<- break line into characters instead of words;
//			scanChar();
//			scanWord();
		}
		
		private void scanChar(){
			lineScanner.useDelimiter(CHAR_DELIMITER);
		}
		private void scanWord(){
//			lineScanner.reset();
			lineScanner.useDelimiter(WORD_DELIMITER);
		}
		
		/* 
		 * Use nextChar() or nextWord() instead;
		 * @see com.mec.app.plugin.grammar.Lexer.FileLexer#nextToken()
		 */
		@Override
		public String nextToken() {
			throw new UnsupportedOperationException();
//			return lineScanner.nextLine();
		}

//		@Override
//		public boolean hasNext() {
//			return super.hasNext();
//		}

//		public boolean hasNextLine(){
//			return fileScanner.hasNextLine();
//		}
		public void skipLine(){
			Optional<String> nextLine = nextValidLine();
			if(nextLine.isPresent()){
				updateLineScanner(nextLine.get());
			}else{
//				throw new IllegalArgumentException("No more valid line");
				lineScanner = null;
			}
		}
		

//		public String nextSQLStatementStart(){
//			
//		}
//		
//		public String nextTableName(){
//			lineScanner.
//		}
		
		public String nextWord(){
			scanWord();
			return super.nextToken();
		}
		
		public String nextNonSpaceChar(){
			scanChar();
			skipWhitespace();
			return super.nextToken();
		}
		
		public void matchChar(String ch){
//			Objects.requireNonNull(ch);
//			String nextChar = nextNonSpaceChar();
//			if(!ch.equals(nextChar)){
//				throw new IllegalArgumentException(
//						String.format("actual char %s doesn't match expected %s", nextChar, ch));
//			}
			match(this::nextNonSpaceChar, ch);
		}
		public void matchWord(String word){
//			Objects.requireNonNul
			match(this::nextWord, word);
		}
		
		
		private void match(Supplier<String> getNext, String toMatch){
			Objects.requireNonNull(toMatch);
//			String nextChar = nextNonSpaceChar();
			String nextChar = getNext.get();
			if(!toMatch.equals(nextChar)){
				throw new IllegalArgumentException(
						String.format("actual char %s doesn't match expected %s", nextChar, toMatch));
			}
		}
		
		private String lookahead;
//		private static final string 
//		private static final Pattern WORD_DELIMITER = Pattern.compile("\\p{javaWhitespace}+");
//		private static final Pattern CHAR_DELIMITER = Pattern.compile("");
	}
	
	class FileLexer implements Lexer{
	
		private FileLexer(Path file){
			Objects.requireNonNull(file);
			try {
				setFileScanner(new Scanner(file));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	//		this.file = file;
		}
		public String nextToken(){
	//		
	//		String retval = fileScanner.next();
	////		while(scanner.)
	//		return fileScanner.next();
	//		checkLine();
			
			return lineScanner.next();
		}
		public boolean hasNext(){
	//		checkLine();
	//		if(!fileScanner.hasNext()){
	//			return false;
	//		}
			
			if(null == lineScanner || !lineScanner.hasNext()){
	//			String line = fileScanner.nextLine().trim();
	//			while(isCommentLine(line)){
	//				if(fileScanner.hasNextLine()){
	//					line = fileScanner.nextLine();
	//				}
	//			}
	//			lineScanner = new Scanner(line);
	//			String line = null;
	//			for(String line = fileScanner.nextLine().trim(); 
	//					line.isEmpty()
	//					|| line.trim().startsWith(SQL_COMMENT_START);
	//				line = fileScanner.next()){
	//				if(!fileScanner.hasNext()){
	//					break;
	//				}
	//				
	//			}
				Optional<String> line = nextValidLine();
				if(line.isPresent()){
//					lineScanner = new Scanner(line.get());
					updateLineScanner(line.get());
				}else{
					return false;
				}
			}
			return fileScanner.hasNext() || lineScanner.hasNext();
		}
		
		protected Optional<String> nextValidLine(){
			if(!fileScanner.hasNextLine()){
				return Optional.empty();
			}
			String retval = fileScanner.nextLine();
			while(isCommentLine(retval) && fileScanner.hasNextLine()){
				retval = fileScanner.nextLine().trim();
			}
			if(isCommentLine(retval)){
				return Optional.empty();
			}else{
				return Optional.of(retval);
			}
		}
		
		protected void skipWhitespace(){
			if(hasNext()){
				lineScanner.skip(WORD_DELIMITER);
			}
		}
		
		protected boolean isCommentLine(String line){
			return line.isEmpty()
					|| line.trim().startsWith(SQL_COMMENT_START);
		}
		
	//	private void checkLine(){
	//		if(null == lineScanner || !lineScanner.hasNext()){
	//			String line = fileScanner.nextLine().trim();
	//			while(line.isEmpty()
	//					|| line.trim().startsWith(SQL_COMMENT_START)){
	//				line = fileScanner.nextLine();
	//			}
	//			lineScanner = new Scanner(line);
	//		}
	//	}
		
		
		private void setFileScanner(Scanner fileScanner){
			this.fileScanner = fileScanner;
		}
		protected void updateLineScanner(String line){
			lineScanner = new Scanner(line);
		}
	//	private boolean endOfFile = false;
		protected Scanner fileScanner;
	//	private Path file;
		protected Scanner lineScanner;
		private static final String SQL_COMMENT_START = "--";
		protected static final Pattern WORD_DELIMITER = Pattern.compile("\\p{javaWhitespace}+");
		protected static final Pattern CHAR_DELIMITER = Pattern.compile("");
	
	}
	
	class StringLexer implements Lexer{
		private StringLexer(String strToParse){
			scanner = new Scanner(strToParse);
		}
		
		@Override
		public String nextToken() {
			return scanner.next();
		}
	
		@Override
		public boolean hasNext(){
			return scanner.hasNext();
		}
	
	
	
		private Scanner scanner;
	}

}