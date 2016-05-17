package com.mec.logger;

import java.io.PrintStream;
import java.util.concurrent.CompletableFuture;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SimpleSocketServer;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Ignore;
import org.junit.Test;

public class Log4jServerTest {

	@Ignore
	@Test
	public void testLog4jConfiguration(){
		//ref: https://wiki.apache.org/logging-log4j/Log4jXmlFormat
		DOMConfigurator.configure("config/log4j.xml");
		
		logger.info("Hello, Log4j");
		
		Logger l = Logger.getLogger("HelloLogger");
		l.info("Hello from custom logger");
	}
	/**
	 * 
	 * ref: http://howtodoinjava.com/log4j/log4j-socketappender-and-socket-server-example/
	 * ref2: http://stackoverflow.com/questions/1253586/can-two-log4j-fileappenders-write-to-the-same-file
	 * ref3: http://stackoverflow.com/questions/4046825/log4j-have-multiple-appenders-write-to-the-same-file-with-one-that-always-logs
	 */
	@Ignore
	@Test
	public void testLog4jServer() {
		String[] logServerParams = new String[]{
				"4712"
				,"config/log4j-server.xml"
		};
		CompletableFuture.runAsync(() -> {
			SimpleSocketServer.main(logServerParams);	//<- Seems like this is a blocking method
		});
		
		
		DOMConfigurator.configure("config/log4j.xml");
		logger.info("Hello, SocketAppender");
		
		Logger loggerToOut = Logger.getLogger("HelloLogger");
		loggerToOut.debug("debug");
		loggerToOut.info("info");
		
		/**
		 * output:
		 * log4j:ERROR Could not connect to remote log4j server at [localhost]. We will try again later.
		INFO  Log4jTest - Hello, SocketAppender					//<- rootLogger(on client) -> SocketAppender -> rootLogger(on server) -> consoleOut 
		INFO  SimpleSocketServer - Listening on port 4712		//<- rootLogger(on server) -> consoleOut
		INFO  HelloLogger - info								//<- HelloLogger -> consoleOut
		INFO  HelloLogger - info								//<- rootLogger(on server) -> consoleOut
		INFO  SimpleSocketServer - Waiting to accept a new client.	//<- rootLogger(on server) -> consoleOut

		 */
	}
	
	
	@Ignore
	@Test
	public void testLog4jServer2(){
		String[] logServerParams = new String[]{
				"4712"
				,"config/log4j-server.xml"
		};
		CompletableFuture.runAsync(() -> {
			SimpleSocketServer.main(logServerParams);	//<- Seems like this is a blocking method
		});
		
		
		DOMConfigurator.configure("config/log4j.xml");
		Logger loggerToError = Logger.getLogger("ErrorLogger");
		loggerToError.info("info");
		loggerToError.error("error");
		loggerToError.fatal("fatal");
	}
	
	@Ignore
	@Test
	public void testLoggerWrapper(){
		String[] logServerParams = new String[]{
				"4712"
				,"config/log4j-server.xml"
		};
//		CompletableFuture.runAsync(() -> {
			SimpleSocketServer.main(logServerParams);	//<- Seems like this is a blocking method
//		});
		
		
//		DOMConfigurator.configure("config/log4j.xml");
//		Logger loggerToError = Logger.getLogger("ErrorLogger");
//		loggerToError.info("info");
//		loggerToError.error("error");
//		loggerToError.fatal("fatal");
		
	}
	
	
	@Ignore
	@Test
	public void testLog4jClient(){
		String logClientXml = "//10.39.103.100/ibm/WebSphere/AppServer/profiles/AppSrv04/installedApps/SPDBENVNode04Cell/GTS.ear/ee_para/EEConfig/EE_Log_Client.xml";
		DOMConfigurator.configure(logClientXml);
		
		Logger eeLogger = Logger.getLogger("EELOG");
		out.println(eeLogger);
	}
	
	@Ignore
	@Test
	public void tsetLog4jAP(){
		String logServerXml = "//10.39.105.232/ee_para(TW_Q)/EEConfig/EE_Log_Config.xml";
		DOMConfigurator.configure(logServerXml);
		
		Logger eeLogger = Logger.getLogger("EELOG");
		out.println(eeLogger);
	}
	
	@Test
	public void testSystemParam(){
		out.println(System.getProperty("java.io.tmpdir"));	//C:\Users\MEC\AppData\Local\Temp\

	}
	
	static class LoggerWrapper{
	  	private Logger log=null;
	  	public LoggerWrapper(Logger log){
	  		this.log=log;
	  	}

		public void debug(Object message) {
		 
			log.debug(message);
		}

		public void error(Object message) {
			 
			log.error(message);
		}

		public void fatal(Object message) {
			 
			log.fatal(message);
		}

		public void info(Object message) {
			 
			log.info(message);

		}

		public void warn(Object message) {
			 
			log.warn(message);
		}

		public boolean isDebug(){
	    	 
	    	return log.isDebugEnabled();
	    }

		public boolean isInfo() {
		 
			return log.isInfoEnabled();
		}

		public void clearLogger() {
			log.removeAllAppenders();
		}
	}
	private static final Logger logger = Logger.getLogger(Log4jServerTest.class);
	
	
	
	private static final PrintStream out = System.out;
}
