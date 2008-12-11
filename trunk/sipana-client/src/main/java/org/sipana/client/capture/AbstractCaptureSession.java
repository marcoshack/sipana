package org.sipana.client.capture;

import org.apache.log4j.Logger;

/**
 * Implements library independent code for capture session implementations.
 * 
 * @author mhack
 */
public abstract class AbstractCaptureSession implements CaptureSession,
        Runnable {

    private String filter;
    private String device;
    private CaptureListener listener;
    private boolean isPromiscuous;
    private String id;
    private int state;
    private Thread thread;
    protected Logger logger;

    public AbstractCaptureSession(String filter, String device,
            CaptureListener listener) {
        this.filter = filter;
        this.device = device;
        this.listener = listener;

        isPromiscuous = false;
        logger = Logger.getLogger(this.getClass());
        id = String.valueOf(hashCode());
        setState(CaptureSession.STATE_IDLE);
    }

    protected abstract void startCapture() throws CaptureException;

    protected abstract void stopCapture() throws CaptureException;

    public String getFilter() {
        return filter;
    }

    public CaptureListener getListener() {
        return listener;
    }

    public String getDevice() {
        return device;
    }

    public int getState() {
        synchronized (this) {
            return state;
        }
    }

    public void setState(int newState) {
        synchronized (this) {
            state = newState;
        }
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

    public void setListener(CaptureListener listener) {
        this.listener = listener;
    }

    public void setPromiscuous(boolean promiscuous) {
        isPromiscuous = promiscuous;
    }

    public synchronized void start() throws CaptureException {
        logger.info("Starting Capture Session Id=" + getId());

        if (getState() == CaptureSession.STATE_IDLE) {
            state = CaptureSession.STATE_RUNINNG;
            thread = new Thread(this);
            thread.setName("CaptureSession-" + getId());
            thread.start();

        } else {
            throw new CaptureException("Illegal state (" + getState()
                    + "). Cannot start capture session.");
        }
    }

    public synchronized void stop() throws CaptureException {
        logger.info("Stopping Capture Session Id=" + getId());
        stopCapture();
        logger.info("Capture Session Id=" + getId() + " stoped");
    }

    public void run() {
        try {
            setState(CaptureSession.STATE_RUNINNG);
            startCapture();

        } catch (Exception e1) {
            logger.error("Fail running Capture Session", e1);
            setState(CaptureSession.STATE_ERROR);

        } finally {
            try {
                stop();
                
                if (getState() == CaptureSession.STATE_RUNINNG) {
                    setState(CaptureSession.STATE_IDLE);
                }

            } catch (Exception e) {
                logger.error("Fail stopping Capture Session", e);
                setState(CaptureSession.STATE_ERROR);
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
        sb.append(", listener = ").append(getListener().getClass().getName());
        return sb.toString();
    }
}
