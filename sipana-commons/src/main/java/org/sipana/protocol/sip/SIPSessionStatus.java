package org.sipana.protocol.sip;

import org.sipana.SipanaProperties;

public class SIPSessionStatus {
    public static final int INITIATED   = 1;
    public static final int ESTABLISHED = 2;
    public static final int COMPLETED   = 3;
    public static final int FAILED      = 4;
    public static final int TIMEOUT     = 5;
    
    public static String getStateString(int state) {
        String strStatus = System.getProperty(SipanaProperties.SIPSESSION_STATUS_UNKNOWN);
        
        switch(state) {
        case SIPSessionStatus.INITIATED:
            strStatus = System.getProperty(SipanaProperties.SIPSESSION_STATUS_INITIATED);
            break;
        case SIPSessionStatus.ESTABLISHED:
            strStatus = System.getProperty(SipanaProperties.SIPSESSION_STATUS_ESTABLISHED);
            break;
        case SIPSessionStatus.COMPLETED:
            strStatus = System.getProperty(SipanaProperties.SIPSESSION_STATUS_COMPLETED);
            break;
        case SIPSessionStatus.FAILED:
            strStatus = System.getProperty(SipanaProperties.SIPSESSION_STATUS_FAILED);
            break;
        case SIPSessionStatus.TIMEOUT:
            strStatus = System.getProperty(SipanaProperties.SIPSESSION_STATUS_TIMEOUT);
            break;
        }
        
        return strStatus;
    }
}
