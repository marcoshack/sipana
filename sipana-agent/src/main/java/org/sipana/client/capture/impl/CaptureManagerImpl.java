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
package org.sipana.client.capture.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sipana.client.capture.CaptureException;
import org.sipana.client.capture.CaptureListener;
import org.sipana.client.capture.CaptureManager;
import org.sipana.client.capture.CaptureSession;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class CaptureManagerImpl implements CaptureManager {
	private static CaptureManagerImpl instance;
	private Map<String,CaptureSession> sessionList;
	private Logger logger;
	
	private CaptureManagerImpl() {
		sessionList = new HashMap<String,CaptureSession>();
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
		addSession(session);
		
		return session;
	}

	public void destroyCaptureSession(CaptureSession session) {
	    // TODO implement destroyCaptureSession(CaptureSession session)
	}
	
	public void destroyCaptureSession(String sessionId) {
	    // TODO implement destroyCaptureSession(String sessionId)
	}

	private void addSession(CaptureSession session) {
		synchronized (sessionList) {
			sessionList.put(session.getId(), session);
		}
	}
	
//	private void removeSession(CaptureSession session) {
//		synchronized (sessionList) {
//			sessionList.remove(session.getId());
//		}
//	}

}
