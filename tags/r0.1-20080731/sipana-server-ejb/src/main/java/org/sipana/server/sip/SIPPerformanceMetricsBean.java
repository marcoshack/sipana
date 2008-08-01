package org.sipana.server.sip;

import java.util.List;
import javax.ejb.Stateless;
import javax.sip.message.Request;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPSessionStatus;
import org.sipana.protocol.sip.impl.SIPSessionImpl;

@Stateless
public class SIPPerformanceMetricsBean implements SIPPerformanceMetrics
{
    public long getAvgHopsPerRequest(List<SIPSessionImpl> sessionList) {
        long result = 0;

        if (sessionList.size() == 0) {
            return 0;
        }

        for (SIPSessionImpl session : sessionList) {
            String initialMethod = session.getMethod();
            List<SIPRequest> requestList = session.getRequests();
            SIPRequest first = requestList.get(0);
            
            // Get the last request within the session with the same method of 
            // initial request to discard requests captured on intermediate 
            // hosts (Average Hops is an end-to-end metric)
            SIPRequest last = null;
            for (SIPRequest request : requestList) {
                // TODO Use CSeq to get only the first request 
                if (request.getMethod().equals(initialMethod)) {
                    last = request;
                }
            }

            result += (first.getMaxForwards() - last.getMaxForwards());
        }

        int listSize = sessionList.size();
        return listSize > 0 ? result / listSize : 0;
    }

    public long getAvgRegistrationRequestDelay(List<SIPSessionImpl> sessionList) {
        long result = 0L;
        int nRegister = 0;

        for (SIPSessionImpl session : sessionList) {
            if (session.getMethod().equals(Request.REGISTER)) {
                result =+ getRegistrationRequestDelay(session);
                nRegister++;
            }
        }
        
        return nRegister > 0 ? result / nRegister : 0;
    }

    public long getAvgSessionDisconnectDelay(List<SIPSessionImpl> sessionList) {
        long result = 0;

        for (SIPSessionImpl session : sessionList) {
            result = +getSessionDisconnectDelay(session);
        }

        int listSize = sessionList.size();
        return listSize > 0 ? result / listSize : 0;
    }

    public long getAvgSessionDurationTime(List<SIPSessionImpl> sessionList) {
        long result = 0;

        for (SIPSessionImpl session : sessionList) {
            result = +getSessionDurationTime(session);
        }

        int listSize = sessionList.size();
        return listSize > 0 ? result / listSize : 0;
    }

    public long getAvgSessionRequestDelay(List<SIPSessionImpl> sessionList) {
        long result = 0;

        for (SIPSessionImpl session : sessionList) {
            result =+ getSessionRequestDelay(session);
        }

        int listSize = sessionList.size();
        return listSize > 0 ? result / listSize : 0;
    }

    public double getIneffectiveSessionAttempts(List<SIPSessionImpl> sessionList) {
        long nFail = 0;
        
        for (SIPSessionImpl session : sessionList) {
            if (session.getState() == SIPSessionStatus.FAILED) {
                nFail++;
            }
        }

        int listSize = sessionList.size();
        return (double) (listSize > 0 ? nFail / listSize : 0);
    }

    public long getRegistrationRequestDelay(SIPSessionImpl session) {
        return (session.getEndTime() - session.getStartTime());
    }

    public double getSessionCompletionRate(List<SIPSessionImpl> sessionList) {
        int nComplete = 0;
        
        for (SIPSessionImpl session : sessionList) {
            if (session.getState() == SIPSessionStatus.COMPLETED) {
                nComplete++;
            }
        }

        int listSize = sessionList.size();
        return (double) (listSize > 0 ? nComplete / listSize : 0);
    }

    public long getSessionDefects(List<SIPSessionImpl> sessionList) {
        // TODO getSessionDefects()
        return 0;
    }

    public long getSessionDisconnectDelay(SIPSessionImpl session) {
        return (session.getEndTime() - session.getDisconnectionStart());
    }

    public long getSessionDisconnectFailures(List<SIPSessionImpl> sessionList) {
        // TODO getSessionDisconnectFailures()
        return 0;
    }

    public long getSessionDurationTime(SIPSessionImpl session) {
        return (session.getDisconnectionStart() - session.getEstablishedTime());
    }

    public long getSessionEstablishmentEfficiencyRate(
            List<SIPSessionImpl> sessionList) {
        // TODO getSessionEstablishmentEfficiencyRate()
        return 0;
    }

    public long getSessionEstablishmentRate(List<SIPSessionImpl> sessionList) {
        // TODO getSessionEstablishmentRate()
        return 0;
    }

    public long getSessionRequestDelay(SIPSessionImpl session) {
        return (session.getSetupTime() - session.getStartTime());
    }

    public double getSessionSuccessRate(List<SIPSessionImpl> sessionList) {
        // TODO getSessionSuccessRate()
        return 0;
    }

}
