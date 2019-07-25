package test.org.concordia.soen691.assignment;

public class Test3{
	
	int value=0;
	public Test3() {
		
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.getValue();
	}
	
	@Override
	public boolean equals(Object t) {
		if (t==this)
			return true;
		
		if (!(t instanceof Test3))
			return false;
		
		if (t==null)
			return false;
		
		Test3 t3 = (Test3)t;
		return (this.value==t3.value);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode()*32;
	}
}
