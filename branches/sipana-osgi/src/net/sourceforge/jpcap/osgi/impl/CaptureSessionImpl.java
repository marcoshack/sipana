package net.sourceforge.jpcap.osgi.impl;

import net.sourceforge.jpcap.capture.PacketCapture;
import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.osgi.CaptureSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CaptureSessionImpl implements CaptureSession, Runnable {

    private String filter;
    private String device;
    private PacketListener listener;
    private boolean isPromiscuous;
    private int state;
    private PacketCapture capturer;
    private Thread thread;
    private Log logger;
    
    public CaptureSessionImpl() {
        filter = null;
        device = null;
        listener = null;
        isPromiscuous = false;
        capturer = new PacketCapture();
        logger = LogFactory.getLog(CaptureSession.class);
    }

    public String getFilter() {
        return filter;
    }

    public PacketListener getListener() {
        return listener;
    }
    
    public String getDevice() {
        return device;
    }
    
    public int getState() {
        return state;
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

    private void startCapture() throws Exception {
        capturer.open(device, CaptureSession.DEFAULT_SNAPLEN, isPromiscuous(),
                CaptureSession.DEFAULT_TIMEOUT);
        capturer.setFilter(filter, true);
        capturer.addPacketListener(listener);
        capturer.capture(CaptureSession.DEFAULT_COUNT);
    }

    private void stopCapture() throws Exception {
        capturer.endCapture();
        capturer.close();
    }
    
    public synchronized void start() throws Exception {
        logger.info("Starting " + this.toString());
        
        thread = new Thread(this);
        thread.setName("CaptureSession-" + listener.toString());
        thread.start();
        state = CaptureSession.RUNINNG;
        
        logger.info("Capture Session Id:" + this.hashCode() + " started");
    }
    
    public synchronized void stop() throws Exception {
        stopCapture();
        state = CaptureSession.IDLE;
    }
    
    public void run() {
        try {
            startCapture();         
        } 
        catch (Exception e) {
            logger.error("Fail running Capture Session: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sbInfo = new StringBuilder("Capture Session Id:");
        sbInfo.append(hashCode());
        sbInfo.append(" for listener ");
        sbInfo.append(listener);
        sbInfo.append(" on device ");
        sbInfo.append(getDevice());
        sbInfo.append(" with filter \"");
        sbInfo.append(getFilter());
        return sbInfo.toString();
    }
}
