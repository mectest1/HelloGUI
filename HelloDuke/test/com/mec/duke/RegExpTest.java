
package com.mec.duke;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RegExpTest {

//	@Ignore
	@Test
	public void testLogServersConfig(){
		String logServerConfig = ";10.39.105.232:4712;localhost:1234  ;10.39.103.167;10.39.105.30:;";
		
		LogServerConfig.getLogServers(logServerConfig).forEach(out::println);
		
		
	}
	
	
	static class LogServerConfig{
		String remoteHost;
//		int port = SocketAppender.DEFAULT_PORT;
		int port = 4560;
		InetAddress address;
		public LogServerConfig(String remoteHost){
			this.remoteHost = remoteHost;
		}
		public LogServerConfig(String remoteHost, int port) {
			super();
			this.remoteHost = remoteHost.trim();
			this.port = port;
		}
		public String getRemoteHost() {
			return remoteHost;
		}
		public int getPort() {
			return port;
		}
//		public InetAddress getAddress() {
//			if(null == address){
//				address = getAddressByName(remoteHost);
//			}
//			return address;
//		}
		@Override
		public String toString() {
			return "LogServerConfig [remoteHost=" + remoteHost + ", port=" + port + "]";
		}
		
		public static List<LogServerConfig> getLogServers(String logServersStr){
			List<LogServerConfig> retval = new ArrayList<LogServerConfig>();
			
//			if(StringUtil.isTrimEmpty(logServersStr)){
			if(null == logServersStr || logServersStr.trim().isEmpty()){
				return retval;
			}
			
			String[] logServers = logServersStr.split(LOG_SERVER_DELIMITER);
			for(String logServer : logServers){
				if(null == logServer || logServer.trim().isEmpty()){
					continue;
				}
				String[] serverAndPort = logServer.split(LOG_SERVER_PORT_DELIMITER);
				if(1 < serverAndPort.length){
					try{
						retval.add(new LogServerConfig(serverAndPort[0], Integer.parseInt(serverAndPort[1].trim())));
					}catch(NumberFormatException e){
//						LogLog.error(String.format("Invalid log server pot number: %s", serverAndPort[1]), e);
						out.println(String.format("Invalid log server pot number: %s", serverAndPort[1]));
					}
				}else if(0 < serverAndPort.length){
					retval.add(new LogServerConfig(serverAndPort[0]));
				}else{
					//Ignore empty string
				}
			}
			
			
			return retval;
			
		}
		
		public static final String LOG_SERVER_DELIMITER = ";";
		public static final String LOG_SERVER_PORT_DELIMITER = ":";
		
	}
	
	static final PrintStream out = System.out;
}
