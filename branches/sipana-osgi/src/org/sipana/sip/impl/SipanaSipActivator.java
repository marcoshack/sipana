
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

package org.sipana.sip.impl;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.sipana.sip.SipanaSipProvider;

public class SipanaSipActivator implements BundleActivator {
    private static BundleContext bc = null;
    private SipanaSipProviderImpl service;
    private Logger logger;

    public void start(BundleContext bc) throws Exception {
        SipanaSipActivator.bc = bc;
        logger = Logger.getLogger(SipanaSipActivator.class);
        
        logger.info("Registering SipanaSipProvider");
        service = new SipanaSipProviderImpl();
        bc.registerService(SipanaSipProvider.class.getName(), 
                service, new Hashtable());
    }

    public void stop(BundleContext bc) throws Exception {
        SipanaSipActivator.bc = null;
    }
}
