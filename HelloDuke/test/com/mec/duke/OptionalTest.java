package com.mec.duke;

import java.io.PrintStream;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;

public class OptionalTest {

	@Ignore
	@Test
	public void testOptionalValue() {
		Optional<Car> optCar = Optional.empty();
		
		
		Car car = null;
		Optional<Car> optCar2 = Optional.ofNullable(car);	//<- If car were null, 
											//the resulting Optional object would be empty
		
	}

	
	@Ignore
	@Test
	public void testOptionalMap(){
		Insurance insurance = null;
		Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
		Optional<String> name = optInsurance.map(Insurance::getName);
	}
	
	
	@Ignore
	@Test(expected=NullPointerException.class)
	public void testFlatMap(){
		Person person = new Person();
		
		Optional<Person> optPerson = Optional.of(person);
		
		//<- Optional failed
//		Optional<String> name = optPerson.map(Person::getCar).map(Car::getInsurance).map(Insurance::getName); 
		
		Optional<String> name = optPerson.flatMap(Person::getCar)	//<- NullPointerException will be thrown here, since null == person.getCar();
				.flatMap(Car::getInsurance)
				.map(Insurance::getName);
		out.printf("Name of the insurance company: %s\n", name.orElse("Empty!"));	//<- Since an exception has been thrown, no output will be present;
		
		//
	}
	
	@Ignore
	@Test
	public void testFlatMap2(){
		Person person = new Person();
		
		Optional<Person> optPerson = Optional.ofNullable(person);
		Optional<String> name = optPerson.flatMap(Person::getCar2).flatMap(Car::getInsurance2).map(Insurance::getName);	//<- Correct use case;
		out.printf("Name of the insurance2 company: %s\n", name.orElse("Empty!"));
	}
	
	
	@Test
	public void testOptionalFilter(){
		Optional<String> p = Optional.of("Derp");
		out.println(p.filter("Derpia"::equals).orElse("It's not Derpia!"));
				
	}
	
	//--------------------------------
	static class Person{
		Optional<Car> car;
		Optional<Car> car2 = Optional.empty();
		int age = 0;

		public Optional<Car> getCar() {
			return car;
		}

		public Optional<Car> getCar2() {
			return car2;
		}
		
		int getAge(){
			return age;
		}
		public static Insurance findCheapestInsurance(Person person, Car car){
			Insurance retval = null;
			//Query services provided by the different insurance companies
			//Compare all those data;
			return retval;
		}
		
		public static Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car){
			return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
		}
		
		static String getCarInsuranceName(Optional<Person> person, int minAge){
			return person.filter(p -> minAge <= p.getAge())
					.flatMap(Person::getCar)
					.flatMap(Car::getInsurance)
					.map(Insurance::getName)
					.orElse("Unknown");
		}
	}
	
	static class Car{
		Optional<Insurance> insurance;
		Optional<Insurance> insurance2 = Optional.empty();
		
		public Optional<Insurance> getInsurance() {
			return insurance;
		}

		public Optional<Insurance> getInsurance2() {
			return insurance2;
		}
	}
	
	static class Insurance{
		String name;

		public String getName() {
			return name;
		}
	}
	
	
	private static final PrintStream out = System.out;
}
