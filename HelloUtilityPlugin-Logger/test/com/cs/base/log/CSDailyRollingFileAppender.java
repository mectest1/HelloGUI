package com.cs.base.log;

import org.apache.log4j.DailyRollingFileAppender;

public class CSDailyRollingFileAppender extends DailyRollingFileAppender {

	String CSFile;

	public String getCSFile() {
		return CSFile;
	}

	public void setCSFile(String cSFile) {
		CSFile = cSFile;
	}
}
