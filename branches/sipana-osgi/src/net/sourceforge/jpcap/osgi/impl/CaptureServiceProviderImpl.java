package net.sourceforge.jpcap.osgi.impl;

import java.util.LinkedList;

import net.sourceforge.jpcap.capture.PacketCapture;
import net.sourceforge.jpcap.osgi.CaptureServiceProvider;
import net.sourceforge.jpcap.osgi.CaptureSession;
import net.sourceforge.jpcap.osgi.impl.CaptureSessionImpl;

public class CaptureServiceProviderImpl implements CaptureServiceProvider {

    private LinkedList<CaptureSession> sessionList;
    
    public CaptureServiceProviderImpl() {
        sessionList = new LinkedList<CaptureSession>();
    }

    public CaptureSession createCaptureSession() {
        CaptureSessionImpl session = new CaptureSessionImpl();
        addSession(session);
        return session;
    }

    public LinkedList<CaptureSession> getCaptureSessionList() {
        LinkedList<CaptureSession> sessions;
        
        synchronized (sessionList) {
            sessions = (LinkedList<CaptureSession>) sessionList.clone();    
        }
        
        return sessions;
    }

    public String[] getDeviceList() {
        String[] deviceList;
        
        try {
            deviceList = PacketCapture.lookupDevices();
        } catch (Exception e) {
            deviceList = null;
        }
        
        return deviceList;
    }
    
    private void addSession(CaptureSession session) {
        synchronized (sessionList) {
            sessionList.add(session);
        }
    }
    
    private void removeSession(CaptureSession session) {
        synchronized (sessionList) {
            sessionList.remove(session);
        }
    }

}
