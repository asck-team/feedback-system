package org.asck.web.exceptions;

public class ClientServiceRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -5557664177953056724L;

	public ClientServiceRuntimeException(String message) {
		super(message);
	}

	public ClientServiceRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
