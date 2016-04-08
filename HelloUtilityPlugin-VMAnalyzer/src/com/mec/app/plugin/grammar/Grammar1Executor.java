package com.mec.app.plugin.grammar;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;

import com.mec.app.plugin.grammar.Grammar.ParseResult;
import com.mec.app.plugin.grammar.Grammar1.Expr;

public class Grammar1Executor implements GrammarExecutor<Expr, Integer> {

	@Override
	public Integer execute(ParseResult<Expr> parseResult) {
//		int result = 0;
		
		Expr expr = parseResult.getResult();
//		while(null != expr.getExpr()){
//			
//		}
		
		return expr.evaluate();
		
	}

	
	private Deque<Integer> operandStack = new ArrayDeque<>();
	private static final PrintStream out = System.out;
}
