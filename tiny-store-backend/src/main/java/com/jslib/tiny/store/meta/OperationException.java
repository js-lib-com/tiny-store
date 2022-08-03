package com.jslib.tiny.store.meta;

public class OperationException {
	private String type;
	private String cause;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
}
