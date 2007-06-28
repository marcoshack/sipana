package net.sourceforge.jpcap.osgi;

import java.util.LinkedList;

public interface CaptureServiceProvider {
    public String[] getDeviceList();
    public CaptureSession createCaptureSession() throws Exception ;
    public void destroyCaptureSession(CaptureSession session) throws Exception ;
    public LinkedList<CaptureSession> getCaptureSessionList();
}
