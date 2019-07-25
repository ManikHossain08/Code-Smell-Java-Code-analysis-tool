package test.org.concordia.soen691.assignment;

import java.util.Vector;

public class Test {
		
	public Test() {
		System.out.println("Test object created");
	}
	
	public int someMethod(int a, int b) {
		/*
		 * NOTE this method contains illogical code!
		 * Its purpose is to test out more complex occurence cases of Destructive Wrapping anti-pattern
		 */
		int c = 0;
		if (a<b) {
			try {
				c = a+b;
			}catch (IllegalArgumentException e) {
				if (a>b) {
					b=a+a;
					throw new IndexOutOfBoundsException();
				} else {
					throw new IOException();
				}
			}
			
		}
		if (a>b) {
			try {
				c = a+b+1;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				for (i=0; i<10; i++) {
					e.printStackTrace();
					throw new Exception();
				}
			}
		}
		
		if (a!=b) {
			c=a;
			System.out.println("This should not be caught");			
		}
		
		if (a==b) {
			c=a;
			System.out.println("This should not be caught");			
		}
		
		return c;
	}
}
