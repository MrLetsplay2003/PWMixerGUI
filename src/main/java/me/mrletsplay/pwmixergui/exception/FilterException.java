package me.mrletsplay.pwmixergui.exception;

public class FilterException extends RuntimeException {

	private static final long serialVersionUID = 1799974463786611751L;

	public FilterException() {
		super();
	}

	public FilterException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilterException(String message) {
		super(message);
	}

	public FilterException(Throwable cause) {
		super(cause);
	}

}
