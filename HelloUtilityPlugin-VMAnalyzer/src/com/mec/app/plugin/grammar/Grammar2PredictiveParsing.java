package com.mec.app.plugin.grammar;

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
		// TODO Auto-generated method stub
		return null;
	}

	
	
	void stmt(){
		
	}
	
	void optexpr(){
		
	}
	
	
	void match(Terminal t){
		if(nextIs(t)){
			nextTerminal();
		}else{
			reportError("Syntax error");
		}
	}
	
	boolean nextIs(Terminal t){
		return false;
	}
	
	void nextTerminal(){
		
	}
	
	void reportError(String errMsg){
		
	}
	
	static enum Terminal{
		EMPTY
		,EXPR
		,COLON
		,IF
		,LEFT_PARENTHESIS
		,RIGHT_PARENTHESIS
		,FOR
		,OTHER
		;
		
	}
	
	private Terminal lookahead;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

