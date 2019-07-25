import java.io.FileNotFoundException;

public class Test14 {
	public void test() {		
		try { 
			File f= new File("someFile"); 
		}
		catch(FileNotFoundException e) {
			System.out.print(e.getMessage());
		}
		finally {
			
			try {
				tryMethod();
			}
			finally{
				//todo
			}
		}
		 	
	}
	
	public void tryMethod() throws IOException {
		System.out.print("find an exception");
	}
	 

}
