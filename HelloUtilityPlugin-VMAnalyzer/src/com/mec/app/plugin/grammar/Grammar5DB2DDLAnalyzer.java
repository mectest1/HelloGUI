package com.mec.app.plugin.grammar;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mec.app.plugin.grammar.DB2Construct.Table;
import com.mec.app.plugin.grammar.DB2Construct.Table.Column;
import com.mec.app.plugin.grammar.DB2Construct.Table.ColumnDataType;
import com.mec.app.plugin.grammar.DB2Construct.Tablespace;
import com.mec.app.plugin.resource.MsgLogger;

public class Grammar5DB2DDLAnalyzer{

	private Grammar5DB2DDLAnalyzer(Path ddlFile, Path outputDir){
		this.ddlFile = ddlFile;
		this.outputDir = outputDir;
	}
	
	public static Grammar5DB2DDLAnalyzer inst(Path ddlFile, Path outputDir){
		Objects.requireNonNull(ddlFile);
		Objects.requireNonNull(outputDir);
		return new Grammar5DB2DDLAnalyzer(ddlFile, outputDir);
	}
	
	void prepareWorkFileTablespace(){
//		SQLStatement dropTS = new SQLStatement.DropTablespace(dbName, tablespace);
//		SQLStatement createTS = new SQLStatement.CreateTablespace(dbName, tablespace);
		
		Tablespace ts = new Tablespace(dbName, tablespace);
		logger.log(ts.getDropSQL().toString());
		logger.log(ts.getCreateSQL().toString());
	}
	
//	
//	
//	interface SQLStatement{
//		
//	}
//	
	
	static class DB2Importer{
		private DB2Importer(DBConfig importFrom, 
				DBConfig insertInto,
				List<Table> tables){
			Stream.of(importFrom, insertInto, tables).forEach(Objects::requireNonNull);
			
//			importTables = new HashSet<>(tables);
			importTables = tables;
//			sourceSession = importFrom.getConnection();
//			destSession = insertTo.getConnection();
			this.importFrom = importFrom;
			this.insertInto = insertInto;
		}
		
		public static DB2Importer from(DBConfig importFrom, 
				DBConfig insertTo,
				List<Table> tables
				){
			return new DB2Importer(importFrom, insertTo, tables);
		}
		
