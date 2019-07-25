
public class Test11 {
	//contains only two trys, but not nested.
	public void test() {
		tryInMethod();
		
		try {
			System.out.println("try");
		}catch(Exception e) {
			//todo
		}
	}
	
	public void tryInMethod() {
	 try {
		 System.out.println("Method contains try");
	 }
	 catch(Exception e) {
		 //todo
	 }
		
		 
	}

}
