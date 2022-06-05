package js.tiny.store;

public class ValidatorException extends Exception {
	private static final long serialVersionUID = 2495300801450321825L;

	public ValidatorException() {
		super();
	}

	public ValidatorException(String message) {
		super(message);
	}

	public ValidatorException(String message, Object... args) {
		super(String.format(message, args));
	}

	public ValidatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidatorException(Throwable cause) {
		super(cause);
	}

	public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