		/**
		 * Records will be extracted one by one, according to the order specified in <code>imortTables</code>
		 */
		public void extractAndImport(){
//			Connection sourceSession = null;
//			Connection destSession = null;
			try(Connection sourceSession = importFrom.getConnection();
				Connection destSession = insertInto.getConnection()){
				Statement selectStmt = sourceSession.createStatement();
				
				StringBuilder recordsAlreadyExsistedSQL = new StringBuilder("SELECT count(*) FROM ?;");
//				recordsAlreadyExsistsSQL.append(table.getTableNameWithSchema());
				
				PreparedStatement recordsAlreadyExsists = destSession.prepareStatement(recordsAlreadyExsistedSQL.toString()); 
				for(Table table : importTables){
					StringBuilder selectAll = new StringBuilder("SELECT ");
//					selectAll.append(table.getColumnsStr());
					selectAll.append(table.getColumns().stream().map(Column::getName).collect(Collectors.joining(", ")));
					selectAll.append(" FROM ");
					selectAll.append(table.getTableNameWithSchema());
					
					logger.log("Try to extract/import records for table: %s", table.getTableNameWithSchema());
					
					
					StringBuilder insertSQL = new StringBuilder("INSERT INTO ").append(table.getTableNameWithSchema());
					insertSQL.append(table.getColumnsStr());
//						insertSQL.append(" VALUES(");
					insertSQL.append(" VALUES");
					insertSQL.append(table.getColumns().stream().map(c -> "?").collect(Collectors.joining(",", "(", ")")));
//						insertSQL.append(")");
//					new StringBuilder("INSERT INTO ").append(table.getTableNameWithSchema());
//					insertSQL.append(" VALUES(");
//					insertSQL.append(")");
					PreparedStatement insertStmt = destSession.prepareStatement(insertSQL.toString());
					
//					PreparedStatement selectStmt = sourceSession.prepareStatement(selectAll.toString());
//					ResultSet selectResult = selectStmt.executeQuery();
					ResultSet selectResult = selectStmt.executeQuery(selectAll.toString());
					
//					recordsAlreadyExsistsSQL.append(" WHERE ");
//					List<Column> pkCols = table.getPrimaryKeys();
//					if(!pkCols.isEmpty()){
//					recordsAlreadyExsistsSQL.append(pkCols.stream().map(c -> c.getName() + " = ?").collect(Collectors.joining(" AND ", " WHERE ", "")));
//					}
					
					
//					StringBuilder recordsAlreadyExsistsSQL = new StringBuilder("SELECT count(*) FROM ?");
////					recordsAlreadyExsistsSQL.append(table.getTableNameWithSchema());
//					
//					PreparedStatement recordsAlreadyExsists = destSession.prepareStatement(recordsAlreadyExsistsSQL.toString());
//					recordsAlreadyExsistsSQL.append(";");
//					int recordsAlreadyExistedCount = 0;
//					if(pkCols.isEmpty()){
//						ResultSet recordsAlreadyExistsRS = recordsAlreadyExsists.executeQuery();
//						if(recordsAlreadyExistsRS.next()){
//							int recordsCount = recordsAlreadyExistsRS.getInt(1);
//							if(0 < recordsCount){
//								continue;
//							}
//						}
					recordsAlreadyExsists.setString(1, table.getTableNameWithSchema());
					int recordsAlreadyExistedCount = executeSQLAndGetCount(recordsAlreadyExsists);
//					}
					for(; 0 < recordsAlreadyExistedCount; --recordsAlreadyExistedCount){
						selectResult.next();
					}
					WHILE_SOURCE_RECORDS:
					while(selectResult.next()){
						
						//----------------
						//---Check for existed records
//						if(!pkCols.isEmpty()){
//							for(int i = 0; i < table.getPrimaryKeys().size(); ++i){
////							Column pkColumn = table.getPrimaryKeys.get(i);
//								setPreparedStatementParams(recordsAlreadyExsists, selectResult, pkCols.get(i).getColumnType().getDataType(), i + 1);
//							}
////							ResultSet recordsAlreadyExistsRS = recordsAlreadyExsists.executeQuery();
////							if(recordsAlreadyExistsRS.next()){
////								int recordsCount = recordsAlreadyExistsRS.getInt(1);
////								if(0 < recordsCount){
////									continue;
////								}
////							}
//							int recordExisted = executeSQLAndGetCount(recordsAlreadyExsists);
//							if(0 < recordExisted){
//								continue;
//							}
//						}else{
//							
//						}
						//---------------
						
						
						
						
						ColumnDataType colType = null;
						Column column; 
//						int prepIndex = 1;
//						for(Column column : table.getColumns()){
						int colIndex = 0;
						for(int i = 0; i < table.getColumns().size(); ++i){
//							int i = 0;
							column = table.getColumns().get(i);
							colIndex = i + 1;
							colType = column.getColumnType().getDataType();
//							switch(colType){
//								ColumnDataType.VARCHAR:
//									
//									break;
//							
//								default:
//									throw new IllegalArgumentException("unrecgonized column data type");
//							}
							setPreparedStatementParams(insertStmt, selectResult, colType, colIndex);
//							if(ColumnDataType.VARCHAR == colType){
//							if(colType.isStringColumn()){
//								insertStmt.setString(colIndex, selectResult.getString(colIndex));
//							}
//							else if(ColumnDataType.INTEGER == colType){
//								insertStmt.setInt(colIndex, selectResult.getInt(colIndex));
//							}else if(ColumnDataType.SMALLINT == colType){
//								insertStmt.setShort(colIndex, selectResult.getShort(colIndex));
//							}else if(ColumnDataType.BIGINT == colType){
//								insertStmt.setLong(colIndex, selectResult.getLong(colIndex));
//							}
//							else if(ColumnDataType.DECIMAL == colType){
//								insertStmt.setBigDecimal(colIndex, selectResult.getBigDecimal(colIndex));
//							}else if(ColumnDataType.DOUBLE == colType){
//								insertStmt.setDouble(colIndex, selectResult.getDouble(colIndex));
//							}
//							else if(ColumnDataType.CLOB == colType){
//								insertStmt.setClob(colIndex, selectResult.getClob(colIndex));
//							}else if(ColumnDataType.BLOB == colType){
//								insertStmt.setBlob(colIndex, selectResult.getBlob(colIndex));
//							}
//							else if(ColumnDataType.TIME == colType){
//								insertStmt.setTime(colIndex, selectResult.getTime(colIndex));
//							}else if(ColumnDataType.DATE == colType){
//								insertStmt.setDate(colIndex, selectResult.getDate(colIndex));
//							}else if(ColumnDataType.TIMESTAMP == colType){
//								insertStmt.setTimestamp(colIndex, selectResult.getTimestamp(colIndex));
//							}
//							else{
//								throw new IllegalArgumentException("unrecgonized column data type");
//							}
						}
						
//						logger.log("Execute insert SQL: %s\n", insertSQL);
						insertStmt.execute();
					}
					insertStmt.close();
					selectResult.close();
				}
				recordsAlreadyExsists.close();
				selectStmt.close();
				
			} catch (SQLException e) {
				throw new IllegalArgumentException(e);
			}
//			finally{
//				Stream.of(sourceSession, destSession).forEach(conn -> {
//					if(!(null == conn || conn.isClosed())){
//						conn.close();
//					}
//						
//				});
//			}
		}
		
