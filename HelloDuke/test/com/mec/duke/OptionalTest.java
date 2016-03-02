package com.mec.duke;

import static org.junit.Assert.fail;

import java.util.Optional;

import org.junit.Test;

public class OptionalTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	
	
	static class Person{
		Optional<Car> car;

		public Optional<Car> getCar() {
			return car;
		}
	}
	
	static class Car{
		Optional<Insurance> insurance;

		public Optional<Insurance> getInsurance() {
			return insurance;
		}
	}
	
	static class Insurance{
		String name;

		public String getName() {
			return name;
		}
	}
}
