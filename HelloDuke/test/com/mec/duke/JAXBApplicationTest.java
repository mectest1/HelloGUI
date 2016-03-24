package com.mec.duke;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Ignore;
import org.junit.Test;

import com.mec.duke.util.Config;



public class JAXBApplicationTest {

//	@Ignore
	@Test
	public void testBasicMarshal() {
		Map<String, String> config = new HashMap<String, String>();
		config.put("Hello", "World");
		config.put("Cafe", "Babe");
//		Config.inst().saveConfig(this, "helloConfig", config);	//unable to marshal type "java.util.HashMap" as an element because it is missing an @XmlRootElement annotation]
		
		List<String> list = Arrays.asList("Welcome", "to", "JAXB");
		
		Person p = new Person("John", "Smith");
		
		TestConfig testConfig = new TestConfig(config);
		testConfig.setParamList(list);
		testConfig.setPerson(p);
		Config.inst().saveConfig(this, "helloConfig", testConfig);
	}
	
	@Ignore
	@Test
	public void testUnMarshal(){
		TestConfig config = Config.inst().loadConfig(this.getClass(), "helloConfig", TestConfig.class).get();
		out.println(config);
	}
	
	@XmlRootElement
	static class TestConfig{	
		
		Map<String, String> params;
		List<String> paramList;
		Person person;
		
		public TestConfig() { //without it: TestConfig does not have a no-arg default constructor.
		}
		TestConfig(Map<String, String> params){
			Objects.requireNonNull(params);
			this.params = params;
		}
		public Map<String, String> getParams() {
			return params;
		}
		public void setParams(Map<String, String> params) {
			this.params = params;
		}
		public List<String> getParamList() {
			return paramList;
		}
		public void setParamList(List<String> paramList) {
			this.paramList = paramList;
		}
		
		public Person getPerson() {
			return person;
		}
		public void setPerson(Person person) {
			this.person = person;
		}
		@Override
		public String toString() {
			return "TestConfig [params=" + params + ", paramList=" + paramList + ", person=" + person + "]";
		}
	}
	
	static class Person{
		String first;
		String last;
		
		public Person() {
			super();
		}
		public Person(String first, String last) {
			super();
			this.first = first;
			this.last = last;
		}
		public String getFirst() {
			return first;
		}
		public void setFirst(String first) {
			this.first = first;
		}
		public String getLast() {
			return last;
		}
		public void setLast(String last) {
			this.last = last;
		}
		@Override
		public String toString() {
			return "Person [first=" + first + ", last=" + last + "]";
		}
	}
	
	private static final PrintStream out = System.out;

}
