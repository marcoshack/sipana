package org.sipana.client.capture.impl;


import org.apache.log4j.Logger;
import org.sipana.client.capture.CaptureSession;

import net.sourceforge.jpcap.capture.PacketCapture;
import net.sourceforge.jpcap.capture.PacketListener;

public class CaptureSessionImpl implements CaptureSession, Runnable {

    private String filter;
    private String device;
    private PacketListener listener;
    private boolean isPromiscuous;
    private String id;
    private int state;
    private PacketCapture capturer;
    private Thread thread;
    private Logger logger;
    
    public CaptureSessionImpl(String filter, String device, PacketListener listener) {
        this.filter = filter;
        this.device = device;
        this.listener = listener;
        
        isPromiscuous = false;
        capturer = new PacketCapture();
        logger = Logger.getLogger(CaptureSession.class);
        state = CaptureSession.IDLE;
        id = String.valueOf(hashCode());
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

    private void stopCapture() {
        capturer.endCapture();
    }
    
    public synchronized void start() throws Exception {
        logger.info("Starting Capture Session Id=" + getId());
        
        state = CaptureSession.RUNINNG;
        thread = new Thread(this);
        thread.setName("CaptureSession");
        thread.start();
        
        logger.info("Capture Session Id=" + getId() + " started");
    }
    
    public synchronized void stop() {
        logger.info("Stopping Capture Session Id=" + getId());
        stopCapture();
        state = CaptureSession.IDLE;
        
        logger.info("Capture Session Id=" + getId() + " stoped");
    }
    
    public void run() {
        while (getState() == CaptureSession.RUNINNG) {
            try {
                startCapture();
            } 
            catch (Exception e) {
                logger.error("Fail running Capture Session: " + e.getMessage(), e);
                stop();
            }
            finally {
                capturer.close();
            }
        }
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Id = ").append(getId());
        sb.append(", filter = ").append(getFilter());
        sb.append(", device = ").append(getDevice());
        sb.append(", listener = ").append(getListener().getClass().getSimpleName());
        return sb.toString();
    }
}
