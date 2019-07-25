// nested try in try bl
public class Test12 {	 
	public void test() {		
		try {
			File f= new File("someFile"); 
			
			try {
				tryMethod();
			}
			catch(IOException ioexception) {
				System.out.print("catch ioexception");
			}
			
		}catch(FileNotFoundException e) {
			//todo
		}
	}
	
	public void tryMethod() throws IOException {
		System.out.print("throw an exception");
	}

}
