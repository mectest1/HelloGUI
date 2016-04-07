package com.mec.app.plugin.grammar;

import java.io.PrintStream;

import org.junit.Test;

public class GrammarTest {

	@Test
	public void test() {
//		fail("Not yet implemented");
		
		
		final String str = "9+5-2";
		
		Grammar g1 = new Grammar1();
		
		Grammar g2 = new Grammar2();
		
		g1.parse(str);
		g2.parse(str);
	}

	
	interface ParseResult{
		
	}
	
	interface Grammar{
		ParseResult parse(String str);
	}
	static class Grammar1 implements Grammar{
		@Override
		public ParseResult parse(String str){
			
			return null;
		}
	}
	
	static class Grammar2 implements Grammar{
		
		@Override
		public ParseResult parse(String str){
			return null;
		}
	}
	
	enum Terminals{
		T0(0)
		,T1(1)
		,T2(2)
		,T3(3)
		,T4(4)
		,T5(5)
		,T6(6)
		,T7(7)
		,T8(8)
		,T9(9)
		;
		
		
		
		private Terminals(int val){
			this.value = val;
		}
		
		public int getValue(){
			return value;
		}
		
		private int value;
	}
	
	private static final PrintStream out = System.out;
}
