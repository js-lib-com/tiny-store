package js.tiny.store.tool;

public class FinalInteger {
	private int value;

	public FinalInteger() {
	}

	public FinalInteger(int value) {
		this.value = value;
	}

	public void set(int value) {
		this.value = value;
	}

	public int get() {
		return value;
	}

	public void increment() {
		++value;
	}

	public void increment(int increment) {
		value += increment;
	}

	public void decrement() {
		--value;
	}

	public void decrement(int decrement) {
		value += decrement;
	}
}