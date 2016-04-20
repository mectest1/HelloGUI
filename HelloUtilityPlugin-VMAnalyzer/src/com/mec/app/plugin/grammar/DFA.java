package com.mec.app.plugin.grammar;

/**
 * Deterministic Finite Automaton
 * finite  
	fi·nite / `faɪ,naɪt / adjective
 * @author MEC
 *
 */
public interface DFA {

	AcceptResult accept(String input);
	
	
	abstract class DFABase implements DFA{

		@Override
		public AcceptResult accept(String input) {
			int s = START_STATE;
//			char c = nextChar();
//			while(EOF != c){
//				s = move(s, c);
//				c = nextChar();
//			}
			for(char c = nextChar(); EOF != c; c = nextChar()){
				s = move(s, c);
			}
			if(isFinalState(s)){
				return AcceptResult.YES;
			}else{
				return AcceptResult.NO;
			}
		}
		
		
		abstract boolean isFinalState(int state);
		abstract char nextChar();
		abstract int move(int currentState, char inputChar);
		
//		int state;
		char c;
		static final char START_STATE = '\0';
		static final char EOF = '\0';
		
	}
	
	/**
	 * Accept state from this DFA
	 * @author MEC
	 *
	 */
	enum AcceptResult{
		YES
		,NO
		;
		
		
	}
}
