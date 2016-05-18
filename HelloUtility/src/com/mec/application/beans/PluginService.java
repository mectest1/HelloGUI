package com.mec.application.beans;

public interface PluginService {

//	interface MsgService extends PluginService{
//		
//	}
	
	interface ViewService extends PluginService{
//		<ViewRoot, Controller> LoadViewResult<ViewRoot, Controller> loadView(String viewURL);
		void showNewStage(String viewURL, String title);
	}
	
	interface LogService extends PluginService{
		
	}
	

	/**
	 * @author MEC
	 *
	 * @param <ViewRoot> type of ViewRoot
	 * @param <Controller>	type of Controller for this view
	 */
	public static class LoadViewResult<ViewRoot, Controller>{
		ViewRoot viewRoot;
		Controller controller;
		public LoadViewResult(ViewRoot viewRoot, Controller controller) {
			super();
			this.viewRoot = viewRoot;
			this.controller = controller;
		}
		public ViewRoot getViewRoot() {
			return viewRoot;
		}
		public void setViewRoot(ViewRoot viewRoot) {
			this.viewRoot = viewRoot;
		}
		public Controller getController() {
			return controller;
		}
		public void setController(Controller controller) {
			this.controller = controller;
		}
	}
}
