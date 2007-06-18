package net.sourceforge.jpcap.osgi;

import java.util.LinkedList;

public interface CaptureServiceProvider {
    public String[] getDeviceList();
    public CaptureSession createCaptureSession();
    public LinkedList<CaptureSession> getCaptureSessionList();
}
