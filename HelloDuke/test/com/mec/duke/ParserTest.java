package com.mec.duke;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

	/**
	 * ref: http://jsqlparser.sourceforge.net/
	 */
	@Test
	public void testParseSQLScript() {
		/*
		 * Test case for parsing SQL Script:
		 * purpose: Re-organize the SQL script
		 * 
		 * INPUT:
		 * select * from 
		 * 		(select A.*,row_number() over() as rn from
		 * 				(select C_INIT_FLAG,C_REF_NAME,C_REF_DESC,C_UNIT_CODE,C_TRX_STATUS,C_LOCKED_FLAG,C_REF_TYPE from EXI1.STT_TRX_REF where 1=1) 
		 * 		as A) 
		 * where rn<= 10 and rn> 0 ;
		 * 
		 * OUTPUT:
		 * 
		 * select * from 
		 * 		(select A.*,row_number() over() as rn from
		 * 				(select C_INIT_FLAG,C_REF_NAME,C_REF_DESC,C_UNIT_CODE,C_TRX_STATUS,C_LOCKED_FLAG,C_REF_TYPE from EXI1.STT_TRX_REF where 1=1) 
		 * 		as A) AS TMP2
		 * where rn<= 10 and rn> 0 ;
		 * 
		 * 
		 * Note:
		 * 1, Grammar for the SQL 
		 */
	}

	
	static class SelectStatement{
		
	}
	
	static class InsertStatement{
		
	}
	
	
	
}
