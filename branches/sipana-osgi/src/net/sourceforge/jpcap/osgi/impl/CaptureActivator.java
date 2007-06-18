package net.sourceforge.jpcap.osgi.impl;

import java.util.Hashtable;

import net.sourceforge.jpcap.osgi.CaptureServiceProvider;

import org.osgi.framework.*;

public class CaptureActivator implements BundleActivator, ServiceListener {
    private static BundleContext context = null;

    public void start(BundleContext bc) throws Exception {
        CaptureActivator.context = bc;
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
