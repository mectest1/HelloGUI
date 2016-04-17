package com.mec.app.plugin.grammar;

import com.mec.app.plugin.grammar.DB2Construct.Table;

public interface SQLAction {

	
	SQLStatement getAction();
	
	default void execute(){
		//run the SQLStatemen from getAction();
	}
	
	interface SQLResult{
		
	}

	class SelectTableAction implements SQLAction{
		private SelectTableAction(Table table){
			this.table = table;
		}
		
		public static SelectTableAction from(Table table){
			return new SelectTableAction(table);
		}

		@Override
		public SQLStatement getAction() {
			// TODO Auto-generated method stub
			return null;
		}
		
		private Table table;
	}
	
	
	class InsertTableAction implements SQLAction{

		private InsertTableAction(){
			
		}
		
		
		@Override
		public SQLStatement getAction() {
			return null;
		}
		
	}
	
	
	
	class CreateTableAction implements SQLAction{
		

		@Override
		public SQLStatement getAction() {
			return null;
		}
		
	}
	
	class AlterTableAction implements SQLAction{

		@Override
		public SQLStatement getAction() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	class UpdateTableAction implements SQLAction{

		@Override
		public SQLStatement getAction() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	class DeleteRecordInTableAction implements SQLAction{

		@Override
		public SQLStatement getAction() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	class DropTableAction implements SQLAction{

		@Override
		public SQLStatement getAction() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
}
