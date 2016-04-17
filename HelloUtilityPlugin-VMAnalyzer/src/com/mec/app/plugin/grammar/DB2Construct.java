package com.mec.app.plugin.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import com.mec.app.plugin.grammar.SQLStatement.AlterTableAddUniqueConstraint;
import com.mec.app.plugin.grammar.SQLStatement.AlterTableDropUniqueConstraint;
import com.mec.app.plugin.grammar.SQLStatement.CreateAuxiliaryTable;
import com.mec.app.plugin.grammar.SQLStatement.CreateLobTablespace;
import com.mec.app.plugin.grammar.SQLStatement.CreateTable;
import com.mec.app.plugin.grammar.SQLStatement.CreateTablespace;
import com.mec.app.plugin.grammar.SQLStatement.DropAuxiliaryTable;
import com.mec.app.plugin.grammar.SQLStatement.DropTable;
import com.mec.app.plugin.grammar.SQLStatement.DropTablespace;

public interface DB2Construct {

	SQLStatement getCreateSQL();
	
	SQLStatement getDropSQL();
	
//	String getName();
//-----------------------------------------
	interface Indexable extends DB2Construct{
		
	}
//	class Table implements DB2Construct{
	class Table implements Indexable{
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
			m.matches();
//			boolean matches = m.matches();
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
//				createSQL = new CreateTable(this);
				createSQL = CreateTable.forTable(this);
			}
			return createSQL;
		}

		@Override
		public DropTable getDropSQL() {
			if(null == dropTable){
//				dropTable = new DropTable(getTableNameWithSchema());
//				dropTable = new DropTable(this);
				dropTable = DropTable.forTable(this);
			}
			return dropTable;
		}
		
		
		public List<SQLStatement> getCreateUsableTableSQL(){
			List<SQLStatement> retval = new ArrayList<>();
			CreateTable createSQL = getCreateSQL();
			retval.add(createSQL);
			
			List<Column> pkColumns = getPrimaryKeys();
//			if(!getPrimaryKeys().isEmpty()){
			if(!pkColumns.isEmpty()){
//				CreatePrimaryKeyIndex createIndex = pkIndex.getCreateSQL();
				UniqueConstraintIndex pkIndex = new UniqueConstraintIndex(this);
//				CreatePrimaryKeyIndex createPKIndex = new CreatePrimaryKeyIndex(pkIndex);
//				retval.add(createPKIndex);
//				
//				AlterTableAddUniqueConstraint addPrimaryKey = pkIndex.getCreateSQL();
//				retval.add(addPrimaryKey);
				AlterTableAddUniqueConstraint addPrimaryKey = pkIndex.getCreateSQL();
				retval.add(addPrimaryKey);
			}
			
			
			if(!uniqueKeyColumns.isEmpty()){
//				boolean uniqueKeyInPKList =uniqueKeyColumns.stream().allMatch(ukCol -> 
//					pkColumns.stream().anyMatch(pkCol -> 
//						pkCol.getName().equals(ukCol.getName())
//					)
//				);
//				if(uniqueKeyInPKList){
				if(pkColumns.containsAll(uniqueKeyColumns) 
					&& uniqueKeyColumns.containsAll(pkColumns)){
					//primary key columns and unique key columns are the same.;
					//in this case, only one index & constraint will be created
					//or simply reports error
					throw new IllegalArgumentException("primary key columns and the unique constrainted columns are the same");
				}else{
					UniqueConstraintIndex ukIndex = new UniqueConstraintIndex(this, UniqueConstraintType.UNIQUE_KEY);
					AlterTableAddUniqueConstraint addUniqueKey = ukIndex.getCreateSQL();
					retval.add(addUniqueKey);
				}
			}
			
			if(!getLobColumns().isEmpty()){
				getLobColumns().forEach(lobCol -> {
					AuxiliaryTable auxTable = new AuxiliaryTable(this, lobCol);
					CreateAuxiliaryTable createAuxTable = auxTable.getCreateSQL();
					retval.add(createAuxTable);
//					AuxiliaryIndex auxIndex = new AuxiliaryIndex(this);
				});
			}
			
			return retval;
		}
		
		/**
		 * To drop a table cleanly, we only need to drop this table first, 
		 * and then drop all the LOB tablespaces;
		 * @return
		 */
		public List<SQLStatement> getDropTableAndTS(){
			List<SQLStatement> retval = new ArrayList<>();
			DropTable dropTable = getDropSQL();
			retval.add(dropTable);
			
			//
			List<Column> lobColumns = getLobColumns();
			if(!lobColumns.isEmpty()){
				lobColumns.forEach(lobCol -> {
					AuxiliaryTable auxTable = new AuxiliaryTable(this, lobCol);
//					CreateAuxiliaryTable createAuxTable = auxTable.getCreateSQL();
					DropAuxiliaryTable dropAuxTable= auxTable.getDropSQL();
					retval.add(dropAuxTable);
				});
			}
			
			return retval;
		}
		
		public void addColumn(Column column){
			Objects.requireNonNull(column);
			columns.stream().filter(c -> c.getName().equals(column.getName())).findAny().ifPresent(c -> {
				throw new IllegalArgumentException(String.format("Column with name %s already exists", c.getName()));
			});
			columns.add(column);
		}
		public void addColumns(Collection<Column> columns){
			Objects.requireNonNull(columns);
			columns.stream().forEach(this::addColumn);
		}
		
		public String getTableNameWithSchema(){
//			return String.format("\"%s\".\"%s\"", schema, tableName);
			return combinePrefixWithName(schema, tableName);
		}
		public String getDatabaseAndTablespace(){
			
			
//			return combinePrefixWithName(dbName, tablespace);
			
			return combinePrefixWithName(Grammar5DB2DDLAnalyzer.dbName, Grammar5DB2DDLAnalyzer.tablespace);
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
			return getColumnsNameList(getPrimaryKeys());
		}
		public String getColumnsStr(){
			return getColumnsNameList(getColumns());
		}
		
		
		
		public String getUniqueKeysStr(){
			return getColumnsNameList(uniqueKeyColumns);
		}
		
		private static String getColumnsNameList(List<Column> columns){
			StringBuilder retval = new StringBuilder();

//			List<Column> primaryKeys = getPrimaryKeys();
			if(!(null == columns || columns.isEmpty())){
				String pkStr = columns.stream()
					.map(c -> String.format("\"%s\"", c.getName()))
					.collect(Collectors.joining(",\n\t", "(", ")"));
				retval.append(pkStr);
			}
			
			return retval.toString();
		}
		public void addUniqueConstraintKey(String columnName, UniqueConstraintType constraintType){
//			String colName = Lexer.stripQuotes(columnName);
//			Optional<Column> column = getColumns().stream().filter(c -> colName.equals(c.getName())).findFirst();
//			if(!column.isPresent()){
//				throw new IllegalArgumentException(String.format("the primary key column %s doesn't exist in table", columnName));
//			}else{
//				column.get().setPrimaryKey(true);
//			}
			Column col = getAvailableColumn(columnName);
			if(UniqueConstraintType.PRIMARY_KEY == constraintType){
				col.setPrimaryKey(true);
			}else{
				uniqueKeyColumns.add(col);
			}
		}
		
		private void addPrimaryKey(String columnName){}
		private void addUniqueKey(String columnName){
			Column col = getAvailableColumn(columnName);
			uniqueKeyColumns.add(col);
		}
		
		private Column getAvailableColumn(String columnName){
			String colName = Lexer.stripQuotes(columnName);
			Optional<Column> column = getColumns().stream().filter(c -> colName.equals(c.getName())).findFirst();
//			if(!column.isPresent()){
//				throw new IllegalArgumentException(String.format("the primary key column %s doesn't exist in table", columnName));
//			}
			return column.orElseThrow(() -> 
					new IllegalArgumentException(String.format("there is no such column with name %s in this table", columnName))
				);
		}

		public String getSchema(){
			return schema;
		}
		
		
		public List<Column> getUniqueKeyColumns() {
			return uniqueKeyColumns;
		}

		private List<Column> uniqueKeyColumns = new ArrayList<>();;
		private String schema = "";
		private String tableName;
		
		private String dbName = "";
		private String tablespace;
		
		private static final Pattern NAME_COMBINE_PATTERN = Pattern.compile("(\"?(\\w+)\"?\\.)?\"?(\\w+)\"?");
