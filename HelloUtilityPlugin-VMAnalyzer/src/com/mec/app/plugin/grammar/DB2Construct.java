package com.mec.app.plugin.grammar;

import com.mec.app.plugin.grammar.SQLStatement.CreateTable;
import com.mec.app.plugin.grammar.SQLStatement.CreateTablespace;
import com.mec.app.plugin.grammar.SQLStatement.DropTable;
import com.mec.app.plugin.grammar.SQLStatement.DropTablespace;

public interface DB2Construct {

	SQLStatement getCreateSQL();
	
	SQLStatement getDropSQL();
	
	
	
	//-----------------------------------------
	class Table implements DB2Construct{

		@Override
		public CreateTable getCreateSQL() {
			return null;
		}

		@Override
		public DropTable getDropSQL() {
			return null;
		}
	}
	
	
	
	
	
	
	
	//-----------------------------------------
	class Tablespace implements DB2Construct{

		public Tablespace(String dbName, String tablespace){
			//createSQL = new CreateTablespace(dbName, tablespace);
			//dropSQL = new DropTablespace(dbName, tablespace);
			this.dbName = dbName;
			this.tablespace = tablespace;
		}

		@Override
		public CreateTablespace getCreateSQL() {
			if(null == createSQL){
				createSQL = new CreateTablespace(dbName, tablespace);
			}
			return createSQL;
		}

		@Override
		public DropTablespace getDropSQL() {
			if(null == dropSQL){
				dropSQL = new DropTablespace(dbName, tablespace);
			}
			return dropSQL;
		}
		
		protected CreateTablespace createSQL;
		protected DropTablespace dropSQL; 
		protected String dbName;
		protected String tablespace;
	}
	
	class LobTablespace extends Tablespace{
		public LobTablespace(String dbName, String tablespace) {
			super(dbName, tablespace);
		}
		@Override
		public CreateTablespace getCreateSQL() {
			if(null == createSQL){
				createSQL = new SQLStatement.CreateLobTablespace(dbName, tablespace);
			}
			return createSQL;
		}
	}
	
	//----------------------------------------------------------
	
}
