package org.sipana.client.capture;

import net.sourceforge.jpcap.capture.PacketListener;

public interface CaptureManager {
	public CaptureSession createCaptureSession(String filter, String device, PacketListener listener) throws CaptureException;
	public void destroyCaptureSession(CaptureSession session);
	public void destroyCaptureSession(String sessionId);
}
