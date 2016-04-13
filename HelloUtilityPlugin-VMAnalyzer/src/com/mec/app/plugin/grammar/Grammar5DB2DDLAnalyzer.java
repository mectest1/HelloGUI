package com.mec.app.plugin.grammar;

import java.nio.file.Path;
import java.util.Objects;

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
		SQLStatement dropTS = new SQLStatement.DropTablespace(dbName, tablespace);
		SQLStatement createTS = new SQLStatement.CreateTablespace(dbName, tablespace);
		
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
	
	static class Lexer{
		
	}
	

	private String dbName = "EXIMDB";
	private String tablespace = "EXIM";		
	private String lobTSPattern = "EXIM%s";
	
	private Path ddlFile;
	private Path outputDir;
	private MsgLogger logger = MsgLogger.defaultLogger();
	
}
