package com.mec.app.plugin.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mec.app.plugin.grammar.DB2Construct.Table.Column;
import com.mec.app.plugin.grammar.SQLStatement.CreateAuxiliaryTable;
import com.mec.app.plugin.grammar.SQLStatement.CreateTable;
import com.mec.app.plugin.grammar.SQLStatement.CreateTablespace;
import com.mec.app.plugin.grammar.SQLStatement.DropAuxiliaryTable;
import com.mec.app.plugin.grammar.SQLStatement.DropTable;
import com.mec.app.plugin.grammar.SQLStatement.DropTablespace;

public interface DB2Construct {

	SQLStatement getCreateSQL();
	
	SQLStatement getDropSQL();
	
//	String getName();
	
	static String strippedQuotes(String str){
		Matcher m = QUOTED_STR.matcher(str);
		m.matches();
		return m.group(1);
	}
	
	static final Pattern QUOTED_STR = Pattern.compile("\"?(\\w+)\"?"); 
	//-----------------------------------------
	class Table implements DB2Construct{
		public Table(String schema, String tableName, String dbName, String tablespace){
			Objects.requireNonNull(tableName);
			Objects.requireNonNull(tablespace);
			this.tableName = tableName;
			this.tablespace = tablespace;
			
			//
			Optional.ofNullable(schema).ifPresent(s -> this.schema = s);
			Optional.ofNullable(dbName).ifPresent(d -> this.dbName = d);
		}

		
//		@Override
//		public static final String getName() {
//			return "TABLE";
//		}


		public Table(String schemaAndTableName, 
				String dbNameAndTablespace){
			Matcher m = NAME_COMBINE_PATTERN.matcher(schemaAndTableName);
			boolean matches = m.matches();
//			this.schema = m.group(2);
			this.schema = Optional.ofNullable(m.group(2)).orElse("");
			this.tableName = m.group(3);
			
			//
			m = NAME_COMBINE_PATTERN.matcher(dbNameAndTablespace);
//			if(m.matches()){
			m.matches();
			this.dbName = Optional.ofNullable(m.group(2)).orElse("");
			this.tablespace = m.group(3);
//			}else{
//				m = NAME_SINGLE_PATTERN.matcher(dbNameAndTablespace);
//				this.dbName = "";
//				this.tablespace = m.group(1);
						
//			}
			
			Objects.requireNonNull(tableName);
			Objects.requireNonNull(tablespace);
		}
		
		@Override
		public CreateTable getCreateSQL() {
			if(null == createSQL){
//				String tableNameWithSchema = String.format("\"%s\".\"%s\"", schema, tableName);
//				String tableNameWithSchema = getTableNameWithSchema();
//				String databaseAndTablespace = getDatabaseAndTablespace();
//				createSQL = new CreateTable(tableNameWithSchema, databaseAndTablespace, columns);
				createSQL = new CreateTable(this);
			}
			return createSQL;
		}

		@Override
		public DropTable getDropSQL() {
			if(null == dropTable){
//				dropTable = new DropTable(getTableNameWithSchema());
				dropTable = new DropTable(this);
			}
			return dropTable;
		}
		
		
		public void addColumn(Column column){
			Objects.requireNonNull(column);
			columns.stream().filter(c -> c.getName().equals(column.getName())).findAny().ifPresent(c -> {
				throw new IllegalArgumentException(String.format("Column with name %s already exists", c.getName()));
			});
			columns.add(column);
		}
		
		public String getTableNameWithSchema(){
//			return String.format("\"%s\".\"%s\"", schema, tableName);
			return combinePrefixWithName(schema, tableName);
		}
		public String getDatabaseAndTablespace(){
//			String databaseAndTablespace = null;
//			if(!dbName.isEmpty()){
//				databaseAndTablespace = String.format("\"%s\".\"%s\"", dbName, tablespace);
//			}else{
//				databaseAndTablespace = String.format("\"%s\"", tablespace);
//			}
//			return databaseAndTablespace;
			return combinePrefixWithName(dbName, tablespace);
		}
		private String combinePrefixWithName(String optionalPrefix, String name){
			String retval = null;
			if(Optional.ofNullable(optionalPrefix).orElse("").isEmpty()){
				retval = String.format("\"%s\"", name);
			}else{
				retval = String.format("\"%s\".\"%s\"", optionalPrefix, name);
			}
			
			return retval;
		}
		
