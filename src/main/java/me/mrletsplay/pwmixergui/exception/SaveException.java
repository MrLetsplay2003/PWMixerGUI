package me.mrletsplay.pwmixergui.exception;

public class SaveException extends RuntimeException {

	private static final long serialVersionUID = -344468661495356452L;

	public SaveException() {
		super();
	}

	public SaveException(String message, Throwable cause) {
		super(message, cause);
	}

	public SaveException(String message) {
		super(message);
	}

	public SaveException(Throwable cause) {
		super(cause);
	}

}
