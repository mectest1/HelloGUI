package com.mec.app.plugin.grammar;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.mec.app.plugin.grammar.DB2Construct.AuxiliaryTable;
import com.mec.app.plugin.grammar.DB2Construct.PrimaryKeyIndex;
import com.mec.app.plugin.grammar.DB2Construct.Table;
import com.mec.app.plugin.grammar.DB2Construct.Table.Column;
import com.mec.app.plugin.grammar.DB2Construct.Tablespace;

public interface SQLStatement {

//	String getStart();
	
	interface StatementType{
		
	}
	
	enum CreateStatementType implements StatementType{
		CREATE_TABLESPACE
		,CREATE_LOB_TABLESPACE
		
		,CREATE_TABLE
		,CREATE_AUXLIARY_TABLE
		
		,CREATE_UNIQUE_INDEX
		,CREATE_INDEX
		,
		
		;
	}
	
	enum AlterStatementType implements StatementType{
		ALTER_TABLE
		
		
		;
		
	}
	//----------------------------------------


	/**
	 * 
	 * ref:http://www.ibm.com/support/knowledgecenter/SSEPEK_11.0.0/com.ibm.db2z11.doc.sqlref/src/tpc/db2z_sql_createtable.dita?lang=en
	 * @author MEC
	 *
	 */
	class CreateTable implements SQLStatement{
//		public CreateTable(String tableNameWithSchema, String databaseAndTablespace
//				,List<Table.Column> columns){
//			this.tableNameWithSchema = tableNameWithSchema;
//			this.databaseAndTablespace = databaseAndTablespace;
//			this.columns = columns;
//		}
		
		public CreateTable(Table table){
			this.table = table;
		}
		
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			
//			retval.append(String.format("CREATE TABLE %s ", tableNameWithSchema));
			retval.append(String.format("CREATE TABLE %s ", table.getTableNameWithSchema()));
			
			List<Column> columns = table.getColumns();
			if(!(null == columns || columns.isEmpty())){
				retval.append("(\n");
//				String columnsSQL = columns.stream().map(Table.Column::toString)
				String columnsSQL = columns.stream().map(c -> "\t\t" + c.toString())
						.collect(Collectors.joining(",\n"))
						;
				retval.append(columnsSQL);
				retval.append(")");
			}
			
//			retval.append(String.format("\n\tIN %s;", databaseAndTablespace));
			retval.append(String.format("\n\tIN %s;", table.getDatabaseAndTablespace()));
			return retval.toString();
		}
		
		protected Table table;
//		private String tableNameWithSchema;
//		private String databaseAndTablespace;
//		private List<Table.Column> columns;
	}
	
	class CreateAuxiliaryTable implements SQLStatement{

		public CreateAuxiliaryTable(AuxiliaryTable auxTable) {
//			super(table);
//			Objects.requireNonNull(lobColumn);
//			if(lobColumn.getColumnType().getDataType().isLOBColumn()){
//				throw new IllegalArgumentException(String.format("Column %s is not *LOB column", lobColumn));
//			}
//			auxiliaryTableName = String.format("%s_AUX_%s", table.getTableName(), lobColumn.getName());
			this.auxTable = auxTable;
		}
		
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			retval.append(String.format("CREATE LOB TABLESPACE \"%s\" in \"%s\"" 
					,auxTable.getAuxiliaryTablespace()
					,auxTable.getAuxiliaryDatabase()
					));
			retval.append(";\n");
			retval.append(String.format("CREATE AUXILIARY TABLE %s"
					,auxTable.getAuxiliaryTableNameWithSchema()
					));
			retval.append(String.format("\n\tIN \"%s\".\"%s\"", auxTable.getAuxiliaryDatabase(), auxTable.getAuxiliaryTablespace()));
			retval.append(String.format("\n\tSTORES \"%s\".\"%s\"", auxTable.getSchema(), auxTable.getNameOfTableToAux()));
			retval.append(String.format("\n\tCOLUMN \"%s\"", auxTable.getNameOfColumnToAux()));
			retval.append(";\n");
			
			retval.append(String.format("CREATE INDEX \"%s\"\n", auxTable.getAuxiliaryIndexName()));
			retval.append(String.format("\n\tON %s", auxTable.getAuxiliaryTableNameWithSchema()));
			retval.append(";");
			
			return retval.toString();
		}
		
