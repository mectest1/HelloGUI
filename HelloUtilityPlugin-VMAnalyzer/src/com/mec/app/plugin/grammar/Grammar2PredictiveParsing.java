package com.mec.app.plugin.grammar;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import static com.mec.app.plugin.grammar.Grammar2PredictiveParsing.Terminal.*;
/**
 * 
 * A grammar for statements in Java and C
 * 
 * stmt -> 	expr;
 * 			| if (expr) stmt
 *			| for (optexpr; optexpr; optexpr) stmt
 *			| other
 * optexpr -> {empty}
 * 			| expr
 * 
 * Reference: pseudo-code for predictive parsing
 * @author MEC
 *
 */
public class Grammar2PredictiveParsing implements Grammar {

	@Override
	public <T> ParseResult<T> parse(String str) {
		Objects.requireNonNull(str);
		try{
			strToParse = str;
			cursor = 0;
					
			stmt();
			
			return ParseResult.of(null);
		}finally{
			strToParse = "";
			cursor = 0;
		}
	}

	
	
	/**
	 * @return if terminal parsing successfully for this statement
	 */
	boolean stmt(){
		Optional<Terminal> nextTerminal = Terminal.nextTerminal(strToParse, cursor);
		if(!nextTerminal.isPresent()){
			out.println("Syntax error: there is no recognizable terminal anymore");
			return false;
		}
		switch(nextTerminal.get()){
		case EXPR:
			match(EXPR);match(COLON);break;
		case IF:
			match(IF);match(LEFT_PARENTHESIS);match(EXPR);
			match(RIGHT_PARENTHESIS);stmt();break;
		case FOR:
			match(FOR);match(LEFT_PARENTHESIS);
			optexpr();match(COLON);optexpr();match(COLON);optexpr();
			match(RIGHT_PARENTHESIS);stmt();break;
		case OTHER:
			match(OTHER);break;
		default:
			reportError(String.format("Syntax error: %s is not welcomed here.", nextTerminal.get()));
		}
		return true;
	}
	
	void optexpr(){
		if(nextIs(Terminal.EXPR)){
			match(Terminal.EXPR);
		}
	}
	
	
	void match(Terminal t){
		if(nextIs(t)){
			nextTerminal(t);
			out.println(t.getValue());
		}else{
			reportError(String.format("Syntax error: terminal %s expected", t.getValue()));
		}
	}
	
	boolean nextIs(Terminal t){
		return strToParse.startsWith(t.getValue(), cursor);
	}
	
	void nextTerminal(Terminal t){
		cursor += t.getValue().length();
	}
	
	void reportError(String errMsg){
		out.println(errMsg);
	}
	
	
	
	
	static enum Terminal{
		EMPTY("")
		,EXPR("expr")
		,COLON(";")
		,IF("if)")
		,LEFT_PARENTHESIS("(")
		,RIGHT_PARENTHESIS(")")
		,FOR("for")
		,OTHER("other")
		;
		
		
		private Terminal(String str){
			this.value = str;
		}
		
		public String getValue(){
			return value;
		}
		
		public static Optional<Terminal> nextTerminal(String str, int offset){
			Optional<Terminal> retval = Arrays.stream(Terminal.values())
					.filter(t -> Terminal.EMPTY != t)
					.filter(t -> str.startsWith(t.getValue(), offset)).findFirst();
			return retval;
		}
		
		private String value;
		
	}
	
//	private Terminal lookahead;
	private String strToParse;
	private int cursor;
	private static final PrintStream out = System.out;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

