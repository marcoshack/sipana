package org.sipana.client.capture;

public interface CaptureManager {
	public CaptureSession createCaptureSession(String filter, String device, CaptureListener listener) throws CaptureException;
	public void destroyCaptureSession(CaptureSession session);
	public void destroyCaptureSession(String sessionId);
}
