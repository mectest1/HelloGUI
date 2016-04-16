package com.mec.app.plugin.grammar;

public interface SQLAction {

	
	SQLStatement getAction();
	
	default void execute(){
		//run the SQLStatemen from getAction();
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
	
	
	class SelectTableAction implements SQLAction{

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
