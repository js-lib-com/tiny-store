package js.tiny.store.meta;

import js.util.Strings;

public class Version {
	private int major;
	private int minor;

	public Version() {
	}

	public Version(int major, int minor) {
		this.major = major;
		this.minor = minor;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	@Override
	public String toString() {
		return Strings.concat(major, '.', minor);
	}
}
