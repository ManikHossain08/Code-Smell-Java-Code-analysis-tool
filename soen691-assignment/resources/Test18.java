
public class Test18 {
	public void test() {
		try {
			testhrowsNPE();
			methodB();
		}catch (RuntimeException e) {
			System.out.println("over-catch exception");
		}
		methodA();
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
