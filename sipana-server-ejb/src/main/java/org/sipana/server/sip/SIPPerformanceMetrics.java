package org.sipana.server.sip;

import java.util.List;
import javax.ejb.Local;
import org.sipana.protocol.sip.SIPSession;;

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
    public long getRegistrationRequestDelay(SIPSession session);

    /**
     * 3.1. Average Registration Request Delay (ARRD)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgRegistrationRequestDelay(List<SIPSession> sessionList);

    /**
     * 3.2. Session Request Delay (SRD)
     * 
     * @param session
     * @return
     */
    public long getSessionRequestDelay(SIPSession session);

    /**
     * 3.2. Average Session Request Delay (SRD)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgSessionRequestDelay(List<SIPSession> sessionList);

    /**
     * 3.3. Session Disconnect Delay (SDD)
     * 
     * @param session
     * @return
     */
    public long getSessionDisconnectDelay(SIPSession session);

    /**
     * 3.3. Average Session Disconnect Delay (SDD)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgSessionDisconnectDelay(List<SIPSession> sessionList);

    /**
     * 3.4. Session Duration Time (SDT)
     * 
     * @param session
     * @return
     */
    public long getSessionDurationTime(SIPSession session);

    /**
     * 3.4. Average Session Duration Time (SDT)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgSessionDurationTime(List<SIPSession> sessionList);

    /**
     * 3.5. Average Hops per Request (AHR)
     * 
     * @param sessionList
     * @return
     */
    public long getAvgHopsPerRequest(List<SIPSession> sessionList);

    /**
     * 3.6. Session Establishment Rate (SER)
     * 
     * @param sessionList
     * @return
     */
    public long getSessionEstablishmentRate(List<SIPSession> sessionList);

    /**
     * 3.7. Session Establishment Efficiency Rate (SEER)
     * 
     * @param sessionList
     * @return
     */
    public long getSessionEstablishmentEfficiencyRate(
            List<SIPSession> sessionList);

    /**
     * 3.8. Session Defects (SD)
     * 
     * @param sessionList
     * @return
     */
    public long getSessionDefects(List<SIPSession> sessionList);

    /**
     * 3.9. Ineffective Session Attempts (ISA)
     * 
     * @param sessionList
     * @return
     */
    public double getIneffectiveSessionAttempts(List<SIPSession> sessionList);

    /**
     * 3.10. Session Disconnect Failures (SDF)
     * 
     * @param sessionList
     * @return
     */
    public long getSessionDisconnectFailures(List<SIPSession> sessionList);

    /**
     * 3.11. Session Completion Rate (SCR)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionCompletionRate(List<SIPSession> sessionList);

    /**
     * 3.12. Session Success Rate (SSR)
     * 
     * @param sessionList
     * @return
     */
    public double getSessionSuccessRate(List<SIPSession> sessionList);
}
