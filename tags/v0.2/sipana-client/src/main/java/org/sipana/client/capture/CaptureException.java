package org.sipana.client.capture;

public class CaptureException extends Exception {
	private static final long serialVersionUID = -260000377965849641L;

	public CaptureException(String message) {
		super(message);
	}
	
	public CaptureException(String message, Throwable cause) {
		super(message, cause);
	}
}
