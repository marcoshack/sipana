package net.sourceforge.sipana.capturer;

import java.util.LinkedList;

public interface CaptureServiceProvider {
    public String[] getDeviceList();
    public CaptureSession createCaptureSession();
    public LinkedList<CaptureSession> getCaptureSessionList();
}
