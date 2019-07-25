
public class Test17 {
	
	
	public void test() {
		try {
			testhrowsNPE();
		}catch (Exception e) {
			System.out.println("over-catch exception");
		}
	}
	
	public void testhrowsNPE() throws NullPointerException{
		methodA();
		methodB();
	}
	
	public void methodA() {
		System.out.println("Call graph test 1");
	}
	
	public void methodB() {
		System.out.println("Call graph test 2");
	}
}
