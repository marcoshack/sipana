/**
 * This file is part of Sipana project <http://sipana.org/>
 *
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sipana.protocol.sip;

import org.sipana.SipanaProperties;
import org.sipana.SipanaPropertyType;

public class SIPSessionState {
    public static final int INITIATED       = 1;
    public static final int ESTABLISHED     = 2;
    public static final int COMPLETED       = 3;
    public static final int FAILED          = 4;
    public static final int TIMEOUT         = 5;
    public static final int PROVISIONED     = 6;
    public static final int DISCONNECTING   = 7;
    public static final int CANCELED        = 8;

    public static String getStateString(int state) {
        String strStatus;

        switch (state) {
        case SIPSessionState.INITIATED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_INITIATED);
            break;
        case SIPSessionState.PROVISIONED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_PROVISIONED);
            break;
        case SIPSessionState.ESTABLISHED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_ESTABLISHED);
            break;
        case SIPSessionState.COMPLETED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_COMPLETED);
            break;
        case SIPSessionState.FAILED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_FAILED);
            break;
        case SIPSessionState.TIMEOUT:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_TIMEOUT);
            break;
        case SIPSessionState.DISCONNECTING:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_DISCONNECTING);
            break;
        case SIPSessionState.CANCELED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_CANCELED);
            break;
        default:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_UNKNOWN);
        }

        return strStatus;
    }
}
