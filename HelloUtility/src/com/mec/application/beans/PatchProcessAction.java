package com.mec.application.beans;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlRootElement;

import com.mec.application.views.PatchReleaseController;
import com.mec.resources.Config;
import com.mec.resources.FileParser;
import com.mec.resources.JarTool;
import com.mec.resources.Msg;
import com.mec.resources.MsgLogger;

public interface PatchProcessAction {
	
//	PatchProcessAction from(MsgLogger logger, Path actionConfig);
	void proceed(PatchProcessContext ppctx) throws Exception;
	/**
	 * Simple action name of this process action for build-in actions (RelocateJars, WriteReadMe, etc), 
	 * or the full class name for this PatchProcessAction, which should provide a constructor 
	 * that accepts {@link MsgLogger} as the only parameter. 
	 * <pre>
	 * If no such constructor exists, then the default constructor will be invoked.
	 * </pre>
	 * 
	 * @return name of current process action, defult to full class name
	 */
	default String getName(){
		return getClass().getName();
	};
	
	/**
	 * Get all the patch process actions as specified in <code>data/{PatchReleaseController}/ProcessActions.xml</code>.
	 * If such config file doesn't exist, then a default </code>ProcessActions.xml</code> that bears <em>RelocateJars</em>
	 * and <em>WriteReadMe</em> in it will be generated automatically.
	 * @param logger
	 * @return
	 */
	static List<PatchProcessAction> getPatchProcessActions(final MsgLogger logger){
		List<PatchProcessAction> retval = new ArrayList<PatchProcessAction>();
		retval.add(newWriteToFilesToJarAction(logger));
		
		String configTag = Msg.get(PatchReleaseController.class, "config.processActions");
		Optional<PatchProcessActionConfigs> actionsConfigOpt =
				Config.defaultData().component(PatchReleaseController.class)
				.load(configTag, PatchProcessActionConfigs.class);
		
		if(actionsConfigOpt.isPresent()){
			PatchProcessActionConfigs actionsConfigs = actionsConfigOpt.get();
			List<PatchProcessAction> actionsFromConfig = actionsConfigs.getActionsConfig().stream()
				.filter(config -> config.isToggledOn())
				.map(config -> {
					if(Msg.get(PatchProcessAction.class, "action.RelocateJars").equals(config.getName())){
						return newRelocateJarsAction(logger);
					}else if(Msg.get(PatchProcessAction.class, "action.WriteReadMe").equals(config.getName())){
						return newWriteReadMeAction(logger);
					}else{
						String actionClassName = config.getName();
						
						@SuppressWarnings("rawtypes")
						Constructor actionConstructor;
						try {
							PatchProcessAction actionInst = null;
							try{
								actionConstructor = Class.forName(actionClassName).getConstructor(MsgLogger.class);
								actionInst = (PatchProcessAction) actionConstructor.newInstance(logger);
							}catch(NoSuchMethodException e){
								actionInst = (PatchProcessAction) Class.forName(actionClassName).newInstance();
							}
							return actionInst;
						} catch (ClassNotFoundException 
								| InvocationTargetException | InstantiationException
								| IllegalAccessException
								e) {
							// TODO Auto-generated catch block
//							e.printStackTrace();
							logger.log(e);
							return null;
						}
					}
				}).filter(actionInst -> null != actionInst).collect(Collectors.toList());
			retval.addAll(actionsFromConfig);
		}else{
			List<PatchProcessAction> actionsFromConfig = new ArrayList<>();
			actionsFromConfig.add(newRelocateJarsAction(logger));
			actionsFromConfig.add(newWriteReadMeAction(logger));
			
			PatchProcessActionConfigs actionsConfig = new PatchProcessActionConfigs();
			actionsConfig.setActionsConfig(actionsFromConfig.stream().map(PatchProcessActionConfig::from).collect(Collectors.toList()));
			Config.defaultData().component(PatchReleaseController.class).save(configTag, actionsConfig);
			
			retval.addAll(actionsFromConfig);
		}
		
		return retval;
	}
	