//		private static final Pattern NAME_SINGLE_PATTERN = Pattern.compile("\"?(\\w+)\"?");
		private List<Column> columns = new ArrayList<>();
		 
		private CreateTable createSQL;
		private DropTable dropTable;
//		public static final String NAME = "TABLE";
	
		
		
		public static class Column{
			
			
			
			public Column(String name, String dataType) {
				Objects.requireNonNull(name);
				Objects.requireNonNull(dataType);
//				this.name = name;
				this.name = Lexer.stripQuotes(name);
				this.typeAndLength = ColumnDataTypeAndLength.of(dataType);
//				this.attrs = "";
			}
			
			
			public Column(String name, String dataType, String attrs) {
				this(name, dataType);
				Optional.ofNullable(attrs).ifPresent(a -> this.attrs = a);
				this.attrs = attrs.trim();
				verifyDefaultValue();
			}
			
			@Override
			public String toString(){
//				return String.format("\"%s\" %s %s", name, typeAndLength, attrs);
				return String.format("\"%s\" %s %s", name, typeAndLength, typeAndLength.getDataType().resolveAttrs(attrs));
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

			private void verifyDefaultValue(){
				if(typeAndLength.getDataType().isStringColumn()){
					Matcher m = WITH_DEFAULT_STR.matcher(attrs);
					if(m.matches()){
						String defaultValue = m.group(1);
						int length = typeAndLength.getLength();
						if(length < defaultValue.length()){
							String newDefaultVal = defaultValue.trim();	//try to trim the redundant spaces
							if(length < newDefaultVal.length()){	//stil an invalid default value
								throw new IllegalArgumentException(
										String.format("default value %s is too long for permitted length %s"
												,defaultValue, length));
							}else{
								attrs = attrs.replace(String.format("'%s'", defaultValue), String.format("'%s'", newDefaultVal));
							}
						}
					}
				}
			}
			

			private String name;
//			private String dataType;
			private ColumnDataTypeAndLength typeAndLength;
			private String attrs = "";
			private boolean primaryKey = false;
			

			//Match the default value for *CHAR columns, e.g: NOT NULL WITH DEFAULT '0000000000 '
			static final Pattern WITH_DEFAULT_STR = Pattern.compile("(?:\\w+\\s)*?WITH DEFAULT '(\\w+\\s?)'");
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
//				verifyDefaultValue();
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
			
			public int getLength(){
				return length;
			}
			
			protected int getFractionScale(){
				return fractionScale;
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
			
			public boolean isTemporalColumn(){
				return Stream.of(DATE, TIME, TIMESTAMP).anyMatch(t -> t == this);
			}
			
			public boolean isStringColumn(){
				return Stream.of(VARCHAR, CHAR).anyMatch(t -> t == this);
			}
			
			
			public String resolveAttrs(String columnAttrStr){
				String retval = columnAttrStr;
				if(isLOBColumn()){
					retval = retval.replaceAll("NOT LOGGED", "");
					retval = retval.replaceAll("NOT COMPACT", "");
					retval = retval.replaceAll("LOGGED", "");
					retval = retval.replaceAll("COMPACT", "");
//				}else if(isTemporalColumn()){
				}else if(DATE == this){
					retval = retval.replaceAll("CURRENT DATE", "");
				}else if(TIMESTAMP == this){
					retval = retval.replaceAll("CURRENT TIMESTAMP", "");
				}
				return retval.trim();
			}
			
			private static Map<String, ColumnDataType> nameToType;	//= new HashMap<>();
			static{
				nameToType = new HashMap<>();
				Arrays.stream(ColumnDataType.values()).forEach(c -> nameToType.put(c.name(), c));
			}
		}
	}
	
//	class AuxiliaryTable implements DB2Construct{
	class AuxiliaryTable implements Indexable{

//		public AuxiliaryTable(String schemaAndTableName, String dbNameAndTablespace) {
//			super(schemaAndTableName, dbNameAndTablespace);
//		}
		public AuxiliaryTable(Table table, Column lobColumn){
			Objects.requireNonNull(table);
			Objects.requireNonNull(lobColumn);
			if(!lobColumn.getColumnType().getDataType().isLOBColumn()){
				throw new IllegalArgumentException(String.format("Column %s is not *LOB column", lobColumn));
			}
			this.table = table;
			this.lobColumn = lobColumn;
			auxiliaryTableName = String.format("%s_AUX_%s", table.getTableName(), lobColumn.getName());
			auxiliaryTablespace = getNextLobTS();
//			auxIndexName = String.format("INDEX_%s", auxiliaryTableName);
			//In case different schemas have the same table
			auxIndexName = String.format("INDEX_%s_%s", table.getSchema(), auxiliaryTableName);
		}


		
//		@Override
//		public static final String getName() {
//			return "AUXILIARY";
//		}
		@Override
		public CreateAuxiliaryTable getCreateSQL() {
			if(null == createSQL){
//				createSQL = new CreateAuxiliaryTable(this);
				createSQL = CreateAuxiliaryTable.forAuxTable(this);
			}
			return createSQL;
		}

		@Override
		public DropAuxiliaryTable getDropSQL() {
			if(null == dropSQL){
//				dropSQL = new DropAuxiliaryTable(this);
				dropSQL = DropAuxiliaryTable.forAuxTable(this);
			}
			return dropSQL;
		}
		
//		public DropTablespace getDropAuxiliaryTablespaceSQL(){
//			DropTablespace dropTablespace = new DropTablespace(new Tablespace(dbName, tablespace))
//		}
		
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
//			return table.getDatabaseName();
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
//		public static final String NAME = "AUXILIARY";
		
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
			this.database = Lexer.stripQuotes(dbName);
			this.tablespace = Lexer.stripQuotes(tablespace);
		}


		
//		public static final String getName() {
//			return "TABLESPACE";
//		}
		@Override
		public CreateTablespace getCreateSQL() {
			if(null == createSQL){
//				createSQL = new CreateTablespace(dbName, tablespace);
//				createSQL = new CreateTablespace(this);
				createSQL = CreateTablespace.forTablespace(this);
			}
			return createSQL;
		}

		@Override
		public DropTablespace getDropSQL() {
			if(null == dropSQL){
//				dropSQL = new DropTablespace(dbName, tablespace);
//				dropSQL = new DropTablespace(this);
				dropSQL = DropTablespace.forTablespace(this);
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
//				createSQL = new SQLStatement.CreateLobTablespace(this);
				createSQL = CreateLobTablespace.forLobTablespace(this);
			}
			return createSQL;
		}
		public static final String NAME = "LOB";
	}
	
	//----------------------------------------------------------
	interface Index extends DB2Construct{
		String getName();
//		Table getTable();
		Indexable getIndexedTable();
	}
	static class UniqueConstraintIndex implements Index{
		
		private UniqueConstraintIndex(String indexName, Table table){
			Objects.requireNonNull(indexName);
			Objects.requireNonNull(table);
			this.name = indexName;
			this.table = table;
		}
		
		public UniqueConstraintIndex(Table table){
//			String indexName = String.format("PK_%S", table.getTableName());
			this(String.format("PK_%s", table.getTableName()), table);
		}
		public UniqueConstraintIndex(Table table, UniqueConstraintType constraintType){
			this(String.format("UK_%s", table.getTableName()), table);
			this.constraintType = constraintType;
		}
		
		@Override
		public AlterTableAddUniqueConstraint getCreateSQL() {
			if(null == createSQL){
//				createSQL = new CreatePrimaryKeyIndex(this);
//				createSQL = new AlterTableAddUniqueConstraint(this);
				createSQL = AlterTableAddUniqueConstraint.forUniqueConstraintIndex(this);
			}
			return createSQL;
		}

		@Override
		public AlterTableDropUniqueConstraint getDropSQL() {
			if(null == dropSQL){
//				dropSQL = new DropIndex(this);
//				dropSQL = new AlterTableDropUniqueConstraint(table);
				dropSQL = AlterTableDropUniqueConstraint.forTable(table);
			}
			return dropSQL;
			
		}

		@Override
		public String getName() {
			return name;
		}
		@Override
		public Table getIndexedTable(){
			return table;
		}
//		public String getTableName(){
//			return table.getTableName();
//		}
		
		public UniqueConstraintType getConstraintType() {
			return constraintType;
		}
		
		
		private AlterTableAddUniqueConstraint createSQL;
		private AlterTableDropUniqueConstraint dropSQL;

		private String name;
		private Table table;
		private UniqueConstraintType constraintType = UniqueConstraintType.PRIMARY_KEY;
//		public static final String NAME = "LOB";
	}
	
	enum UniqueConstraintType{
		PRIMARY_KEY("PRIMARY KEY")
		,UNIQUE_KEY("UNIQUE")
		;
		
		
		private UniqueConstraintType(String value){
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}

		private String value;
	}
	
	
//	static class AuxiliaryIndex implements Index{
//
//		public AuxiliaryIndex(Table table, AuxiliaryTable auxiliaryTable) {
//			Objects.requireNonNull(table);
//			Objects.requireNonNull(auxiliaryTable);
//			this.table = table;
//			this.auxiliaryTable = auxiliaryTable;
//			this.name = String.format("INDEX_%s", auxiliaryTable.getAuxiliaryTableName());
//		}
//		
//		
//		
//		@Override
//		public CreateAuxiliar getCreateSQL() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		@Override
//		public SQLStatement getDropSQL() {
//			return null;
//		}
//		@Override
//		public String getName() {
//			return name;
//		}
//		@Override
//		public AuxiliaryTable getIndexedTable() {
//			// TODO Auto-generated method stub
//			return auxiliaryTable;
//		}
//
//
//		private String name;
//		private Table table;
//		private AuxiliaryTable auxiliaryTable;
//		
//	}
	
	//----------------------------------------------------------
	//----------------------------------------------------------
	//----------------------------------------------------------
	//----------------------------------------------------------
	//----------------------------------------------------------
	
}
