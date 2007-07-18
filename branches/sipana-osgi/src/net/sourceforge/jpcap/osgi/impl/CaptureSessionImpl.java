
/**
 * This file is part of Sipana project <http://sipana.org/>
 * 
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */ 

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
        state = CaptureSession.IDLE;
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
        state = CaptureSession.RUNINNG;
        thread = new Thread(this);
        thread.setName("CaptureSession-" + listener.toString());
        thread.start();
        
        logger.info("Capture Session Id:" + this.hashCode() + " started");
    }
    
    public synchronized void stop() throws Exception {
        state = CaptureSession.IDLE;
        stopCapture();
    }
    
    public void run() {
        while (getState() == CaptureSession.RUNINNG) {
            try {
                startCapture();         
            } 
            catch (Exception e) {
                logger.error("Fail running Capture Session: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sbInfo = new StringBuilder("Capture Session Id:");
        sbInfo.append(hashCode());
        sbInfo.append(" for listener ");
        sbInfo.append(listener.getClass().getName());
        sbInfo.append(" on device \"");
        sbInfo.append(getDevice());
        sbInfo.append("\" with filter \"");
        sbInfo.append(getFilter());
        sbInfo.append("\"");
        return sbInfo.toString();
    }
}
