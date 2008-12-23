/**
 * This file is part of Sipana project <http://sipana.org/>
 *
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sipana.client.capture;

import org.apache.log4j.Logger;

/**
 * Implements library independent code for capture session implementations.
 * 
 * @author Marcos Hack <marcoshack@gmail.com>
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

    protected abstract void startCapture() throws Exception;

    protected abstract void stopCapture() throws Exception;

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

    public synchronized void stop() throws Exception {
        logger.info("Stopping Capture Session Id=" + getId());
        stopCapture();
        logger.info("Capture Session Id=" + getId() + " stoped");
    }

    public void run() {
        try {
            setState(CaptureSession.STATE_RUNINNG);
            startCapture();

        } catch (NetworkInterfaceNotFoundException ne) {
            logger.error(ne.getMessage());
            setState(CaptureSession.STATE_ERROR);
            
        } catch (Exception e1) {
            logger.error("Unknown error running capture session", e1);
            setState(CaptureSession.STATE_ERROR);

        } finally {
            try {
                if (getState() == CaptureSession.STATE_RUNINNG) {
                    stop();
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
