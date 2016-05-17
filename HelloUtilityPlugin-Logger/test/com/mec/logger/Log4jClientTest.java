package com.mec.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Ignore;
import org.junit.Test;

public class Log4jClientTest {

	@Ignore
	@Test
	public void test() {
		DOMConfigurator.configure("config/log4j.xml");
		Logger loggerToError = Logger.getLogger("ErrorLogger");
		loggerToError.info("info");
		loggerToError.error("error");
		loggerToError.fatal("fatal");
	}
	
	@Ignore
	@Test
	public void test2() {
		DOMConfigurator.configure("config/log4j-server.xml");
		Logger loggerToError = Logger.getLogger("ErrorLogger");
		loggerToError.info("info");
		loggerToError.error("error");
		loggerToError.fatal("fatal");
	}

}
