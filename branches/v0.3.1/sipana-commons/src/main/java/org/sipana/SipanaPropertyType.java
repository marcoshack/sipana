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
package org.sipana;

public enum SipanaPropertyType {

    SIPSESSION_STATUS_INITIATED("sipana.sip.status.initiated", "INITIATED"),
    
    SIPSESSION_STATUS_PROVISIONED("sipana.sip.status.initiated", "PROVISIONED"),
    
    SIPSESSION_STATUS_ESTABLISHED("sipana.sip.status.established", "ESTABLISHED"),
    
    SIPSESSION_STATUS_COMPLETED("sipana.sip.status.completed", "COMPLETED"),
    
    SIPSESSION_STATUS_FAILED("sipana.sip.status.failed", "FAILED"),
    
    SIPSESSION_STATUS_TIMEOUT("sipana.sip.status.timeout", "TIMEOUT"),
    
    SIPSESSION_STATUS_DISCONNECTING("sipana.sip.status.timeout", "DISCONNECTING"),
    
    SIPSESSION_STATUS_CANCELED("sipana.sip.status.timeout", "CANCELED"),
    
    SIPSESSION_STATUS_UNKNOWN("sipana.sip.status.unknown", "UNKNOWN"),
    
    SIPSCENARIO_ARROW_WIDTH("sipana.sip.scenario.arrowsize", "8"),
    
    SIPSCENARIO_ARROW_HEIGHT("sipana.sip.scenario.arrowsize", "3"),
    
    SIPSCENARIO_COLORS("sipana.sip.scenario.colors", "64ff64,79b8ff,fbff93,ff9a56,b0ff63,fc8bf0,ff8d8d"),
    
    SIPANA_DATE_FORMAT("sipana.date.format", "");
    
    private String key;
    private String defaultValue;

    private SipanaPropertyType(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return this.key;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String toString() {
        return this.key;
    }
}
