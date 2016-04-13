package com.mec.app.plugin.grammar;

import java.io.PrintStream;
import java.util.Objects;

public class Grammar3 implements Grammar {

	@Override
	public <T> ParseResult<T> parse(String str) {
		Objects.requireNonNull(str);
		
		try{
			strToParse = str;
			cursor = 0;
			lookahead = strToParse.charAt(cursor);
			
			expr();
		}finally{
			strToParse = "";
			cursor = 0;
			lookahead = EMPTY_CHAR;
		}
		
		return null;
	}

	void expr(){
		term();
		rest();
	}
	
	
	void rest(){
		if('+' == lookahead){
			match('+');
			term();
			print('+');
			rest();
		}else if('-' == lookahead){
			match('-');
			term();
			print('-');
			rest();
		}else{
			//do nothing with the input
		}
	}
	
	void term(){
//		char lookahead = strToParse.charAt(cursor);
		if(Character.isDigit(lookahead)){
			char t = lookahead;
			match(lookahead);
			print(t);
		}else{
			report("syntax error");
		}
		
	}
	
	void match(char ch){
		if(ch != strToParse.charAt(cursor)){
//		if(lookahead != strToParse.charAt(cursor)){
			throw new ParseException(String.format("%s expected at index %s for %s", ch, cursor, strToParse));
		}
//		if(cursor < strToParse.length() - 1){
//			lookahead = strToParse.charAt(++cursor);
//		}
		if(1 + cursor < strToParse.length()){
			++cursor;
		}
		lookahead = strToParse.charAt(cursor);
	}
	
	void print(char ch){
		out.print(ch);
	}
	
	void report(String msg){
		out.println(msg);
	}
	
	static class ParseException extends IllegalArgumentException{
		public ParseException(){
			
		}
		public ParseException(String msg){
			super(msg);
		}
	}
	
	private static final char EMPTY_CHAR = '\0';
	private char lookahead;
	private int cursor;
	private String strToParse;
	private static final PrintStream out = System.out;
}
