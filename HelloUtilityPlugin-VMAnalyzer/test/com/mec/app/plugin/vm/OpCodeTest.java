package com.mec.app.plugin.vm;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

public class OpCodeTest {

	@Ignore
	@Test
	public void testListAllOpcodes() {
		
//		Arrays.stream(OpCode2.values()).forEach(c -> out.printf("%s - %s\n", c.ordinal(), c));
		Arrays.stream(OpCode.values()).forEach(c -> out.printf("%s - %s\n", c.ordinal(), c));
		
		Set<String> names2 = Arrays.stream(OpCode2.values()).map(OpCode2::name).collect(Collectors.toSet());
		Arrays.stream(OpCode.values()).filter(p -> !names2.contains(p.name())).forEach(out::println);
		
		out.println("--------");
		
		Set<String> names = Arrays.stream(OpCode.values()).map(OpCode::name).collect(Collectors.toSet());
		Arrays.stream(OpCode2.values()).filter(p -> !names.contains(p.name())).forEach(out::println);
	}
	
//	@Ignore
	@Test
	public void testOutputOpcodes(){
		Arrays.stream(OpCode.values()).forEach(p -> out.printf("%s - %s, %s\n", p.ordinal(), p.getCode(), p.name()));
	}

	
	
	
	private static final PrintStream out = System.out;
}
