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

public enum SIPSessionState {
    INITIATED,
    ESTABLISHED,
    COMPLETED,
    FAILED,
    TIMEOUT,
    PROVISIONED,
    DISCONNECTING,
    CANCELED;

    public static String getStateString(SIPSessionState state) {
        String strStatus;

        switch (state) {
        case INITIATED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_INITIATED);
            break;
        case PROVISIONED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_PROVISIONED);
            break;
        case ESTABLISHED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_ESTABLISHED);
            break;
        case COMPLETED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_COMPLETED);
            break;
        case FAILED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_FAILED);
            break;
        case TIMEOUT:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_TIMEOUT);
            break;
        case DISCONNECTING:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_DISCONNECTING);
            break;
        case CANCELED:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_CANCELED);
            break;
        default:
            strStatus = SipanaProperties.getProperty(SipanaPropertyType.SIPSESSION_STATUS_UNKNOWN);
        }

        return strStatus;
    }
}
