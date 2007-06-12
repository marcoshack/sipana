package net.sourceforge.sipana.capturer.impl;

import java.util.Hashtable;

import org.osgi.framework.*;

import net.sourceforge.sipana.capturer.CaptureServiceProvider;


public class CapturerActivator implements BundleActivator, ServiceListener {
    private static BundleContext context = null;

    public void start(BundleContext bc) throws Exception {
        CapturerActivator.context = bc;
        CaptureServiceProvider service = new CaptureServiceProviderImpl();
        
        context.registerService(CaptureServiceProvider.class.getName(), 
                service, new Hashtable());
    }

    public void stop(BundleContext context) throws Exception {
        CapturerActivator.context = null;
    }

    public void serviceChanged(ServiceEvent event) {
        
    }

}
