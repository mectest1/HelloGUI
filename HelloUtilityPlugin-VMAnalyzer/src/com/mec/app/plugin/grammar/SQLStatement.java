package com.mec.app.plugin.grammar;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.mec.app.plugin.grammar.DB2Construct.AuxiliaryTable;
import com.mec.app.plugin.grammar.DB2Construct.Index;
import com.mec.app.plugin.grammar.DB2Construct.Table;
import com.mec.app.plugin.grammar.DB2Construct.Table.Column;
import com.mec.app.plugin.grammar.DB2Construct.Tablespace;
import com.mec.app.plugin.grammar.DB2Construct.UniqueConstraintIndex;
import com.mec.app.plugin.grammar.DB2Construct.UniqueConstraintType;

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
		
		private CreateTable(Table table){
			this.table = table;
		}
		
		public static CreateTable forTable(Table table){
			return new CreateTable(table);
		}
		

//		protected static CreateTable newEmpty(){
//			return null;
//		}
		@Override
		public String toString(){
//			StringBuilder retval = new StringBuilder();
//			retval.append(getCreateTableSQL());
//			return retval.toString();
			
			StringBuilder retval = new StringBuilder();
			
//			retval.append(String.format("CREATE TABLE %s ", tableNameWithSchema));
			retval.append(String.format("CREATE TABLE %s ", table.getTableNameWithSchema()));
			
			List<Column> columns = table.getColumns();
			if(!(null == columns || columns.isEmpty())){
				retval.append("(\n");
//				String columnsSQL = columns.stream().map(Table.Column::toString)
				String columnsSQL = columns.stream().map(c -> "\t\t" + c.toString())
//						.collect(Collectors.joining(",\n"))
						.collect(Collectors.joining(" ,\n"))
						;
				retval.append(columnsSQL);
				retval.append(" )");
			}
			
//			retval.append(String.format("\n\tIN %s;", databaseAndTablespace));
			retval.append(String.format("\n\tIN %s;", table.getDatabaseAndTablespace()));
			return retval.toString();
		}
		
//		private StringBuilder getCreateTableSQL(){
//			StringBuilder retval = new StringBuilder();
//			
////			retval.append(String.format("CREATE TABLE %s ", tableNameWithSchema));
//			retval.append(String.format("CREATE TABLE %s ", table.getTableNameWithSchema()));
//			
//			List<Column> columns = table.getColumns();
//			if(!(null == columns || columns.isEmpty())){
//				retval.append("(\n");
////				String columnsSQL = columns.stream().map(Table.Column::toString)
//				String columnsSQL = columns.stream().map(c -> "\t\t" + c.toString())
////						.collect(Collectors.joining(",\n"))
//						.collect(Collectors.joining(" ,\n"))
//						;
//				retval.append(columnsSQL);
//				retval.append(")");
//			}
//			
////			retval.append(String.format("\n\tIN %s;", databaseAndTablespace));
//			retval.append(String.format("\n\tIN %s;", table.getDatabaseAndTablespace()));
//			return retval;
//		}
		
//		private StringBuilder getAddPrimaryKeySQL(){
//			StringBuilder retval = new StringBuilder();
//			table.getPrimaryKeys().forE
//			
//			return retval;
//		}
		
		protected Table table;
