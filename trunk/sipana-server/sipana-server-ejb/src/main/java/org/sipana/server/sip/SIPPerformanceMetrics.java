package org.sipana.server.sip;

import java.util.List;
import javax.ejb.Local;
import org.sipana.protocol.sip.impl.SIPSessionImpl;;

/**
 * 
 * @author mhack
 * @see http://tools.ietf.org/html/draft-malas-performance-metrics
 */

@Local
public interface SIPPerformanceMetrics
{

    /**
     * 3.1. Registration Request Delay (RRD)
     * 
     * @param session
     * @return
     */
    public long getRegistrationRequestDelay(SIPSessionImpl session);

    /**
     * 3.1. Average Registration Request Delay (ARRD)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgRegistrationRequestDelay(List<SIPSessionImpl> sessionList);

    /**
     * 3.2. Session Request Delay (SRD)
     * 
     * @param session
     * @return
     */
    public long getSessionRequestDelay(SIPSessionImpl session);

    /**
     * 3.2. Average Session Request Delay (SRD)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgSessionRequestDelay(List<SIPSessionImpl> sessionList);

    /**
     * 3.3. Session Disconnect Delay (SDD)
     * 
     * @param session
     * @return
     */
    public long getSessionDisconnectDelay(SIPSessionImpl session);

    /**
     * 3.3. Average Session Disconnect Delay (SDD)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgSessionDisconnectDelay(List<SIPSessionImpl> sessionList);

    /**
     * 3.4. Session Duration Time (SDT)
     * 
     * @param session
     * @return
     */
    public long getSessionDurationTime(SIPSessionImpl session);

    /**
     * 3.4. Average Session Duration Time (SDT)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgSessionDurationTime(List<SIPSessionImpl> sessionList);

    /**
     * 3.5. Average Hops per Request (AHR)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgHopsPerRequest(List<SIPSessionImpl> sessionList);

    /**
     * 3.6. Session Establishment Rate (SER)
     * 
     * @param sessionList
     * @return
     */
    public long getSessionEstablishmentRate(List<SIPSessionImpl> sessionList);

    /**
     * 3.7. Session Establishment Efficiency Rate (SEER)
     * 
     * @param sessionList
     * @return
     */
    public long getSessionEstablishmentEfficiencyRate(
            List<SIPSessionImpl> sessionList);

    /**
     * 3.8. Session Defects (SD)
     * 
     * @param sessionList
     * @return
     */
    public long getSessionDefects(List<SIPSessionImpl> sessionList);

    /**
     * 3.9. Ineffective Session Attempts (ISA)
     * 
     * @param sessionList
     * @return
     */
    public double getIneffectiveSessionAttempts(List<SIPSessionImpl> sessionList);

    /**
     * 3.10. Session Disconnect Failures (SDF)
     * 
     * @param sessionList
     * @return
     */
    public long getSessionDisconnectFailures(List<SIPSessionImpl> sessionList);

    /**
     * 3.11. Session Completion Rate (SCR)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionCompletionRate(List<SIPSessionImpl> sessionList);

    /**
     * 3.12. Session Success Rate (SSR)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionSuccessRate(List<SIPSessionImpl> sessionList);
}