		public String getTableName(){
			return tableName;
		}
		
		public List<Column> getColumns() {
			return columns;
		}
		
//		public boolean hasPrimaryKey(){
////			List<Column> primaryKeys = getPrimaryKeys();
////			return !(null == primaryKeys || primaryKeys.isEmpty());
//			return !getPrimaryKeys().isEmpty();
//		}
//		public boolean hasLOBColumn(){
//			return getColumns().
//		}

		protected List<Column> getPrimaryKeys(){
			return columns.stream().filter(Column::isPrimaryKey).collect(Collectors.toList());
		}
		protected List<Column> getLobColumns(){
//			return columns.stream().filter(c -> c.getColumnType().getDataType().isLOBColumn())
//					.collect(Collectors.toList());
			return columns.stream().filter(Column::isLobColumn).collect(Collectors.toList());
		}
		public String getPrimaryKeysStr(){
			StringBuilder retval = new StringBuilder();

			List<Column> primaryKeys = getPrimaryKeys();
			if(!(null == primaryKeys || primaryKeys.isEmpty())){
				String pkStr = primaryKeys.stream()
					.map(c -> String.format("\"%s\"", c.getName()))
					.collect(Collectors.joining(",", "(", ")"));
				retval.append(pkStr);
			}
			
			return retval.toString();
		}

		public String getSchema(){
			return schema;
		}
		private String schema = "";
		private String tableName;
		
		private String dbName = "";
		private String tablespace;
		
		private static final Pattern NAME_COMBINE_PATTERN = Pattern.compile("(\"?(\\w+)\"?\\.)?\"?(\\w+)\"?");
//		private static final Pattern NAME_SINGLE_PATTERN = Pattern.compile("\"?(\\w+)\"?");
		private List<Column> columns = new ArrayList<>();
		 
		private CreateTable createSQL;
		private DropTable dropTable;
		public static final String NAME = "TABLE";
	
		
		
		public static class Column{
			
			
			
			public Column(String name, String dataType) {
				Objects.requireNonNull(name);
				Objects.requireNonNull(dataType);
				this.name = name;
				this.typeAndLength = ColumnDataTypeAndLength.of(dataType);
//				this.attrs = "";
			}
			
			
			public Column(String name, String dataType, String attrs) {
				this(name, dataType);
				Optional.ofNullable(attrs).ifPresent(a -> this.attrs = a);
				this.attrs = attrs;
			}
			
			@Override
			public String toString(){
				return String.format("\"%s\" %s %s", name, typeAndLength, attrs);
			}
			
			
			public String getName() {
				return name;
			}


			public boolean isPrimaryKey() {
				return primaryKey;
			}
			public void setPrimaryKey(boolean primaryKey) {
				this.primaryKey = primaryKey;
			}

			public ColumnDataTypeAndLength getColumnType(){
				return typeAndLength;
			}
			public boolean isLobColumn(){
				return typeAndLength.getDataType().isLOBColumn();
			}



			private String name;
//			private String dataType;
			private ColumnDataTypeAndLength typeAndLength;
			private String attrs = "";
			private boolean primaryKey = false;
		}
		
		public static class ColumnDataTypeAndLength{
//			ColumnDataTypeAndLength(String typeDesc){
//				this.typeDesc = typeDesc;
//			}
			private ColumnDataTypeAndLength(){
				
			}
			private ColumnDataTypeAndLength(ColumnDataType columnDataType, int length, int factorScale){
				this(columnDataType, length);
				this.fractionScale = factorScale;
			}
			private ColumnDataTypeAndLength(ColumnDataType columnDataType, int length){
				this(columnDataType);
				this.length = length;
			}
			private ColumnDataTypeAndLength(ColumnDataType columnDataType){
				this.dataType = columnDataType;
			}
			
