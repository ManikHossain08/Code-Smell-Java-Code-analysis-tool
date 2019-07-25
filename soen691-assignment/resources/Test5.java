package test.org.concordia.soen691.assignment;

public class Test5{
	public Test5() {
		System.out.println("Test object created");
	}
	
	public void aMethod() {
		
		try {
			File f= new File("someFile"); 
		}
		catch(FileNotFoundException e) {
			throw new IOException("destructive wrapping");
		}		
	}
}