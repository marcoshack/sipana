package org.sipana.protocol.sip;

import org.sipana.SipanaProperties;
import org.sipana.SipanaPropertyType;

public class SIPSessionStatus {
    public static final int INITIATED       = 1;
    public static final int ESTABLISHED     = 2;
    public static final int COMPLETED       = 3;
    public static final int FAILED          = 4;
    public static final int TIMEOUT         = 5;
    public static final int PROVISIONED     = 6;
    public static final int DISCONNECTING   = 7;
    public static final int CANCELED       = 8;

    public static String getStateString(int state) {
        String strStatus;

        switch (state) {
        case SIPSessionStatus.INITIATED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_INITIATED);
            break;
        case SIPSessionStatus.PROVISIONED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_PROVISIONED);
            break;
        case SIPSessionStatus.ESTABLISHED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_ESTABLISHED);
            break;
        case SIPSessionStatus.COMPLETED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_COMPLETED);
            break;
        case SIPSessionStatus.FAILED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_FAILED);
            break;
        case SIPSessionStatus.TIMEOUT:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_TIMEOUT);
            break;
        case SIPSessionStatus.DISCONNECTING:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_DISCONNECTING);
            break;
        case SIPSessionStatus.CANCELED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_CANCELED);
            break;
        default:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_UNKNOWN);
        }

        return strStatus;
    }
}
