
public class Test16 {
	//contains Two nested trys 
	public void test() {		
		try { 
			 try {
				 System.out.println("");
			 }
			 finally{
				 System.out.println("nested finally one");
			 }
		}
		finally {
			 try {
				 System.out.println("");
			 }
			 finally{
				 System.out.println("nested finally one");
			 }
		}
		 	
	}
	
	

}