		private int executeSQLAndGetCount(PreparedStatement stmt) throws SQLException{
			int retval = 0;
			ResultSet recordsAlreadyExistsRS = stmt.executeQuery();
			if(recordsAlreadyExistsRS.next()){
				retval = recordsAlreadyExistsRS.getInt(1);
//				if(0 < recordsCount){
					
//				}
			}
			return retval;
		}
		private void setPreparedStatementParams(PreparedStatement prepStmt, ResultSet resultSet, ColumnDataType colType, int colIndex) throws SQLException{
			if(colType.isStringColumn()){
				prepStmt.setString(colIndex, resultSet.getString(colIndex));
			}
			else if(ColumnDataType.INTEGER == colType){
				prepStmt.setInt(colIndex, resultSet.getInt(colIndex));
			}else if(ColumnDataType.SMALLINT == colType){
				prepStmt.setShort(colIndex, resultSet.getShort(colIndex));
			}else if(ColumnDataType.BIGINT == colType){
				prepStmt.setLong(colIndex, resultSet.getLong(colIndex));
			}
			else if(ColumnDataType.DECIMAL == colType){
				prepStmt.setBigDecimal(colIndex, resultSet.getBigDecimal(colIndex));
			}else if(ColumnDataType.DOUBLE == colType){
				prepStmt.setDouble(colIndex, resultSet.getDouble(colIndex));
			}
			else if(ColumnDataType.CLOB == colType){
				prepStmt.setClob(colIndex, resultSet.getClob(colIndex));
			}else if(ColumnDataType.BLOB == colType){
				prepStmt.setBlob(colIndex, resultSet.getBlob(colIndex));
			}
			else if(ColumnDataType.TIME == colType){
				prepStmt.setTime(colIndex, resultSet.getTime(colIndex));
			}else if(ColumnDataType.DATE == colType){
				prepStmt.setDate(colIndex, resultSet.getDate(colIndex));
			}else if(ColumnDataType.TIMESTAMP == colType){
				prepStmt.setTimestamp(colIndex, resultSet.getTimestamp(colIndex));
			}
			else{
				throw new IllegalArgumentException("unrecgonized column data type");
			}
		}
		
		private MsgLogger logger = MsgLogger.defaultLogger();
//		private Connection sourceSession;
//		private Connection destSession;
		private DBConfig importFrom;
		private DBConfig insertInto;
		private List<Table> importTables;
	}
	
	static class DBConfig{
		private DBConfig(String connectURL, String user, String password){
			this.connectURL = connectURL;
			this.user = user;
			this.password = password;
		}
		
		public static DBConfig conf(String connectURL, String user, String password){
			return new DBConfig(connectURL, user, password);
		}
		
		public Connection getConnection(){
			try {
				Class.forName(DB2_DRIVER);
				Connection conn = DriverManager.getConnection(connectURL, user, password);
				return conn;
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
			
		}
		private static final String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver";
		private String connectURL;
		private String user;
		private String password;
	}

	public static final String dbName = "EXIMDB";
	public static final String tablespace = "EXIM";		
	private String lobTSPattern = "EXIM%s";
	
	private Path ddlFile;
	private Path outputDir;
	private MsgLogger logger = MsgLogger.defaultLogger();
	
}
