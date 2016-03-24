package com.mec.duke;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.junit.Ignore;
import org.junit.Test;

import com.mec.duke.util.Config;
import com.mec.duke.util.Config2;
import com.mec.duke.util.LocalDateTimeXmlAdapter;



public class JAXBApplicationTest {

	@Ignore
	@Test
	public void testBasicMarshal() {
		TestConfig testConfig = getTestConfig();
		Config2.inst().saveConfig(this, "helloConfig", testConfig);
	}
	
	@Ignore
	@Test
	public void testUnMarshal(){
		TestConfig config = Config2.inst().loadConfig(this.getClass(), "helloConfig", TestConfig.class).get();
		out.println(config);
	}
	
	@Ignore
	@Test
	public void testMarshalWithEnhancedConfig(){
		TestConfig testConfig = getTestConfig();
		Config.config(this).save("helloConfig", testConfig);
		
		TestConfig testConfig2 = getTestConfig();
		testConfig2.setBirthDay(LocalDateTime.now());
		Config.config(this).save("helloConfig2", testConfig2);
	}
	
	@Ignore
	@Test
	public void testUnmarshalWithEnhancedConfig(){
		TestConfig testConfig = Config.config(this).load("helloConfig", TestConfig.class).get();
		TestConfig testConfig2 = Config.config(this).load("helloConfig2", TestConfig.class).get();
		
		out.println(testConfig);
		out.println(testConfig2);
	}
	
	
	@Test
	public void saveList(){
		TestConfigs configs = new TestConfigs();
		TestConfig tc = getTestConfig();
		tc.setBirthDay(LocalDateTime.now());
		configs.getConfigsList().add(tc);
		configs.getConfigsList().add(getTestConfig());
		configs.getConfigsList().add(getTestConfig());
		
		Config.config(this).save("helloConfigs", configs);
	}
	
	private TestConfig getTestConfig(){
		Map<String, String> config = new HashMap<String, String>();
		config.put("Hello", "World");
		config.put("Cafe", "Babe");
//		Config.inst().saveConfig(this, "helloConfig", config);	//unable to marshal type "java.util.HashMap" as an element because it is missing an @XmlRootElement annotation]
		
		List<String> list = Arrays.asList("Welcome", "to", "JAXB");
		
		Person p = new Person("John", "Smith");
		
		TestConfig testConfig = new TestConfig(config);
		testConfig.setParamList(list);
		testConfig.setPerson(p);
		return testConfig;
	}
	
	@XmlRootElement
	static class TestConfigs{
		List<TestConfig> configsList = new ArrayList<>();

//		@XmlElement(name="TestConfig")
		public List<TestConfig> getConfigsList() {
			return configsList;
		}

		public void setConfigsList(List<TestConfig> configsList) {
			this.configsList = configsList;
		}

		@Override
		public String toString() {
			return "TestConfigs [configsList=" + configsList + "]";
		}
	}
	
	
	@XmlRootElement
	static class TestConfig{	
		
		Map<String, String> params;
		List<String> paramList;
		Person person;
		LocalDateTime birthDay;
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
		@XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
		public LocalDateTime getBirthDay() {
			return birthDay;
		}
		public void setBirthDay(LocalDateTime birthDay) {
			this.birthDay = birthDay;
		}
		@Override
		public String toString() {
			return "TestConfig [params=" + params + ", paramList=" + paramList + ", person=" + person + ", birthDay="
					+ birthDay + "]";
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
