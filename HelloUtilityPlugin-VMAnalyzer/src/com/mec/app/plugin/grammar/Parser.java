package com.mec.app.plugin.grammar;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mec.app.plugin.grammar.DB2Construct.Table;
import com.mec.app.plugin.grammar.DB2Construct.Table.Column;
import com.mec.app.plugin.grammar.DB2Construct.Tablespace;
import com.mec.app.plugin.grammar.DB2Construct.UniqueConstraintType;
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
		
		public void printCreateUsableTable(PrintStream out){
			getParseResult().getConstructs().forEach(c -> {
				Table table = (Table) c;
				out.println("--================================================");
				out.printf("-- Create table %s\n", table.getTableNameWithSchema());
				out.println("--================================================");
				table.getCreateUsableTableSQL().forEach(stmt -> {
					out.println(stmt.toString());
					out.println();
				});
				out.println("\n");
			});
		}
		
		public void printDropTableAndAuxTS(PrintStream out){
			getParseResult().getConstructs().forEach(c -> {
				Table table = (Table) c;
				out.println("--================================================");
				out.printf("-- Drop table %s\n", table.getTableNameWithSchema());
				out.println("--================================================");
//				table.getCreateUsableTableSQL().forEach(stmt -> {
				table.getDropTableAndTS().forEach(stmt -> {
					out.println(stmt.toString());
					out.println();
				});
			});
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
					scanCreateTable();
				}else if("UNIQUE".equals(createType)){
					String indexName = lexer.nextWord();
				}
			}else if("ALTER".equals(sqlTypeStr)){
				String alterType = lexer.nextWord();
				if("TABLE".equals(alterType)){
					scanAlterTable();
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
			String schemaAndTableName = lexer.nextTableStart();
			List<Column> columns = scanCreateTableColumns();
			lexer.matchWord("IN");
			String dbNameAndTablespace = lexer.nextWord();
			if(!dbNameAndTablespace.endsWith(";")){
				lexer.matchChar(";");
			}
			dbNameAndTablespace = Lexer.stripEndSemiColon(dbNameAndTablespace);
			DB2Construct.Table table = new Table(schemaAndTableName, dbNameAndTablespace);
			table.addColumns(columns);
			
			addToParseList(table);
//			constructs.add(table);
//			if(null != tables.get(schemaAndTableName){
//				thr
//			};
		}
		
		private List<Column> scanCreateTableColumns(){
			List<Column> retval = new ArrayList<>();
			int parenthesisPair = 0;
			String next = null;
//			while(true){
			while(lexer.hasNext()){
//				if(!lexer.hasNext()){
//					break;
//				}
				next = lexer.nextChar();
				if(")".equals(next)){
					if(0 == parenthesisPair){
						break;	//end of column list
					}else if(0 < parenthesisPair){
						parenthesisPair--;
					}else{
						throw new IllegalArgumentException("unmatched parenthesis");
					}
				}
				
//				else if("(".equals(next)){
//					parenthesisPair++;
//					
//				}
				
				String columnName = next.concat(lexer.nextWord()).trim();
//				StringBuilder dataTypeAndLength = new StringBuilder(lexer.nextWord());
				String dataTypeAndLength = lexer.nextWord();
				if(dataTypeAndLength.endsWith(")")){	//last column case 1;
//					Matcher lm = Lexer.LEFT_PARENTHESIS.matcher(dataTypeAndLength);
//					Matcher rm = Lexer.RIGHT_PARENTHESIS.matcher(dataTypeAndLength);
//					int leftParenthesis = 0;
//					int rightParentthesis = 0;
//					while(lm.find()) leftParenthesis++;
//					while(rm.find()) rightParentthesis++;
					int leftParenthesis = Lexer.countMatch(dataTypeAndLength, Lexer.LEFT_PARENTHESIS);
					int rightParenthesis = Lexer.countMatch(dataTypeAndLength, Lexer.RIGHT_PARENTHESIS);
//					int leftParenthesis = Lexer.LEFT_PARENTHESIS.matcher(dataTypeAndLength).groupCount();
//					int rightParentthesis = Lexer.RIGHT_PARENTHESIS.matcher(dataTypeAndLength).groupCount();
//					int rightParentthesis = Lexer.RIGHT_PARENTHESIS.matcher(dataTypeAndLength).groupCount();
					
					if(leftParenthesis == rightParenthesis){
//						Column column = new Column(columnName, dataTypeAndLength);
//						retval.add(column);
//						continue;
					}else if(1 + leftParenthesis == rightParenthesis){
						dataTypeAndLength = Lexer.stripEndParenthesis(dataTypeAndLength);
						Column column = new Column(columnName, dataTypeAndLength);
						retval.add(column);
						break;
					}else{
						throw new IllegalArgumentException("unmatched parenthesis");
					}
				}else if(dataTypeAndLength.endsWith(",")){	//end of current column
					dataTypeAndLength = Lexer.stripEndComma(dataTypeAndLength);
					Column column = new Column(columnName, dataTypeAndLength);
					retval.add(column);
					continue;
				}
				
				
				next = lexer.nextWord().trim();
				StringBuilder columnAttrs = new StringBuilder();
				while(!(next.endsWith(",") || next.endsWith(")"))){
					columnAttrs.append(next).append(" ");
					next = lexer.nextWord();
				}
				if(!next.endsWith(")")){	//simply ends with " ,", note that there is apce before ","
					Column column = new Column(columnName, dataTypeAndLength, columnAttrs.toString());
					retval.add(column);
//					lexer.skipLine();
					continue;
				}else{	//ends with ")", last column
//					Matcher m = LAST_COLUMN.matcher(columnAttrs.toString());
					String attrs = columnAttrs.toString();
					if(!attrs.trim().isEmpty()){
						attrs = Lexer.stripEndParenthesis(attrs);
					}
					Column column = new Column(columnName, dataTypeAndLength, attrs);
					retval.add(column);
					break;
				}
				
			}
			
			
			return retval;
		}
		
		private void scanAlterTable(){
			String schemaAndTableName = lexer.nextWord();
			Table table = createdTables.get(schemaAndTableName);
			UniqueConstraintType constraintType = UniqueConstraintType.PRIMARY_KEY;
			
//			lexer.mathWords(2, "ADD CONSTRAINT");;
			lexer.matchWord("ADD");
			String nextWord = lexer.nextWord();
			if("CONSTRAINT".equals(nextWord)){
				
				lexer.nextWord();				//<- ADD CONSTRAINT constraint-name
				nextWord = lexer.nextWord();	//
				if("PRIMARY".equals(nextWord)){
					lexer.matchWord("KEY");
				}else if("UNIQUE".equals(nextWord)){	//"UNIQUE"
					constraintType = UniqueConstraintType.UNIQUE_KEY;
				}else{
					throw new IllegalArgumentException(String.format("Unrecognized constraint type: %s", nextWord));
				}
			}else if("PRIMARY".equals(nextWord)){
				lexer.matchWord("KEY");
			}else{
				throw new IllegalArgumentException("Unrecognized add type");
			}
//			else{	
//				// nextWord is Primary Key name, ignored here, 
//				//since primary key name will be generated automatically
//				lexer.mathWords(2, "PRIMARY KEY");
//			}
			lexer.matchChar("(");
			String columnName = null;
			while(lexer.hasNext()){
//			for(String columnName = ; )
				columnName = lexer.nextWord();
				if(columnName.endsWith(";")){
					columnName = Lexer.stripEndSemiColon(columnName);
					if(!columnName.endsWith(")")){
						throw new IllegalArgumentException("Invalid end of primary key column list");
					}
					columnName = Lexer.stripEndParenthesis(columnName);
//					columnName = Lexer.stripQuotes(columnName);
//					table.addPrimaryKey(columnName);
					table.addUniqueConstraintKey(columnName, constraintType);
					break;
				}else if(columnName.endsWith(")")){
					columnName = Lexer.stripEndParenthesis(columnName);
//					columnName = Lexer.stripQuotes(columnName);
//					table.addPrimaryKey(columnName);
					table.addUniqueConstraintKey(columnName, constraintType);
					lexer.matchChar(";");
					break;
				}else if(columnName.endsWith(",")){
					columnName = Lexer.stripEndComma(columnName);
//					columnName = Lexer.stripQuotes(columnName);
					table.addUniqueConstraintKey(columnName, constraintType);
				}else{
					table.addUniqueConstraintKey(columnName, constraintType);
				}
//				columnName = lexer.nextWord();
			}
			
		}
//		private void addConstruct(DB2Construct construct){
//			constructs.add(construct);
//		}
		private void addToParseList(Table table){
			constructs.add(table);
			String key = table.getTableNameWithSchema();
			if(null != createdTables.get(key)){
				throw new IllegalArgumentException(String.format("table %s already exists", key));
			}else{
				createdTables.put(key, table);
			};
		}
			
			
//		private static final Pattern LAST_COLUMN = Pattern.compile("(\\w+)\\)");
		private SQLFileLexer lexer;
		private SQLParseResult result;
//		private List<SQLStatement> sqlStatements;
		private List<DB2Construct> constructs = new ArrayList<>();
		private Map<String, Table> createdTables = new HashMap<>();
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
