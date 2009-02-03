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
package org.sipana.agent.capture.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sipana.agent.capture.CaptureException;
import org.sipana.agent.capture.CaptureListener;
import org.sipana.agent.capture.CaptureManager;
import org.sipana.agent.capture.CaptureSession;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class CaptureManagerImpl implements CaptureManager {
	private static CaptureManagerImpl instance;
	private Map<String,CaptureSession> captureSessionMap;
	private Logger logger;
	
	private CaptureManagerImpl() {
		captureSessionMap = new HashMap<String,CaptureSession>();
		logger = Logger.getLogger(CaptureManager.class); 
	}
	
	public static CaptureManagerImpl getInstance() {
		if (instance == null) {
			instance = new CaptureManagerImpl();
		}
		
		return instance;
	}

	public CaptureSession createCaptureSession(String filter, String device,
			CaptureListener listener) throws CaptureException
	{
		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder("Creating capture session: ");
			sb.append("filter = ").append(filter);
			sb.append(", device = ").append(device);
			sb.append(", listener = ").append(listener.getClass().getSimpleName());
			logger.debug(sb);
		}
		
		if (device == null || filter == null || listener == null) {
			throw new CaptureException("Interface name, capture filter and "
					+ "listener cannot be null");
		}
		
		//CaptureSession session = new CaptureSessionSFjpcap(filter, device,listener);
		CaptureSession session = new CaptureSessionKFujii(filter, device,listener);
		addCaptureSession(session);
		
		return session;
	}

	public void addCaptureSession(CaptureSession session) {
		synchronized (captureSessionMap) {
			captureSessionMap.put(session.getId(), session);
		}
	}
	
	public void removeCaptureSession(CaptureSession session) {
		synchronized (captureSessionMap) {
			captureSessionMap.remove(session.getId());
		}
	}

    public List<CaptureSession> getCaptureSessionList() {
        List<CaptureSession> sessionList = new ArrayList<CaptureSession>();

        for (String sessionId : captureSessionMap.keySet()) {
            sessionList.add(captureSessionMap.get(sessionId));
        }

        return sessionList;
    }

}
