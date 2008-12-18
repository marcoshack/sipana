package org.sipana.server.service;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class ServiceLocator {
	private static final Object classLock = ServiceLocator.class;
	private static ServiceLocator instance = null;
	private Logger logger = Logger.getLogger(ServiceLocator.class);
	
	private ServiceLocator() {
		logger.debug("Creating ServiceLocator instance");
	}

	public static ServiceLocator getInstance() {
		synchronized (classLock) {
			if (instance == null) {
				instance = new ServiceLocator();
			}
			
			return instance;
		}
	}

	/**
	 * Get a service reference based on service name.
	 * @see {@link Service} for available service names.
	 * 
	 * @param serviceName
	 * @return
	 * @throws Exception
	 */
	public Object getService(String serviceName) throws ServiceLocatorException {
        StringBuilder sbName = new StringBuilder("sipana/").append(serviceName);
        sbName.append("/local");
		logger.debug("Looking for \"" + sbName + "\"");
		
		try {
			InitialContext context = new InitialContext();
			return context.lookup(sbName.toString());
			
		} catch (Exception e) {
			throw new ServiceLocatorException("Fail getting service " + serviceName, e);
		}
	}
}
