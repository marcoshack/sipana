package net.sourceforge.sipana.sip.impl;

import java.util.Hashtable;

import net.sourceforge.sipana.sip.SipanaSipProvider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class SipanaSipActivator implements BundleActivator, ServiceListener {
    private static BundleContext context = null;

    public void start(BundleContext bc) throws Exception {
        SipanaSipActivator.context = bc;

        // Register the service
        SipanaSipProviderImpl service = new SipanaSipProviderImpl();
        context.registerService(SipanaSipProvider.class.getName(), 
                service, new Hashtable());
    }

    public void stop(BundleContext bc) throws Exception {
        SipanaSipActivator.context = null;
    }

    public void serviceChanged(ServiceEvent arg0) {

    }

}
