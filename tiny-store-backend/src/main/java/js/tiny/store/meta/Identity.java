package js.tiny.store.meta;

public class Identity extends EntityField {
	private boolean generated;

	public boolean isGenerated() {
		return generated;
	}

	public void setGenerated(boolean generated) {
		this.generated = generated;
	}
}
