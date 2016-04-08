package com.mec.app.plugin.grammar;


public interface Grammar{
	<T> ParseResult<T> parse(String str);
	
	

	public static interface ParseResult<T>{
		T getResult();
		
		/**
		 * Simply encapsulate <code>value</code> into {@link ParseResult} and return it
		 * when {@link ParseResult#getResult()} is called.
		 * @param value the encapsulated value
		 * @return
		 */
		static <T> ParseResult<T> of(final T value){
			return new ParseResult<T>() {
				@Override
				public T getResult() {
					return value;
				}
				
			};
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

}