//		private String auxiliaryTableName;
		private AuxiliaryTable auxTable;
		
	}
	
	class DropTable implements SQLStatement{
		
//		public DropTable(String tableNameWithSchema) {
//			super();
//			this.tableNameWithSchema = tableNameWithSchema;
//		}
		
		public DropTable(Table table){
			this.table = table;
		}
		
		
		@Override
		public String toString() {
//			return String.format("DROP TABLE %s;", tableNameWithSchema);
			return String.format("DROP TABLE %s;", table.getTableNameWithSchema());
		}


//		private String tableNameWithSchema;
		private Table table;
	}
	
	
	class DropAuxiliaryTable implements SQLStatement{
		public DropAuxiliaryTable(AuxiliaryTable auxTable){
			this.auxTable = auxTable;
		}
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			
			retval.append(String.format("DROP INDEX \"%s\";", auxTable.getAuxiliaryIndexName()));
			retval.append(String.format("\nDROP TABLE \"%s\";", auxTable.getAuxiliaryTableNameWithSchema()));
			retval.append(String.format("DROP TABLESPACE \"%s\".\"%s\";"
					, auxTable.getAuxiliaryDatabase()
					, auxTable.getAuxiliaryTablespace()));
			
			return retval.toString();
		}
		
		
		private AuxiliaryTable auxTable;
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
//		public CreateTablespace(String dbName, String tablespace){
//			db = dbName;
//			ts = tablespace;
//		}
		public CreateTablespace(Tablespace ts){
			this.ts = ts;
		}
		@Override
		public String toString(){
//			return String.format("CREATE TABLESPACE \"%s\" IN \"%s\" BUFFERPOOL \"%s\";"
//					,ts, db, BUFFERPOOL);
			return String.format("CREATE TABLESPACE \"%s\" IN \"%s\" BUFFERPOOL \"%s\";"
					,ts.getTablespace(), ts.getDatabase(), BUFFERPOOL);
		}
		
//		protected String db;
//		protected String ts;
		//ref: https://www.ibm.com/support/knowledgecenter/SSEPEK_10.0.0/com.ibm.db2z10.doc.comref/src/tpc/db2z_cmd_alterbufferpool.dita
		
		protected Tablespace ts;
		private static final String BUFFERPOOL = "BP32K";
	}
	
	class CreateLobTablespace extends CreateTablespace{
//		public CreateLobTablespace(String dbName, String tablespace) {
//			super(dbName, tablespace);
//		}
		public CreateLobTablespace(Tablespace ts){
			super(ts);
		}

		@Override
		public String toString() {
//			return String.format("CREATE LOB TABLESPACE \"%s\" in \"%s\";" 
//					,ts , db);
			return String.format("CREATE LOB TABLESPACE \"%s\" in \"%s\";" 
					,ts.getTablespace() , ts.getDatabase());
		}
		
	}
	
	class DropTablespace implements SQLStatement{
//		public DropTablespace(String dbName, String tablespace){
//			db = dbName;
//			ts = tablespace;
//		}
		public DropTablespace(Tablespace ts){
			this.ts = ts;
		}
		
		@Override
		public String toString(){
//			return String.format("", db, ts);
//			return fm.format("DROP TABLESPACE \"%s\".\"%s\";", db, ts).toString();
//			return String.format("DROP TABLESPACE \"%s\".\"%s\";", db, ts);
			return String.format("DROP TABLESPACE \"%s\".\"%s\";", ts.getDatabase(), ts.getTablespace());
		}
		
//		private String db;
//		private String ts;
		private Tablespace ts;
	}
	//----------------------------------------
	class CreatePrimaryKeyIndex implements SQLStatement{
		public CreatePrimaryKeyIndex(PrimaryKeyIndex index){
			this.index = index;
		}
		
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			retval.append(String.format(
					"CREATE UNIQUE INDEX \"%s\" ON %s", 
					index.getName(), index.getTable().getTableName()));
//			String primaryKeys = index.getTable().getPrimaryKeys().stream()
//				.map(c -> String.format("\"%s\"", c.getName()))
//				.collect(Collectors.joining(",", "(", ")"));
//			retval.append(primaryKeys).append(";");
			retval.append(index.getTable().getPrimaryKeysStr()).append(";");
			return retval.toString();
		}
		
		private PrimaryKeyIndex index;
	}
	
	//----------------------------------------
	class AlterTableAddPrimaryKey implements SQLStatement{
		public AlterTableAddPrimaryKey(Table table){
			Objects.requireNonNull(table);
			this.table = table;
			pkName = String.format("PK_%s", table.getTableName());
		}
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			retval.append(String.format("ALTER TABLE %s ADD CONSTRAINT \"%s\" PRIMARY KEY %s;"
					, table.getTableNameWithSchema()
					, pkName
					, table.getPrimaryKeysStr()
					));
			
			
			return retval.toString();
		}
		
		private String pkName;
		private Table table;
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