			public static ColumnDataTypeAndLength of(String typeAndLengthDesc){
//				return new ColumnDataTypeAndLength(typeDesc);
				Matcher m = TYPE_LENGTH_PATTERN.matcher(typeAndLengthDesc);
				m.matches();
				
//				boolean m2 = Pattern.matches("\\w+\\(\\d+,\\d+\\)", typeAndLengthDesc);
//				boolean m3 = Pattern.matches("\\w+[\\(\\d+,\\d+\\)]", typeAndLengthDesc);
//				boolean m3 = Pattern.matches("(\\w+)(?:\\((\\d+)(,\\d+)?\\))?", typeAndLengthDesc);
				String dataTypeStr = m.group(1);
				Objects.requireNonNull(dataTypeStr);
				
				ColumnDataTypeAndLength retval = new ColumnDataTypeAndLength();
				retval.dataType = ColumnDataType.forName(dataTypeStr);
				retval.length = Optional.ofNullable(m.group(2)).map(Integer::parseInt).orElse(INIT);
				retval.fractionScale = Optional.ofNullable(m.group(3)).map(Integer::parseInt).orElse(INIT);
				
				return retval;
			}
			
			@Override
			public String toString(){
				StringBuilder retval = new StringBuilder(dataType.name());
				if(INIT == fractionScale){
					if(INIT == length){
						//
					}else{
						retval.append("(").append(length).append(")");
					}
				}else{
					if(INIT == length){
						throw new IllegalArgumentException("Invalid length for TypeAndLength");
					}else{
						retval.append("(").append(length).append(",").append(fractionScale).append(")");
					}
				}
				return retval.toString();
			}
			
			
			public ColumnDataType getDataType() {
				return dataType;
			}

			private ColumnDataType dataType;
//			String typeDesc;
			private int length = INIT;			//precision
			private int fractionScale = INIT;		//scale, for NUMBER
			private static final int INIT = -1;
//			private static final Pattern TYPE_LENGTH_PATTERN = Pattern.compile("(\\w+)[\\((\\d+)[,(\\d+)]\\)]");
			//ref: https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/RegExp
			//Match the type and length. Example scenarios: DATE,  DECIMAL(18,3), CHAR(3), etc.
			private static final Pattern TYPE_LENGTH_PATTERN = Pattern.compile("(\\w+)(?:\\((\\d+)(?:,(\\d+))?\\))?");
		}
		
		enum ColumnDataType{
			VARCHAR
//			,CHARACTER
			,CHAR
			,CLOB
			
//			,GRAPHIC
//			,VARGRAPHIC
//			,DBCLOB
//			,BINARY
//			,VARBINARY
			,BLOB
			
			
			,SMALLINT
			,INTEGER
//			,INT
			,BIGINT
			
			,DECIMAL
//			,NUMERIC
//			,DECFLOAT
//			,REAL
//			,NUMBER	//precision, scale
			,DOUBLE
			
			
			
			,DATE
			,TIME
			,TIMESTAMP
			;
			
			public static ColumnDataType forName(String dataTypeName){
				return Optional.ofNullable(nameToType.get(dataTypeName))
						.orElseThrow(() -> new IllegalArgumentException(String.format("Unrecognized column data type name: %s", dataTypeName)));
			}
			
			public boolean isLOBColumn(){
				return Stream.of(CLOB, BLOB).anyMatch(t -> t == this);
			}
			
			private static Map<String, ColumnDataType> nameToType;	//= new HashMap<>();
			static{
				nameToType = new HashMap<>();
				Arrays.stream(ColumnDataType.values()).forEach(c -> nameToType.put(c.name(), c));
			}
		}
	}
	
	class AuxiliaryTable implements DB2Construct{

//		public AuxiliaryTable(String schemaAndTableName, String dbNameAndTablespace) {
//			super(schemaAndTableName, dbNameAndTablespace);
//		}
		public AuxiliaryTable(Table table, Column lobColumn){
			Objects.requireNonNull(lobColumn);
			if(lobColumn.getColumnType().getDataType().isLOBColumn()){
				throw new IllegalArgumentException(String.format("Column %s is not *LOB column", lobColumn));
			}
			auxiliaryTableName = String.format("%s_AUX_%s", table.getTableName(), lobColumn.getName());
			auxiliaryTablespace = getNextLobTS();
			auxIndexName = String.format("INDEX_%s", auxiliaryTableName);
			this.lobColumn = lobColumn;
		}


		
//		@Override
//		public static final String getName() {
//			return "AUXILIARY";
//		}
		@Override
		public CreateAuxiliaryTable getCreateSQL() {
			if(null == createSQL){
				createSQL = new CreateAuxiliaryTable(this);
			}
			return createSQL;
		}

		@Override
		public DropAuxiliaryTable getDropSQL() {
			if(null == dropSQL){
				dropSQL = new DropAuxiliaryTable(this);
			}
			return dropSQL;
		}
		
