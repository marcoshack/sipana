package org.sipana.sip;

import net.sourceforge.jpcap.osgi.CaptureServiceProvider;

import org.sipana.sip.impl.SipanaSIPSessionManager;

public interface SipanaSIPProvider {
    public SIPMessageFactory getMessageFactory();
    public SIPHandler getSIPHandler();
    public SipanaSIPSessionManager getSIPSessionManager();
    public void setCaptureServiceProvider(CaptureServiceProvider provider);
    public void addCaptureSession(String iface, String filter);
}