	static PatchProcessAction newWriteToFilesToJarAction(MsgLogger logger){
		return new WriteFilesToJarAction(logger);
	}
	
	static PatchProcessAction newRelocateJarsAction(MsgLogger logger){
		return new RelocateJarsAction(logger);
	}
	
	static PatchProcessAction newWriteReadMeAction(MsgLogger logger){
		return new WriteReadMeAction(logger);
	}
	
	class WriteFilesToJarAction implements PatchProcessAction{

		private WriteFilesToJarAction(MsgLogger logger){
			this.logger = logger;
			this.jarTool = JarTool.newInstance(logger);
		}
		
//		@Override
//		public CreateTargetDirectoryAction from(Path actionConfig) {
//			return null;
//		}
		
		@Override
		public void proceed(PatchProcessContext ppctx) throws Exception{
//			try {
				Path workspaceDir = ppctx.getWorkspaceDirectory();
				List<String> modifyList = ppctx.getModifyList();
				Path patchReleaseDir = ppctx.getTargetDirectory();
				Path stashDirectory = ppctx.getStashDirectory();
				Map<String, Set<String>> modifyListMap = FileParser.parseModifyList(modifyList);
				for (String projectName : modifyListMap.keySet()) {
					Set<String> sourceFileList = modifyListMap.get(projectName);
					//				jarTool.writeFilesToJar(workspaceDir, projectName, sourceFileList, patchReleaseDir);
					//				jarTool.writeFilesToJar(workspaceDir, projectName, sourceFileList, patchReleaseDir, getDelPath());
					jarTool.writeFilesToJar(workspaceDir, projectName, sourceFileList, patchReleaseDir, stashDirectory);
					logger.log(FileParser.NEWLINE);
				} 
//			} catch (Exception e) {
//				throw new IllegalArgumentException(e);
//			}
			
		}
		
		@Override
		public String getName() {
			return Msg.get(PatchProcessAction.class, "action.WriteFilesToJars");
		}

		private MsgLogger logger;
		private JarTool jarTool;
	}
	
	class RelocateJarsAction implements PatchProcessAction{
		
		private MsgLogger logger;
		private RelocateJarsAction(MsgLogger logger){
			this.logger = logger;
		}

		@Override
		public void proceed(PatchProcessContext ppctx) throws Exception{
			Path patchReleaseDir = ppctx.getTargetDirectory();
			Path stashDirectory = ppctx.getStashDirectory();
			
			Path eeLibDir = null;
			for(Path jarFile : Files.list(patchReleaseDir).collect(Collectors.toList())){
				String jarFileName = jarFile.getFileName().toString();
				if(JarTool.WEB_CONTENT_JAR.matcher(jarFileName).matches()){
					//leave WebContent.jar in patch release directory (aka current directory)
				}else if(JarTool.EE_LIB_JAR.matcher(jarFileName).matches()){
					if(null == eeLibDir){
						eeLibDir = patchReleaseDir.resolve(Msg.get(PatchReleaseController.class, "path.EE_LIB"));
//						JarTool.tryMoveOldToDelDirectory(eeLibDir, getDelPath());
						JarTool.tryMoveOldToDelDirectory(eeLibDir, stashDirectory);
//						if(Files.notExists(eeLibDir)){
//							Files.createDirectory(eeLibDir);
//							JarTool.validateDirectory(eeLibDir, String.format(Msg.get(this, "path.EE_LIB.error"), eeLibDir.toAbsolutePath()));
//						}
						Config.of(PatchReleaseController.class).createIfNotExists(eeLibDir, Files::createDirectories);
						JarTool.validateDirectory(eeLibDir, String.format(Msg.get(this, "path.EE_LIB.error"), eeLibDir.toAbsolutePath()));
					}
					logger.log(String.format(Msg.get(PatchReleaseController.class, "info.moveJar"), jarFileName, eeLibDir.toAbsolutePath()));
					Files.move(jarFile,eeLibDir.resolve(jarFileName));
				}else{
					logger.log(String.format(Msg.get(PatchReleaseController.class, "info.dontMove"), jarFileName, patchReleaseDir));
				}
			}
			
		}

