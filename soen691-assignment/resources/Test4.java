package test.org.concordia.soen691.assignment;

import java.util.Vector;

public class Test4 {
	
	class MyClass4{
		int value = 0;
		String s = "Default";
		
		public MyClass4() {
			
		}
		
		public MyClass4(int value) {
			this.value=value;
		}
		 public int getValue() {
			 return this.value;
		 }
		 
		 public void setValue(int value) {
			 this.value=value;
		 }
		
	}
	
	public Test4() {
		System.out.println("Test object created");
	}
	
	public int sumTwoIntegers(int a, int b) {
		int c = 0;

		String s1 = "string 1";
		String s2 = "String 2";
		
		MyClass4 m1 = new MyClass4(1);
		MyClass4 m2 = new MyClass4(2);
		
		if (s1==s2) {
			c=100;
			System.out.println("This should be caught");
		}
		
		if (s1.equals(s2) != true) {
			c=200;
			System.out.println("This should not be caught");
		}
		
		if (m1==m2) {
			c=300;
			System.out.println("This should be caught");			
		}
		
		if (m1.getValue() != 0) {
			c = 400;
			System.out.println("This should not be caught");
		}
		
		return c;
	}
}
