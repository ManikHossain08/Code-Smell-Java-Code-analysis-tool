
public class Test13 {
	 
	public void test() {		
		try { 
			File f= new File("someFile"); 
		}
		catch(FileNotFoundException e) {
			try {
				tryMethod();
			}
			catch(IOException e){
				try {
					tryMethod();
				}
				catch(IOException e){
					
				}
			}
		}
		 	
	}
	
	public void tryMethod() throws IOException {
		System.out.print("find an exception");
	}
}
