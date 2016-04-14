package com.mec.app.plugin.grammar;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;

import com.mec.app.plugin.grammar.DB2Construct.AuxiliaryTable;
import com.mec.app.plugin.grammar.DB2Construct.Table;
import com.mec.app.plugin.grammar.DB2Construct.Table.Column;
import com.mec.app.plugin.grammar.DB2Construct.Table.ColumnDataTypeAndLength;
import com.mec.app.plugin.grammar.Lexer.SQLFileLexer;
import com.mec.app.plugin.grammar.Parser.SQLParser;

public class Grammar5DB2DDLAnalyzerTest {

	@Ignore
	@Test
	public void testExtractTableInfo() {
		Grammar5DB2DDLAnalyzer g5 = Grammar5DB2DDLAnalyzer.inst(ddlFile, outputDir);
		g5.prepareWorkFileTablespace();
	}
	
	
	@Ignore
	@Test
	public void testCreateTable(){
		Table table = new Table("\"EXIMMETA\".\"ECD_APROC_MGR\"", "\"EXIMDB\".\"EXIM\"");
//		Table table = new Table("EXIMMETA", "ECD_APROC_MGR", "EXIMDB", "EXIM");
		out.println(table.getCreateSQL().toString());
		
		table = new Table("\"EXIMMETA\".\"ECD_APROC_MGR\"", "\"EXIMMETA\"");
		out.println(table.getCreateSQL().toString());
		
		
		table = new Table("\"ECD_APROC_MGR\"", "\"EXIMMETA\"");
		out.println(table.getCreateSQL().toString());
		
	} 
	
	@Ignore
	@Test
	public void testCreateTableWithColumns(){
		Table table = new Table("EXIMMETA", "ECD_APROC_MGR", "EXIMDB", "EXIM");
		
		table.addColumn(new Column("C_BEF_DAYS_FLD", "VARCHAR(18)"));
		table.addColumn(new Column("C_CAL", "CLOB(524288)"));
		table.addColumn(new Column("C_CATA", "VARCHAR(4000)"));
		table.addColumn(new Column("C_CATA_TYPE", "VARCHAR(1)", "NOT NULL WITH DEFAULT 'S'"));
		table.addColumn(new Column("C_FUNC_ID", "VARCHAR(18)"));
		table.addColumn(new Column("C_SCHEMA_TYPE", "VARCHAR(1)", " NOT NULL"));
		table.addColumn(new Column("C_STARTBYMANUAL", "VARCHAR(1)"));
		table.addColumn(new Column("C_START_FLAG", "VARCHAR(1)", "NOT NULL"));
		table.addColumn(new Column("C_TASK_COMP", "VARCHAR(48)"));
		table.addColumn(new Column("C_TASK_DESC", "VARCHAR(64)"));
		table.addColumn(new Column("C_TASK_ID", "VARCHAR(16)", "NOT NULL"));
		table.addColumn(new Column("C_TASK_NAME", "VARCHAR(48)", "NOT NULL"));
		table.addColumn(new Column("C_TASK_VIEW", "VARCHAR(18)"));
		table.addColumn(new Column("I_BEFORE_DAYS", "INTEGER"));
		table.addColumn(new Column("I_DAY", "INTEGER"));
		table.addColumn(new Column("I_HOUR", "INTEGER"));
		table.addColumn(new Column("I_MINUTE", "INTEGER"));
		table.addColumn(new Column("I_MONTH", "INTEGER"));
		table.addColumn(new Column("I_SCHEDULE_TYPE", "VARCHAR(1)", "NOT NULL"));
		table.addColumn(new Column("I_WEEK", "INTEGER"));
//		table.addColumn(new Column("", ""));
//		table.addColumn(new Column("", ""));
		
		
		out.println(table.getCreateSQL().toString());
		
	}
	
	@Ignore
	@Test
	public void testAuxiliaryTSNames(){
		IntStream.range(0, 10).forEach(i -> out.println(AuxiliaryTable.getNextLobTS()));
	}
	
	@Ignore
	@Test
	public void testLexer(){
		Lexer lexer = Lexer.scanStr("\"C_CAL\" CLOB(524288) ,");
		while(lexer.hasNext()){
			out.println(lexer.nextToken());
		}
		out.println("-------------------------------------------");
		lexer = Lexer.scanStr("\"C_CATA_TYPE\" VARCHAR(1) NOT NULL WITH DEFAULT 'S',");
		while(lexer.hasNext()){
			out.println(lexer.nextToken());
		}
		out.println("-------------------------------------------");
		lexer = Lexer.scanStr("\"ID\" DECIMAL(22 ,        0) NOT NULL ) ");
		while(lexer.hasNext()){
			out.println(lexer.nextToken());
		}
	}
	
	@Ignore
	@Test
	public void testColumnDataTypeAndLength(){
		String desc = "DECIMAL(24,4)";
		ColumnDataTypeAndLength dt = ColumnDataTypeAndLength.of(desc);
		out.println(dt);
		
		desc = "DATE";
		dt = ColumnDataTypeAndLength.of(desc);
		out.println(dt);
		
		
		desc = "VARCHAR(35)";
		dt = ColumnDataTypeAndLength.of(desc);
		out.println(dt);
		
		
		desc = "VARCHAR(35)";
		dt = ColumnDataTypeAndLength.of(desc);
		out.println(dt);
		
		
		desc = "DOUBLE";
		dt = ColumnDataTypeAndLength.of(desc);
		out.println(dt);
		
		
		desc = "CLOB(12345)";
		dt = ColumnDataTypeAndLength.of(desc);
		out.println(dt);
		
		
		desc = "DECIMAL(10,2)";
		dt = ColumnDataTypeAndLength.of(desc);
		out.println(dt);
	}
	
	@Ignore
	@Test
	public void testScanCommentLine(){
		String str = "--Running the DDL below will explicitly create a schema in the";
		Lexer lexer = Lexer.scanStr(str);
		while(lexer.hasNext()){
			out.println(lexer.nextToken());
		}
	}
	
	@Ignore
	@Test
	public void testScanFile(){
		String ddlFile = "data/ddl/2016-04-14_SampleDDL.sql";
		Lexer lexer = Lexer.scanFile(Paths.get(ddlFile));
		while(lexer.hasNext()){
			out.println(lexer.nextToken());
		}
	}
	@Ignore
	@Test
	public void testScanSQLFile(){
		String ddlFile = "data/ddl/2016-04-14_SampleDDL.sql";
		SQLFileLexer lexer = Lexer.scanSQLFile(Paths.get(ddlFile));
		while(lexer.hasNext()){
			out.println(lexer.nextToken());
		}
//		SQLFileLexer lexer = Lexer.scanSQLFile(Paths.get(ddlFile));
//		while(lexer.hasNext()){
//			out.println(lexer.nextToken());
//		}
	}
	
	
	@Test
	public void testParseTableSpace(){
		String ddlFile = "data/ddl/2016-04-14_SampleDDL.sql";
		SQLFileLexer lexer = Lexer.scanSQLFile(Paths.get(ddlFile));
		SQLParser sp = Parser.parseSQL(lexer);
		sp.printCreateSQL(out);
	}
	
	private static final PrintStream out = System.out;
	private static final Path ddlFile = Paths.get("data/ddl/BPIDB_Look.ddl_original.sql");
	private static final Path outputDir = Paths.get("data/ddl-output/");
}
