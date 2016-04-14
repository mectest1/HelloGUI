package com.mec.app.plugin.grammar;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.mec.app.plugin.grammar.DB2Construct.Tablespace;
import com.mec.app.plugin.grammar.Lexer.SQLFileLexer;

public interface Parser {

	static SQLParser parseSQL(SQLFileLexer sqlLexer){
		return new SQLParser(sqlLexer);
	}
	
	ParseResult getParseResult();
	
	class SQLParser implements Parser{
		private SQLParser(SQLFileLexer sqlLexer){
			this.lexer = sqlLexer;
		}
		
		@Override
		public SQLParseResult getParseResult(){
			if(null == result){
				parseSQLStatements();
				result = SQLParseResult.of(constructs);
			}
			return result;
		}
		
		
		public void printCreateSQL(PrintStream out){
			getParseResult().getConstructs().forEach(c -> out.println(c.getCreateSQL().toString()));
		}
		
		private void parseSQLStatements(){
			while(hasMoreSQLStatement()){
				parseSQLStatement();
			}
		}
		
		private boolean hasMoreSQLStatement(){
			return lexer.hasNext();
		}
		private void parseSQLStatement(){
			String sqlTypeStr = lexer.nextWord();
			if("CREATE".equals(sqlTypeStr)){
				String createType = lexer.nextWord();
				if("TABLESPACE".equals(createType)){
//					String tablespace  = lexer.nextWord();
//					lexer.matchWord("IN");
//					String database = lexer.nextWord();
//					Tablespace ts = new Tablespace(database, tablespace);
//					constructs.add(ts);
//					
//					lexer.skipLine();
					scanTablespace();
				}else if("TABLE".equals(createType)){
					
				}else if("UNIQUE".equals(createType)){
					String indexName = lexer.nextWord();
				}
			}else if("ALTER".equals(sqlTypeStr)){
				String alterType = lexer.nextWord();
				if("TABLE".equals(alterType)){
					
				}else{
					throw new IllegalArgumentException(String.format("Unrecognizable alterType: %s", alterType));
				}
			}else{
				throw new IllegalArgumentException(String.format("Unrecognizable sqlType: %s", sqlTypeStr));
			}
		}
		
		private void scanTablespace(){
			String tablespace  = lexer.nextWord();
			lexer.matchWord("IN");
			String database = lexer.nextWord();
			Tablespace ts = new Tablespace(database, tablespace);
			constructs.add(ts);
			
			lexer.skipLine();
		}
		
		private void scanCreateTable(){
			
		}
		
		private void scanCreateTableColumn(){
			
		}
		
		private void scanAlterTable(){
			
		}
			
			
		
		private SQLFileLexer lexer;
		private SQLParseResult result;
//		private List<SQLStatement> sqlStatements;
		private List<DB2Construct> constructs = new ArrayList<>();
	}
	
	
	interface ParseResult{
		
	}
	
	
	class SQLParseResult implements ParseResult{
		private SQLParseResult(List<DB2Construct> constructs){
			this.constructs = constructs;
		}
		public static SQLParseResult of(List<DB2Construct> constructs){
			return new SQLParseResult(constructs);
		}
		List<DB2Construct> getConstructs(){
			return this.constructs;
		};
		
		private List<DB2Construct> constructs;
	}
}
