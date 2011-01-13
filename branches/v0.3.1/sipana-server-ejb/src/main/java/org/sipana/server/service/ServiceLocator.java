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
