package com.mec.app.plugin.filemanager;

import com.mec.app.plugin.filemanager.resources.Msg;
import com.mec.application.beans.PluginInfo.PluginContext;
import com.mec.application.beans.PluginInfo.PluginStart;
import com.mec.application.beans.PluginService.LoadViewResult;
import com.mec.application.beans.PluginService.ViewService;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * An XML structure comparator according to July's requirements;
 * <h3>Application of this plugin: </h3>
 * <p>
 * Parse descendants XMLs in these kind of directories: /*  /characteristic_path/{number}/*  /
 * </p>
 * <h4>Current status: unresponsive UI </h4>
 * <p>
 * <strong>Reason: </strong> heavy-load tasks take too much time to run, and they failed to be 
 * switched into background and keep running.
 * </p>
 * <h3>Next: resolve the lacking of responsiveness problem</h3>
 * <p>
 * <strong>Work to be done:</strong> a progress indicator is needed for long running tasks.
 * </p>
 * @author MEC
 *
 */
public class XMLParamComparator {

	
	
	@PluginStart
	public void start(PluginContext pctx){
		ViewService vs = pctx.getViewService();
//		LoadViewResult<BorderPane, XMLParamComparatorController> loadResult = vs.loadView(Msg.get(this, "view"));
		
//		showNewState(loadResult.getViewRoot(), Msg.get(this, "title"));
		String viewURL = Msg.get(this, "view");
		String title = Msg.get(this, "title");
		vs.showNewStage(viewURL, title);
	}
	
	
//	static Stage showNewState(Parent root, String title){
//		Scene scene = new Scene(root);
//		//Exception: java.lang.IllegalStateException: Not on FX application thread; currentThread = ForkJoinPool.commonPool-worker-1
//		Stage stage = new Stage();
//		stage.setScene(scene);
//		stage.sizeToScene();
//		stage.setTitle(title);
//		stage.show();
//		return stage;
//	}
}
