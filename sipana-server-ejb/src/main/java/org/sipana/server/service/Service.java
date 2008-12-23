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
package org.sipana.server.service;

import org.sipana.server.sip.SIPPerformanceMetricsBean;
import org.sipana.server.sip.SIPSessionManagerBean;


public interface Service {
	public static final String SIP_SESSION_MANAGER = SIPSessionManagerBean.class.getSimpleName();
	public static final String SIP_PERFORMANCE_METRICS = SIPPerformanceMetricsBean.class.getSimpleName();
}
