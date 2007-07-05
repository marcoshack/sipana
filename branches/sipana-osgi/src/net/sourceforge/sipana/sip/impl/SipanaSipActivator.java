package net.sourceforge.sipana.sip.impl;

import java.util.Hashtable;

//import net.sourceforge.jpcap.osgi.CaptureServiceProvider;
//import net.sourceforge.jpcap.osgi.CaptureSession;
import net.sourceforge.sipana.sip.SipanaSipProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
//import org.osgi.framework.ServiceEvent;
//import org.osgi.framework.ServiceListener;
//import org.osgi.framework.ServiceReference;

public class SipanaSipActivator implements BundleActivator {
    private static BundleContext bc = null;
//    private CaptureServiceProvider captureProvider;
//    private CaptureSession captureSession;
    private SipanaSipProviderImpl service;
    private Log logger;

    public void start(BundleContext bc) throws Exception {
        SipanaSipActivator.bc = bc;
        logger = LogFactory.getLog(SipanaSipActivator.class);
        
        logger.info("Registering SipanaSipProvider");
        service = new SipanaSipProviderImpl();
        bc.registerService(SipanaSipProvider.class.getName(), 
                service, new Hashtable());
        
//        captureProvider = null;
//        captureSession = null;
//        logger.info("Subscribing as service listener to needed services");
//        String filter = "(objectclass=" + CaptureServiceProvider.class.getName() + ")";
//        bc.addServiceListener(this, filter);
//        ServiceReference references[] = bc.getServiceReferences(null, filter);
//        
//        for (int i = 0; references != null && i < references.length; i++) {
//            serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, references[i]));
//        }
    }

    public void stop(BundleContext bc) throws Exception {
//        if (captureSession != null) {
//            stopSipCapture();
//        }
//        
//        ServiceReference captureRef = SipanaSipActivator.bc.getServiceReference(CaptureServiceProvider.class.getName());
//        SipanaSipActivator.bc.ungetService(captureRef);
//        captureProvider = null;
        SipanaSipActivator.bc = null;
    }

//    public void serviceChanged(ServiceEvent event) {
//        switch (event.getType()) {
//        case ServiceEvent.REGISTERED:
//            captureProvider = (CaptureServiceProvider) SipanaSipActivator.bc.getService(event.getServiceReference());
//            startSipCapture();
//            break;
//        
//        case ServiceEvent.MODIFIED:
//            stopSipCapture();
//            captureProvider = (CaptureServiceProvider) SipanaSipActivator.bc.getService(event.getServiceReference());
//            startSipCapture();
//            break;
//            
//        case ServiceEvent.UNREGISTERING:
//            stopSipCapture();
//            break;
//        }
//    }
//    
//    private void startSipCapture() {
//        try {
//            logger.info("Starting SIP capture");
//            captureSession = captureProvider.createCaptureSession();
//            captureSession.setListener(service);
//            
//            // TODO remove capture session hard coded configuration
//            captureSession.setDevice("lo");
//            captureSession.setFilter("udp or icmp");
//            captureSession.start();
//            
//            logger.info("SIP capture started");
//        } catch (Exception e) {
//            logger.error("Fail stoping SIP capture", e);
//        }
//    }
//    
//    private void stopSipCapture() {
//        try {
//            logger.info("Stoping SIP capture");
//            captureSession.stop();
//            captureProvider.destroyCaptureSession(captureSession);
//            logger.info("SIP capture stoped");
//        } catch (Exception e) {
//            logger.error("Fail stoping SIP capture", e);
//        }
//    }

}
