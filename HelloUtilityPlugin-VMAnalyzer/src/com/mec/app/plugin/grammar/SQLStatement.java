package com.mec.app.plugin.grammar;

import java.util.Formatter;

public interface SQLStatement {

	
	//----------------------------------------

	class CreateTable implements SQLStatement{
		
	}
	
	class CreateAuxiliaryTable implements SQLStatement{
		
	}
	
	class DropTable implements SQLStatement{
		
	}
	
	
	
	//----------------------------------------
	class CreateView implements SQLStatement{
		
	}
	
	class DropView implements SQLStatement{
		
	}
	
	//--------------------
	class CreateProcedure implements SQLStatement{
		
	}
	
	class DropProcedure implements SQLStatement{
		
	}
	//----------------------------------------
	class CreateTablespace implements SQLStatement{
		public CreateTablespace(String dbName, String tablespace){
			db = dbName;
			ts = tablespace;
		}
		@Override
		public String toString(){
			return String.format("CREATE TABLESPACE \"%s\" IN \"%s\" BUFFERPOOL \"%s\";"
					,ts, db, BUFFERPOOL);
		}
		
		protected String db;
		protected String ts;
		//ref: https://www.ibm.com/support/knowledgecenter/SSEPEK_10.0.0/com.ibm.db2z10.doc.comref/src/tpc/db2z_cmd_alterbufferpool.dita
		private static final String BUFFERPOOL = "BP32K";
	}
	
	class CreateLobTablespace extends CreateTablespace{
		public CreateLobTablespace(String dbName, String tablespace) {
			super(dbName, tablespace);
		}

		@Override
		public String toString() {
			return String.format("CREATE LOB TABLESPACE \"%s\" in \"%s\";" 
					,ts , db);
		}
		
	}
	
	class DropTablespace implements SQLStatement{
		public DropTablespace(String dbName, String tablespace){
			db = dbName;
			ts = tablespace;
		}
		
		@Override
		public String toString(){
//			return String.format("", db, ts);
//			return fm.format("DROP TABLESPACE \"%s\".\"%s\";", db, ts).toString();
			return String.format("DROP TABLESPACE \"%s\".\"%s\";", db, ts);
		}
		
		private String db;
		private String ts;
	}
	//----------------------------------------
	class CreateIndex implements SQLStatement{
		
	}
	
	//----------------------------------------
	class AlterTable implements SQLStatement{
		
	}
	//----------------------------------------
	class CommentOn implements SQLStatement{
		
	}
	
	class SetVariable implements SQLStatement{
		
	}
	//----------------------------------------
	//----------------------------------------
//	Formatter fm = new Formatter();
}
