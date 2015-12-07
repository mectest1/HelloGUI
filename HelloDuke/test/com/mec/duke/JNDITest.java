package com.mec.duke;

import java.io.PrintStream;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import com.sun.jndi.ldap.LdapCtxFactory;

public class JNDITest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testJNDIConnection() throws Exception{
//		fail("Not yet implemented");
		//Use Apache Directory Studio that runs on localhost here;
		Hashtable<String, String> configProps = new Hashtable<>();
		configProps.put(Context.INITIAL_CONTEXT_FACTORY, LdapCtxFactory.class.getName());
		configProps.put(Context.PROVIDER_URL, "ldap://localhost:10389/dc=example,dc=com");
//		configProps.put(Context.PROVIDER_URL, "ldap://localhost:10389");
		
		DirContext dirContext = new InitialDirContext(configProps);
//		dirContext.list("/");
//		dirContext.getAttributes("/dc-example,dc=com");
		
//		String greeting = "Hello, World!";
//		String name = "greeting";
//		dirContext.bind(name, greeting);
//		out.println("Bind successfully");
//		String msg = (String) dirContext.lookup(name);
//		out.printf("Bound message: %s\n", msg);
		
		
	}

	
	
	
	private static final PrintStream out = System.out;
	
}
