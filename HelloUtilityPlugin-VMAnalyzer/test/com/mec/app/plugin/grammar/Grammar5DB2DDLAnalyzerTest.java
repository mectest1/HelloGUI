package com.mec.app.plugin.grammar;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;

import com.mec.app.plugin.grammar.DB2Construct.AuxiliaryTable;
import com.mec.app.plugin.grammar.DB2Construct.Table;
import com.mec.app.plugin.grammar.DB2Construct.Table.Column;
import com.mec.app.plugin.grammar.DB2Construct.Table.ColumnDataType;
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
		}//Pattern.matches("", "");
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
	
	@Ignore
	@Test
	public void testParseTableSpace(){
		String ddlFile = "data/ddl/2016-04-14_SampleDDL.sql";
		SQLFileLexer lexer = Lexer.scanSQLFile(Paths.get(ddlFile));
		SQLParser sp = Parser.parseSQL(lexer);
		sp.printCreateSQL(out);
	}
	
	@Ignore
	@Test
	public void testParseTableSQL(){
		String ddlFile = "data/ddl/2016-04-14_SampleDDL.sql";
		SQLFileLexer lexer = Lexer.scanSQLFile(Paths.get(ddlFile));
		SQLParser sp = Parser.parseSQL(lexer);
		sp.printCreateUsableTable(out);
	}
	
	@Ignore
	@Test
	public void testParseGeneratedSQL(){
		String ddlFile = "data/ddl/2016-04-14_SampleDDL2_generated.sql";
		SQLFileLexer lexer = Lexer.scanSQLFile(Paths.get(ddlFile));
		SQLParser sp = Parser.parseSQL(lexer);
//		sp.printCreateSQL(out);
		sp.printCreateUsableTable(out);
	}
	@Ignore
	@Test
	public void testParseTableSQL3(){
		String ddlFile = "data/ddl/2016-04-14_SampleDDL3_2tables.sql";
		SQLFileLexer lexer = Lexer.scanSQLFile(Paths.get(ddlFile));
		SQLParser sp = Parser.parseSQL(lexer);
//		sp.printCreateSQL(out);
		sp.printCreateUsableTable(out);
	}
	
	
	//---------------------------------------
	@Ignore
	@Test
	public void testDefaultValuePattern(){
		String colAttr = "NOT NULL WITH DEFAULT '0000000000 '";
		Matcher m = Column.WITH_DEFAULT_STR.matcher(colAttr);
		m.matches();
		out.println(m.group(1));
	}
//	@Ignore
	@Test
	public void testParseTableSQL5(){
		String ddlFile = "";
//		String ddlFile = "data/ddl/2016-04-14_SampleDDL5_original1~1000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original1000~2000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original2000~6000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original6000~10000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original10000~20000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original20000~30000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original30000~40000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original40000~50000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original50000~60000.sql";
		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original00001~60000.sql";

		Path inputDDL = Paths.get(ddlFile);
		SQLFileLexer lexer = Lexer.scanSQLFile(inputDDL);
		SQLParser sp = Parser.parseSQL(lexer);
//		sp.printCreateUsableTable(out);
		
		String inputDDLFileName = inputDDL.getFileName().toString();
		Path createDDL = inputDDL.resolveSibling(
				inputDDLFileName.substring(0, inputDDLFileName.lastIndexOf("."))
				+ "_create.sql");
		try(PrintStream ddlOutput = new PrintStream(Files.newOutputStream(createDDL))){
			sp.printCreateUsableTable(ddlOutput);
		}catch(Exception e){
			e.printStackTrace(out);
		}
	}
	
	@Ignore
	@Test
	public void testParseTableSQL5DropTable(){
		String ddlFile = "";
//		String ddlFile = "data/ddl/2016-04-14_SampleDDL5_original1~1000.sql";
//		String ddlFile = "data/ddl/2016-04-14_SampleDDL5_original1000~2000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original2000~6000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original6000~10000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original10000~20000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original20000~30000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original30000~40000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original40000~50000.sql";
//		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original50000~60000.sql";
		ddlFile = "data/ddl/2016-04-14_SampleDDL5_original00001~60000.sql";
		SQLFileLexer lexer = Lexer.scanSQLFile(Paths.get(ddlFile));
		SQLParser sp = Parser.parseSQL(lexer);
//		sp.printDropTableAndAuxTS(out);

		Path inputDDL = Paths.get(ddlFile);
		String inputDDLFileName = inputDDL.getFileName().toString();
		Path createDDL = inputDDL.resolveSibling(
				inputDDLFileName.substring(0, inputDDLFileName.lastIndexOf("."))
				+ "_drop.sql");
		try(PrintStream ddlOutput = new PrintStream(Files.newOutputStream(createDDL))){
			sp.printDropTableAndAuxTS(ddlOutput);
		}catch(Exception e){
			e.printStackTrace(out);
		}
	}
	
	private static final PrintStream out = System.out;
	private static final Path ddlFile = Paths.get("data/ddl/BPIDB_Look.ddl_original.sql");
	private static final Path outputDir = Paths.get("data/ddl-output/");
}
