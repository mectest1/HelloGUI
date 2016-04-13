package com.mec.app.plugin.grammar;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class Grammar5DB2DDLAnalyzerTest {

	@Test
	public void testExtractTableInfo() {
		Grammar5DB2DDLAnalyzer g5 = Grammar5DB2DDLAnalyzer.inst(ddlFile, outputDir);
		g5.prepareWorkFileTablespace();
	}
	
	
	
	
	private static final PrintStream out = System.out;
	private static final Path ddlFile = Paths.get("data/ddl/BPIDB_Look.ddl_original.sql");
	private static final Path outputDir = Paths.get("data/ddl-output/");
}
