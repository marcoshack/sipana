package org.sipana.client.capture.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sipana.client.capture.CaptureException;
import org.sipana.client.capture.CaptureListener;
import org.sipana.client.capture.CaptureManager;
import org.sipana.client.capture.CaptureSession;

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
