import java.io.IOException;

public class Test15 {
	//contains Two nested trys 
	public void test() {		
		try { 
			File f= new File("someFile"); 		
			aMethod();
		}
		catch(FileNotFoundException e) {
			System.out.print(e.getMessage());
		}		
		catch(IOException e) {
			try {
				aMethod();
			}
			finally {
				System.out.println(e.getMessage());
			}
		}
		 	
	}
	
	
	public void aMethod() throws IOException {
		try {
			 System.out.println("Method contains try");
		 }
		catch(Exception e) {
			 throw new IOException(e);
		 }
	}

}
