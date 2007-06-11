package net.sourceforge.sipana.capturer.impl;

import org.osgi.framework.*;

public class CapturerActivator implements BundleActivator {
    private static BundleContext context = null;

    public void start(BundleContext context) throws Exception {
        CapturerActivator.context = context;
    }

    public void stop(BundleContext context) throws Exception {
        CapturerActivator.context = null;
    }

}
