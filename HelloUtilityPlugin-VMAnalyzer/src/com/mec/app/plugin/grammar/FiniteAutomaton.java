package com.mec.app.plugin.grammar;

/**
 * Deterministic Finite Automaton
 * finite  
	fi·nite / `faɪ,naɪt / adjective
 * @author MEC
 *
 */
public interface FiniteAutomaton {
	
	
	interface DFA extends FiniteAutomaton{
		AcceptResult accept(String input);
	}
	interface NFA extends FiniteAutomaton{
		DFA toDFA();
	}
	
	
	
	
	
	class DFABase implements DFA{
		private DFABase(){
			
		}

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
		
		
		//To be implemented:
		boolean isFinalState(int state){return false;};
		char nextChar(){return '\0';};
		int move(int currentState, char inputChar){return 0;};
		
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
	
	//----------------------------------------------------------
	class NFABase implements NFA{
		private NFABase(){
			
		}

		
		public NFA fromRegExp(String regularExpression){
			return null;
		}
		
		//To be implemented
		@Override
		public DFA toDFA() {
			return null;
		}
		
	}
}