		public String getSchema(){
			return table.getSchema();
		}
		
		protected static String getNextLobTS(){
			return String.format("%s%03d", Grammar5DB2DDLAnalyzer.tablespace, startIndex++);
		}
		
		public String getAuxiliaryTablespace(){
			return auxiliaryTablespace;
		}
		
		public String getAuxiliaryDatabase(){
			return Grammar5DB2DDLAnalyzer.dbName;
		}
		
		private String getAuxiliaryTableName(){
			return auxiliaryTableName;
		}
		public String getAuxiliaryTableNameWithSchema(){
			return String.format("\"%s\".\"%s\""
					,getSchema()
					,getAuxiliaryTableName());
		}
		
		public String getAuxiliaryIndexName(){
			return auxIndexName;
		}
		
		public String getNameOfTableToAux(){
			return table.getTableName();
		}
		public String getNameOfColumnToAux(){
			return lobColumn.getName();
		}
		
		
		private String auxiliaryTablespace;
		private Table table;
		private Column lobColumn;
		private String auxiliaryTableName;
		private String auxIndexName;
		private static int startIndex = 1;
		public static final String NAME = "AUXILIARY";
		
		private CreateAuxiliaryTable createSQL;
		private DropAuxiliaryTable dropSQL;
	}
	
	
	
	
	
	
	
	//-----------------------------------------
	class Tablespace implements DB2Construct{

		public Tablespace(String dbName, String tablespace){
			//createSQL = new CreateTablespace(dbName, tablespace);
			//dropSQL = new DropTablespace(dbName, tablespace);
//			this.database = dbName;
//			this.tablespace = tablespace;
			this.database = strippedQuotes(dbName);
			this.tablespace = strippedQuotes(tablespace);
		}


		
//		public static final String getName() {
//			return "TABLESPACE";
//		}
		@Override
		public CreateTablespace getCreateSQL() {
			if(null == createSQL){
//				createSQL = new CreateTablespace(dbName, tablespace);
				createSQL = new CreateTablespace(this);
			}
			return createSQL;
		}

		@Override
		public DropTablespace getDropSQL() {
			if(null == dropSQL){
//				dropSQL = new DropTablespace(dbName, tablespace);
				dropSQL = new DropTablespace(this);
			}
			return dropSQL;
		}
		
		public String getDatabase() {
			return database;
		}
		public String getTablespace() {
			return tablespace;
		}

		protected CreateTablespace createSQL;
		protected DropTablespace dropSQL; 
		protected String database;
		protected String tablespace;
		
		
		public static final String NAME = "TABLESPACE";
	}
	
	class LobTablespace extends Tablespace{
		public LobTablespace(String dbName, String tablespace) {
			super(dbName, tablespace);
		}
		@Override
		public CreateTablespace getCreateSQL() {
			if(null == createSQL){
//				createSQL = new SQLStatement.CreateLobTablespace(dbName, tablespace);
				createSQL = new SQLStatement.CreateLobTablespace(this);
			}
			return createSQL;
		}
		public static final String NAME = "LOB";
	}
	
	//----------------------------------------------------------
	interface Index extends DB2Construct{
		String getName();
		Table getTable();
	}
	static class PrimaryKeyIndex implements Index{
		public PrimaryKeyIndex(String indexName, Table table){
			Objects.requireNonNull(indexName);
			Objects.requireNonNull(table);
			this.name = indexName;
			this.table = table;
		}
		
		public PrimaryKeyIndex(Table table){
//			String indexName = String.format("PK_%S", table.getTableName());
			this(String.format("PK_%S", table.getTableName()), table);
		}
		
		@Override
		public SQLStatement getCreateSQL() {
			return null;
		}

		@Override
		public SQLStatement getDropSQL() {
			return null;
		}

		@Override
		public String getName() {
			return name;
		}
		@Override
		public Table getTable(){
			return table;
		}
//		public String getTableName(){
//			return table.getTableName();
//		}
		

		private String name;
		private Table table;
		public static final String NAME = "LOB";
	}
	
	
	static class AuxiliaryIndex extends PrimaryKeyIndex{

		public AuxiliaryIndex(Table table) {
			super(table);
		}
		
	}
	
	//----------------------------------------------------------
	//----------------------------------------------------------
	//----------------------------------------------------------
	//----------------------------------------------------------
	//----------------------------------------------------------
	
}
