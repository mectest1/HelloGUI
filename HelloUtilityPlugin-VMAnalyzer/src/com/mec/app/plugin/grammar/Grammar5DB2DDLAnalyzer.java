package com.mec.app.plugin.grammar;

import java.io.IOException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

import com.mec.app.plugin.grammar.DB2Construct.Tablespace;
import com.mec.app.plugin.resource.MsgLogger;

public class Grammar5DB2DDLAnalyzer{

	private Grammar5DB2DDLAnalyzer(Path ddlFile, Path outputDir){
		this.ddlFile = ddlFile;
		this.outputDir = outputDir;
	}
	
	public static Grammar5DB2DDLAnalyzer inst(Path ddlFile, Path outputDir){
		Objects.requireNonNull(ddlFile);
		Objects.requireNonNull(outputDir);
		return new Grammar5DB2DDLAnalyzer(ddlFile, outputDir);
	}
	
	void prepareWorkFileTablespace(){
//		SQLStatement dropTS = new SQLStatement.DropTablespace(dbName, tablespace);
//		SQLStatement createTS = new SQLStatement.CreateTablespace(dbName, tablespace);
		
		Tablespace ts = new Tablespace(dbName, tablespace);
		logger.log(ts.getDropSQL().toString());
		logger.log(ts.getCreateSQL().toString());
	}
	
//	
//	
//	interface SQLStatement{
//		
//	}
//	
	
	public static class Lexer{
		
		public static Lexer parseFile(Path file){
			Objects.requireNonNull(file);
			Lexer retval = new Lexer();
			try {
				retval.setScanner(new Scanner(file));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return retval;
		}
		
		public static Lexer parseStr(String strToParse){
			Lexer retval = new Lexer();
			retval.setScanner(new Scanner(strToParse));
			return retval;
		}
		
		
		public String nextToken(){
			return scanner.next();
		}
		public boolean hasNextToken(){
			return scanner.hasNext();
		}
		
		private void setScanner(Scanner scanner){
			this.scanner = scanner;
		}
		private Scanner scanner;
	}
	

	public static final String dbName = "EXIMDB";
	public static final String tablespace = "EXIM";		
	private String lobTSPattern = "EXIM%s";
	
	private Path ddlFile;
	private Path outputDir;
	private MsgLogger logger = MsgLogger.defaultLogger();
	
}