//		private String tableNameWithSchema;
//		private String databaseAndTablespace;
//		private List<Table.Column> columns;
	}
	
	class CreateAuxiliaryTable implements SQLStatement{

		private CreateAuxiliaryTable(AuxiliaryTable auxTable) {
//			super(table);
//			Objects.requireNonNull(lobColumn);
//			if(lobColumn.getColumnType().getDataType().isLOBColumn()){
//				throw new IllegalArgumentException(String.format("Column %s is not *LOB column", lobColumn));
//			}
//			auxiliaryTableName = String.format("%s_AUX_%s", table.getTableName(), lobColumn.getName());
			this.auxTable = auxTable;
		}
		
		public static CreateAuxiliaryTable forAuxTable(AuxiliaryTable auxTable){
			return new CreateAuxiliaryTable(auxTable);
		}
		
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			retval.append(String.format("CREATE LOB TABLESPACE \"%s\" IN \"%s\"" 
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
			
			retval.append(String.format("CREATE INDEX \"%s\"", auxTable.getAuxiliaryIndexName()));
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
		
		private DropTable(Table table){
			this.table = table;
		}
		
		public static DropTable forTable(Table table){
			return new DropTable(table);
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
		private DropAuxiliaryTable(AuxiliaryTable auxTable){
			this.auxTable = auxTable;
		}
		
		public static DropAuxiliaryTable forAuxTable(AuxiliaryTable auxTable){
			return new DropAuxiliaryTable(auxTable);
		}
		
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			
//			retval.append(String.format("DROP INDEX \"%s\";", auxTable.getAuxiliaryIndexName()));
//			retval.append(String.format("\nDROP TABLE \"%s\";", auxTable.getAuxiliaryTableNameWithSchema()));
			retval.append(String.format("DROP TABLESPACE \"%s\".\"%s\";"
					, auxTable.getAuxiliaryDatabase()
					, auxTable.getAuxiliaryTablespace()));
			
			return retval.toString();
		}
		
		
		private AuxiliaryTable auxTable;
	}
	
	
	
	
	
	//----------------------------------------
//	class CreateAuxiliaryIndex implements SQLStatement{
//		
//		public CreateAuxiliaryIndex(AuxiliaryIndex auxIndex){
//			Objects.requireNonNull(auxIndex);
//			this.auxIndex = auxIndex;
//		}
//		
//		@Override
//		public String toString(){
//			StringBuilder retval = new StringBuilder();
//			retval.append(String.format("", 
//					
//					
//					));
//			
//			
//			return retval.toString();
//		}
//		
//		private AuxiliaryIndex auxIndex;
//	}
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
		private CreateTablespace(Tablespace ts){
			this.ts = ts;
		}
		
		public static CreateTablespace forTablespace(Tablespace ts){
			return new CreateTablespace(ts);
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
		private CreateLobTablespace(Tablespace ts){
			super(ts);
		}

		public static CreateLobTablespace forLobTablespace(Tablespace ts){
			return new CreateLobTablespace(ts);
		}
		
		
		@Override
		public String toString() {
//			return String.format("CREATE LOB TABLESPACE \"%s\" in \"%s\";" 
//					,ts , db);
			return String.format("CREATE LOB TABLESPACE \"%s\" IN \"%s\";" 
					,ts.getTablespace() , ts.getDatabase());
		}
		
	}
	
	class DropTablespace implements SQLStatement{
//		public DropTablespace(String dbName, String tablespace){
//			db = dbName;
//			ts = tablespace;
//		}
		private DropTablespace(Tablespace ts){
			this.ts = ts;
		}
		
		public static DropTablespace forTablespace(Tablespace ts){
			return new DropTablespace(ts);
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
//	class CreatePrimaryKeyIndex implements SQLStatement{
//		public CreatePrimaryKeyIndex(UniqueConstraintIndex index){
//			Objects.requireNonNull(index);
//			this.index = index;
//		}
//		
//		@Override
//		public String toString(){
//			StringBuilder retval = new StringBuilder();
//			retval.append(String.format(
//					"CREATE UNIQUE INDEX \"%s\" ON %s", 
//					index.getName(), index.getIndexedTable().getTableName()));
////			String primaryKeys = index.getTable().getPrimaryKeys().stream()
////				.map(c -> String.format("\"%s\"", c.getName()))
////				.collect(Collectors.joining(",", "(", ")"));
////			retval.append(primaryKeys).append(";");
//			retval.append(index.getIndexedTable().getPrimaryKeysStr()).append(";");
//			return retval.toString();
//		}
//		
//		private UniqueConstraintIndex index;
//	}
	
	class DropIndex implements SQLStatement{
		private DropIndex(Index index){
			Objects.requireNonNull(index);
			this.index = index;
		}
		
		public static DropIndex forIndex(Index indexToDrop){
			return new DropIndex(indexToDrop);
		}
		
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			
			retval.append(String.format("DROP INDEX %s", 
					index.getName()));
			
			return retval.toString();
		}
		
		private Index index;
	}
	
	//----------------------------------------
	class AlterTableAddUniqueConstraint implements SQLStatement{
//		public AlterTableAddUniqueConstraint(Table table){
		private AlterTableAddUniqueConstraint(UniqueConstraintIndex constraintIndex){
			Objects.requireNonNull(constraintIndex);
			this.index = constraintIndex;
			this.table = constraintIndex.getIndexedTable();
			pkName = String.format("PK_%s", table.getTableName());
			ukName = String.format("UK_%s", table.getTableName());
		}
		
		
		public static AlterTableAddUniqueConstraint forUniqueConstraintIndex(UniqueConstraintIndex constraintIndex){
			return new AlterTableAddUniqueConstraint(constraintIndex);
		}
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			UniqueConstraintType constraintType = index.getConstraintType();
//			retval.append(String.format("ALTER TABLE %s \n\tADD CONSTRAINT \"%s\" PRIMARY KEY %s;"
//					, table.getTableNameWithSchema()
//					, pkName
//					, table.getPrimaryKeysStr()
//					));
			
//			CreatePrimaryKeyIndex createIndex = new CreatePrimaryKeyIndex(constraintIndex);
//			retval.append(createIndex.toString()).append("\n");
//			String primaryKeys = index.getTable().getPrimaryKeys().stream()
//				.map(c -> String.format("\"%s\"", c.getName()))
//				.collect(Collectors.joining(",", "(", ")"));
//			retval.append(primaryKeys).append(";");
			retval.append(String.format(
					"CREATE UNIQUE INDEX \"%s\" ON %s \n\t", 
					index.getName(), 
//					index.getIndexedTable().getTableName()
					index.getIndexedTable().getTableNameWithSchema()
					));
//			retval.append(index.getIndexedTable().getPrimaryKeysStr()).append(";\n");
			String columnsNameList = UniqueConstraintType.PRIMARY_KEY == constraintType ? 
					index.getIndexedTable().getPrimaryKeysStr() :
					index.getIndexedTable().getUniqueKeysStr();
			retval.append(columnsNameList).append(";\n");
			
			retval.append(String.format("ALTER TABLE %s \n\tADD CONSTRAINT \"%s\" %s \n\t%s;"
					, table.getTableNameWithSchema()
					, UniqueConstraintType.PRIMARY_KEY == constraintType ? pkName : ukName
					, constraintType.getValue()
//					, table.getPrimaryKeysStr()
					, columnsNameList
					));
			
			
			return retval.toString();
		}
			
//		private CreateIndex createIndex;
		private UniqueConstraintIndex index;
		private String pkName;
		private String ukName;
		private Table table;
	}
	class AlterTableDropUniqueConstraint implements SQLStatement{
		private AlterTableDropUniqueConstraint(Table table){
			Objects.requireNonNull(table);
			this.table = table;
			pkName = String.format("PK_%s", table.getTableName());
		}
		
		public static AlterTableDropUniqueConstraint forTable(Table tableToAlter){
			return new AlterTableDropUniqueConstraint(tableToAlter);
		}
		
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder();
			retval.append(
					String.format("ALTER TABLE %s DROP CONSTRAINT \"%s\";"
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

	class InsertStatement implements SQLStatement{
		
		private InsertStatement(Table table){
			Objects.requireNonNull(table);
			this.table = table;
		}
//		private InsertStatement(Table table, List<Column> insertColumns, List<String> values){
//			Objects.requireNonNull(table);
//			this.table = table;
//		}
		
		public static InsertStatement forTable(Table table){
			return new InsertStatement(table);
		}
		
		
		@Override
		public String toString(){
			StringBuilder retval = new StringBuilder("INSERT INTO ");
			
			retval.append(table.getTableNameWithSchema()).append(" ");
			retval.append(table.getColumnsStr());
			
			retval.append(" VALUES(");
			
			//TODO:
			
			retval.append(")");
			
			
			return retval.toString();
		}
		
		private Table table;
//		private List<Column> 
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