		@Override
		public String getName() {
			return Msg.get(PatchProcessAction.class, "action.RelocateJars");
		}
		
	}
	
	
	class WriteReadMeAction implements PatchProcessAction{
		private MsgLogger logger;
		private WriteReadMeAction(MsgLogger logger){
			this.logger = logger;
		}

		@Override
		public void proceed(PatchProcessContext ppctx) throws Exception {
			Path patchReleaseDir = ppctx.getTargetDirectory();
			List<String> contentLines = ppctx.getModifyList();
			Path stashDirectory = ppctx.getStashDirectory(); 
			
//			Path readMe = patchReleaseDir.resolve(Msg.get(this, "path.README"));
			Path readMe = patchReleaseDir.resolve(Msg.get(PatchReleaseController.class, "path.README"));
//			JarTool.tryMoveOldToDelDirectory(readMe, getDelPath());
			JarTool.tryMoveOldToDelDirectory(readMe, stashDirectory);
//			Config.of(PatchReleaseController.class).createIfNotExists(readMe, Files::createFile);
			Config.defaultData().createIfNotExists(readMe, Files::createFile);
			Files.write(readMe, contentLines);
		}

		@Override
		public String getName() {
			return Msg.get(PatchProcessAction.class, "action.WriteReadMe");
		}
		
	}
	
	public static interface PatchProcessContext{
		Path getWorkspaceDirectory();
		Path getTargetDirectory();
		List<String> getModifyList();
		/**
		 * Move old patch files into this directory. Note that this 
		 * may be a <strong>relative</strong> path 
		 * @return
		 */
		Path getStashDirectory();	//
		
		static PatchProcessContext newInstance(Path workspaceDirectory, List<String> modifyList, 
				Path patchReleaseDirectory, Path stashDirectory){
			return new PatchProcessContextImpl(workspaceDirectory, modifyList, patchReleaseDirectory, stashDirectory);
		}
		
		
		class PatchProcessContextImpl implements PatchProcessContext{
			private PatchProcessContextImpl(Path workspaceDirectory, List<String> modifyList, Path targetDirectory,
					Path stashDirectory) {
				super();
				this.workspaceDirectory = workspaceDirectory;
				this.targetDirectory = targetDirectory;
				this.modifyList = modifyList;
				this.stashDirectory = stashDirectory;
			}

			public Path getWorkspaceDirectory() {
				return workspaceDirectory;
			}
			public Path getTargetDirectory() {
				return targetDirectory;
			}
			public List<String> getModifyList() {
				return modifyList;
			}
			
			public Path getStashDirectory() {
				return stashDirectory;
			}

			private Path workspaceDirectory;
			private Path targetDirectory;
			private List<String> modifyList;
			private Path stashDirectory;
		}
	}
	
	@XmlRootElement
	class PatchProcessActionConfigs{
		List<PatchProcessActionConfig> actionsConfig = new ArrayList<>();
		public List<PatchProcessActionConfig> getActionsConfig() {
			return actionsConfig;
		}
		public void setActionsConfig(List<PatchProcessActionConfig> actionsConfig) {
			this.actionsConfig = actionsConfig;
		}
		@Override
		public String toString() {
			return "PatchProcessActionConfigs [actionsConfig=" + actionsConfig + "]";
		}
	}
	
//	@XmlRootElement
	class PatchProcessActionConfig{
		boolean toggledOn = true;
		String name;
		Map<String, String> attributes = new HashMap<>();
		public boolean isToggledOn() {
			return toggledOn;
		}
		public void setToggledOn(boolean toggle) {
			this.toggledOn = toggle;
		}
		public Map<String, String> getAttributes() {
			return attributes;
		}
		public void setAttributes(Map<String, String> attributes) {
			this.attributes = attributes;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "PatchProcessActionConfig [toggledOn=" + toggledOn + ", name=" + name + ", attributes=" + attributes
					+ "]";
		}
		static PatchProcessActionConfig from(PatchProcessAction action){
			PatchProcessActionConfig retval = new PatchProcessActionConfig();
			retval.setName(action.getName());
			return retval;
		}
	}
}
