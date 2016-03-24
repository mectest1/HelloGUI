package com.mec.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * Configuration Factory
 * @author MEC
 *
 */
public class Config {

	private Config(){
		configDir = Paths.get(Msg.get(this, "data.path"));
		createIfNotExists(configDir, Files::createDirectory);
	}
	
	private Path getDataPath(Object obj, String tag){
		return getDataPath(obj.getClass(), tag);
	}
	@SuppressWarnings("unchecked")
	public <T> Optional<T> loadConfig(Object obj, String tag, Class<? extends T> configObjClass){
		
		return loadConfig(obj.getClass(), tag, (Class<? extends T>) configObjClass);
	}
	/**
	 * @param objClassAsPath obj.class.name will be used as parent name for this config
	 * @param tag tag.xml to be saved
	 * @param configObj configuration object to be saved
	 */
	public <T> void saveConfig(Object objClassAsPath, String tag, T configObj){
		saveConfig(objClassAsPath.getClass(), tag, configObj);
	}
	
	public void createIfNotExists(Path path, CreatePathMethod pathCreateMethod){
		if(!Files.exists(path)){
			try {
//				Files.createDirectories(path);
				pathCreateMethod.create(path);
			} catch (IOException e) {
				logger.log(e);
			}
		}
	}
	//----------------------------------------------------------
	private Path getDataPath(Class<?> clazz, String tag){
		Path componentConfigDir = configDir.resolve(clazz.getName());
		createIfNotExists(componentConfigDir, Files::createDirectories);
		
		Path retval = componentConfigDir.resolve(String.format(CONFIG_FILENAME_PATTERN, tag));
		createIfNotExists(retval, Files::createFile);
		return retval;
	}
	
	
	//------------------------------------------------------------
	public <T> Optional<T> loadConfig(Class<?> clazz, String tag, Class<? extends T> configObjClass){
//		Path xmlFile = getDataPath(clazz, tag);
		Optional<T> retval;
		try {
			Path componentConfigDir = configDir.resolve(clazz.getName());
			Path xmlFile = componentConfigDir.resolve(String.format(CONFIG_FILENAME_PATTERN, tag));
			if(!(Files.exists(componentConfigDir) && Files.exists(xmlFile))){
				throw new FileNotFoundException(xmlFile.normalize().toString());
//				throw new FileNotFoundException(xmlFile.toRealPath().toString());
			}
			
			retval = Optional.of(BeanMarshal.loadFromXML(configObjClass, xmlFile));
		} catch (JAXBException | FileNotFoundException e) {
			logger.log(e);
			retval = Optional.empty();
		}
		return retval;
	}
	/**
	 * @param clazz class.name will be used as configuration parent
	 * @param tag configuration object will be saved into tag.xml
	 * @param configObj configuration object to be saved
	 */
	public <T> void saveConfig(Class<?> clazz, String tag, T configObj){
		Path xmlFile = getDataPath(clazz, tag);
		try {
			BeanMarshal.saveToXML(configObj, xmlFile);
		} catch (JAXBException e) {
			logger.log(e);
		}
	}
	
	
	/**
	 * The one and only getInstance() method;
	 * @return
	 */
	public static Config inst(){
		return instance;
	}
	
	//---------------------------------------
	public void setLogger(MsgLogger logger) {
		this.logger = logger;
	}

	private Path configDir;
//	private static Path configDir = Paths.get(Msg.get(Config.class, "data.path"));
//	static{
//		createIfNotExists(configDir, Files::createDirectories);
//	}
	private static final Config instance = new Config();
	private MsgLogger logger = MsgLogger.defaultLogger();
	private static final String CONFIG_FILENAME_PATTERN = Msg.get(Config.class, "config.fileName.pattern");
	private static interface CreatePathMethod{
		Path create(Path p) throws IOException;
	}
	
	/**
	 * policy that specifies the behavior when config file already exists;
	 * @author MEC
	 *
	 */
	public enum ConfigExistedPolicy{
		OVERRIDE	//default policy: overriding existing configuration file
		,BACKUP_OLD
		;
	}
	
	
	/**
	 * Configuration directories generate policy
	 * @author MEC
	 *
	 */
	public enum DirectoryOrganizPolicy{
		FLAT
		,HIERARCHY
		;
		
	}
	
	
	
	static class BeanMarshal{
		
		public static <T> void saveToXML(T obj, Path xmlFile) throws JAXBException, PropertyException{
			Stream.of(obj, xmlFile).forEach(Objects::requireNonNull);
			
			JAXBContext ctx = JAXBContext.newInstance(obj.getClass());
			Marshaller m = ctx.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			//
			m.marshal(obj, xmlFile.toFile());
		}
		
		@SuppressWarnings("unchecked")
		public static <T> T loadFromXML(Class<? extends T> clazz, Path xmlFile) throws JAXBException{
			Stream.of(clazz, xmlFile).forEach(Objects::requireNonNull);
			
			JAXBContext ctx = JAXBContext.newInstance(clazz);
			Unmarshaller um = ctx.createUnmarshaller();
			
			//
			return (T) um.unmarshal(xmlFile.toFile());
		}
	}

}
