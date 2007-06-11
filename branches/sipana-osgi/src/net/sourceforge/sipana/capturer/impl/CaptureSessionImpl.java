package net.sourceforge.sipana.capturer.impl;

import net.sourceforge.jpcap.capture.PacketCapture;
import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.sipana.capturer.CaptureSession;

public class CaptureSessionImpl implements CaptureSession {
    
    private String filter;
    private String device;
    private PacketListener listener;
    private boolean isPromiscuous;
    private PacketCapture capturer;
    
    public CaptureSessionImpl() {
        filter = null;
        device = null;
        listener = null;
        isPromiscuous = false;
        capturer = new PacketCapture();
    }

    public String getFilter() {
        return filter;
    }

    public PacketListener getListener() {
        return listener;
    }

    public boolean isPromiscuous() {
        return isPromiscuous;
    }

    public void setDevice(String device) {
        // TODO Auto-generated method stub
    }

    public void setFilter(String filter) {
        // TODO Auto-generated method stub
    }

    public void setListener(PacketListener listener) {
        // TODO Auto-generated method stub
    }

    public void setPromiscuous(boolean promiscuous) {
        // TODO Auto-generated method stub

    }

    public void start() throws Exception {
        // TODO Auto-generated method stub

    }

    public void stop() throws Exception {
        // TODO Auto-generated method stub

    }

}
