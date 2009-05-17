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
package org.sipana.server.sip.metric;

import java.util.List;
import javax.ejb.Local;
import org.sipana.protocol.sip.SIPSession;;

/**
 * Based in the Internet-Draft "SIP End-to-End Performance Metrics" version 3.
 *
 * @author mhack
 * @see http://tools.ietf.org/html/draft-ietf-pmol-sip-perf-metrics-03
 */

@Local
public interface SIPPerformanceMetrics
{

    /**
     * Item 4.1. Registration Request Delay (RRD)
     * 
     * @param session
     * @return
     */
    public long getRegistrationRequestDelay(SIPSession session);

    /**
     * Average Registration Request Delay (ARRD)
     * 
     * @param sessionList
     * @return
     */
    public double getAvgRegistrationRequestDelay(List<SIPSession> sessionList);

    /**
     * Item 4.2. Ineffective Registration Attempts (IRA)
     *
     * @param sessionList
     * @return Percentage of the number of failed by the total of Registration
     *         attempts.
     */
    public double getIneffectiveRegistrationAttempts(
            List<SIPSession> sessionList);

    /**
     * Item 4.3. Session Request Delay (SRD)
     * 
     * @param session
     * @return
     */
    public long getSessionRequestDelay(SIPSession session);

    /**
     * Average Session Request Delay (ASRD)
     * 
     * @param sessionList
     * @return
     */
    public double getAvgSessionRequestDelay(List<SIPSession> sessionList);


    /**
     * Item 4.4. Session Disconnect Delay (SDD)
     * 
     * @param session
     * @return
     */
    public long getSessionDisconnectDelay(SIPSession session);

    /**
     * Average Session Disconnect Delay (ASDD)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgSessionDisconnectDelay(List<SIPSession> sessionList);

    /**
     * Item 4.5. Session Duration Time (SDT)
     * 
     * @param session
     * @return
     */
    public long getSessionDurationTime(SIPSession session);

    /**
     * Average Session Duration Time (ASDT)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgSessionDurationTime(List<SIPSession> sessionList);

    /**
     * Item 4.6. Hops per Request (HpR)
     *
     * @param sessionList
     * @return
     */
    public int getHopsPerRequest(SIPSession session);

    /**
     * Average Hops per Request (AHpR)
     * 
     * @param sessionList
     * @return
     */
    public double getAvgHopsPerRequest(List<SIPSession> sessionList);

    /**
     * Item 4.7. Session Establishment Ratio (SER)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionEstablishmentRatio(List<SIPSession> sessionList);

    /**
     * Item 4.8. Session Establishment Effectiveness Ratio (SEER)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionEstablishmentEffectivenessRatio(
            List<SIPSession> sessionList);

    /**
     * Item 4.9. Session Defects Ratio (SDR)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionDefectsRatio(List<SIPSession> sessionList);

    /**
     * Item 4.10. Ineffective Session Attempts (ISA)
     * 
     * @param sessionList
     * @return
     */
    public double getIneffectiveSessionAttempts(List<SIPSession> sessionList);

    /**
     * Item 4.11. Session Disconnect Failures (SDF)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionDisconnectFailures(List<SIPSession> sessionList);

    /**
     * Item 4.12. Session Completion Ratio (SCR)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionCompletionRatio(List<SIPSession> sessionList);

    /**
     * Item 4.13. Session Success Ratio (SSR)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionSuccessRatio(List<SIPSession> sessionList);
}
