package com.mec.app.plugin.grammar;

import java.io.PrintStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * Extracted from the dragon book section 2.6: Lexical Analysis
 * expr -> 		expr + term {print('+')}
 * 			|	expr - term {print('-')}
 * 			|	term
 * term -> 		term * factor {print('*')}
 * 			|	term / factor {print('/')}
 * 			factor
 * factor -> 	( expr )
 * 			| num	{print(num.value)}
 * 			| id	{print(id.lexeme)}
 * 
 * 
 * After eliminating left-recursion:
 * 
 * @author MEC
 *
 */
public class Grammar4 implements Grammar {

	@Override
	public <T> ParseResult<T> parse(String str) {
		
		
		return null;
	}

		
	
	
	static abstract class Token{
		TokenType tag;
		public Token(TokenType tag){this.tag = tag;}
		@Override
		public String toString(){
			return String.format("<%s, >", tag.name());
		}
	}
	
	static class Num extends Token{
		public final int value;
		public Num(int value){
			super(TokenType.NUM);
			this.value = value;
		}
		@Override
		public String toString(){
			return String.format("<%s, %s>", tag.name(), value);
		}
	}
	
	static class Word extends Token{
		public final String lexeme;
		public Word(TokenType tag, String lexeme){
			super(tag);
			this.lexeme = lexeme;
		}
		public Word(String lexeme){
			this(TokenType.ID, lexeme);
		}
		@Override
		public String toString(){
			return String.format("<%s, %s>", tag.name(), lexeme);
		}
	}
	
	static class Operator extends Token{
		public String op;
		public Operator(String op){
			super(TokenType.OP);
			this.op = op;
		}
		@Override
		public String toString(){
			return String.format("<%s, %s>", tag.name(), op);
		}
	}
	
	static class Parser{
		public Parser(){
			
		}
		
	}
	
	static class Lexer{
		public Lexer(String str){
			this();
			Objects.requireNonNull(str);
			this.strToParse = str;
		}
		void reserve(Word t){
			words.put(t.lexeme, t);
		}
		
		private Lexer(){
			reserve(new Word(TokenType.TRUE, TokenType.TRUE.getLexeme()));
			reserve(new Word(TokenType.FALSE, TokenType.FALSE.getLexeme()));
		}
		
//		public Token scan(){
		public Optional<Token> nextToken(){
//			initChar();
			if(0 >= cursor){
				nextChar();
			}
			
//			ignoreWhitespace();
			for(;;nextChar()){
				if(SPACE == peek	//Ignore white spaces;
					|| TAB == peek){
					continue;
				}else if(NEWLINE == peek){
					line++;
				}else{
					break;
				}
			}
			
			if(cursor >= strToParse.length()
				&& EMPTY_CHAR == peek){
//				out.println("Not more tokens");
				return Optional.empty();
			}
			
			Token retval = null;
			if(Character.isDigit(peek)){
				StringBuilder intval = new StringBuilder(peek);
				for(;Character.isDigit(peek);
//					peek = strToParse.charAt(cursor++)){
					nextChar()){
					intval.append(peek);
				}
				retval = new Num(Integer.parseInt(intval.toString()));
			}else if(Character.isLetter(peek)){
				StringBuilder idval = new StringBuilder(peek);
				for(;
					Character.isLetterOrDigit(peek);
//					peek = strToParse.charAt(cursor++)){
					nextChar()){
					idval.append(peek);
				}
//				retval = words.computeIfAbsent(idval.toString(), identifier -> new Word(TokenType.ID, identifier));
				retval = words.computeIfAbsent(idval.toString(), Word::new);
			}else{
				retval = new Operator(String.valueOf(peek));
				nextChar();
			}
			return Optional.of(retval);
		}
		
		private void initChar(){
			if(0 >= cursor){
				nextChar();
			}
		}
		private void ignoreWhitespace(){
			for(;;nextChar()){
				if(SPACE == peek	//Ignore white spaces;
					|| TAB == peek){
					continue;
				}else if(NEWLINE == peek){
					line++;
				}else{
					break;
				}
			}
		}
		private boolean hasMoreChar(){
			if(cursor < strToParse.length()){
				return true;
			}else{
				out.printf("No character in input string anymore: %s\n", strToParse);
				return false;
			}
		}
		private void nextChar(){
//			if(!hasMoreChar()){
//				peek = EMPTY_CHAR;
//				return;
//			}
//			peek = strToParse.charAt(cursor++);
			if(cursor < strToParse.length()){
				peek = strToParse.charAt(cursor++);
			}else{
				peek = EMPTY_CHAR;
//				out.printf("No character in input string anymore: %s\n", strToParse);
			}
		}
		
		private StringReader sr;
		private String strToParse;
		private int cursor = 0;
		private int line = 1;
		private char peek = EMPTY_CHAR;	//the lookahead char
		private HashMap<String, Token> words = new HashMap<>();
		
		private static final char EMPTY_CHAR = '\0';
		private static final char SPACE = ' ';
		private static final char TAB = '\t';
		private static final char NEWLINE = '\n';
		private static final PrintStream out = System.out;
	}
	
	enum TokenType{
		NUM
		,ID
		,OP
		,TRUE("true")
		,FALSE("false")
		;
		private TokenType(){
			lexeme = this.name();
		}
		
		private TokenType(String val){
			lexeme = val;
		}
		
		public String getLexeme(){
			return lexeme;
		}
		
		
		private String lexeme;
	}
	
}
