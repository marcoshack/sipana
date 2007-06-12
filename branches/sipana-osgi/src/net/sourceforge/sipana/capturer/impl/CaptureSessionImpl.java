package net.sourceforge.sipana.capturer.impl;

import net.sourceforge.jpcap.capture.PacketCapture;
import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.sipana.capturer.CaptureSession;

public class CaptureSessionImpl implements CaptureSession {
    
    private String filter;
    private String device;
    private PacketListener listener;
    private boolean isPromiscuous;
    private int state;
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
        this.device = device;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setListener(PacketListener listener) {
        this.listener = listener;
    }

    public void setPromiscuous(boolean promiscuous) {
        isPromiscuous = promiscuous;
    }

    public void start() throws Exception {
        capturer.open(device, CaptureSession.DEFAULT_SNAPLEN, isPromiscuous(),
                CaptureSession.DEFAULT_TIMEOUT);
        capturer.setFilter(filter, true);
        capturer.addPacketListener(listener);
        capturer.capture(CaptureSession.DEFAULT_COUNT);
    }

    public void stop() throws Exception {
        capturer.endCapture();
        capturer.close();
    }

}
