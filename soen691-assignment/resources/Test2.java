package test.org.concordia.soen691.assignment;

public class Test2 {

	int value = 0;
	List<String> innerList = new ArrayList<String>();

	public Test2() {

	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.getValue();
	}

	@Override
	public boolean equals(Object t) {
		if (t == this)
			return true;

		if (t == null)
			return false;

		if (!(t instanceof Test2)) {
			try {
				i = 0;
			} catch (InvalidArgumentException e) {
				for (String item : innerList) {
					if (item.equals("Exception")) {
						throw new Exception();
					}
				}
				throw new IndexOutOfBoundsException();
			}
			return false;
		}

		Test2 t2 = (Test2) t;
		return (this.value == t2.value);
	}
}
