package com.mec.duke;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

public class LambdaExpressionTest {

	@Test
	public void testFilter() {
		
		class A{
			
		}
		
		
		Function func = (derp)  -> {return null;};
	}

	
	public static class ObjectFilter{
		/**
		 * Filter the specified objects with the given filter;
		 * @param objects
		 * @param filter
		 * @return
		 */
		public <T> Collection<T> filter(Collection<T> objects, Predicate<T> filter){
			return objects.stream().filter(filter).collect(Collectors.toList());
		}
		
		void $(){	//<- valid function name;
			
		}
	}
}

