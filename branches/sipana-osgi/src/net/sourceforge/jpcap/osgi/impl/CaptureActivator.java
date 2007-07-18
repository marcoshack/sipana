
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

import java.util.Hashtable;

import net.sourceforge.jpcap.osgi.CaptureServiceProvider;

import org.osgi.framework.*;

public class CaptureActivator implements BundleActivator, ServiceListener {
    private static BundleContext context = null;

    public void start(BundleContext bc) throws Exception {
        CaptureActivator.context = bc;

        // Register the service
        CaptureServiceProvider service = new CaptureServiceProviderImpl();
        context.registerService(CaptureServiceProvider.class.getName(), 
                service, new Hashtable());
    }

    public void stop(BundleContext context) throws Exception {
        CaptureActivator.context = null;
    }

    public void serviceChanged(ServiceEvent event) {
        
    }

}
