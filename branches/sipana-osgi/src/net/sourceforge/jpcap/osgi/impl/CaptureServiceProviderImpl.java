
/**
 * This file is part of Sipana project <http://sipana.sourceforge.net>
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

    public CaptureSession createCaptureSession() throws Exception {
        CaptureSessionImpl session = new CaptureSessionImpl();
        addSession(session);
        return session;
    }
    
    public void destroyCaptureSession(CaptureSession session) throws Exception {
        session.stop();
        removeSession(session);
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
