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
package org.sipana.server.sip;

import java.util.List;
import javax.ejb.Stateless;
import javax.sip.message.Request;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPSessionState;
import org.sipana.protocol.sip.SIPSession;

@Stateless
public class SIPPerformanceMetricsBean implements SIPPerformanceMetrics
{
    public long getAvgHopsPerRequest(List<SIPSession> sessionList) {
        long result = 0;

        if (sessionList.size() == 0) {
            return 0;
        }

        for (SIPSession session : sessionList) {
            String initialMethod = session.getRequestMethod();
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

    public long getAvgRegistrationRequestDelay(List<SIPSession> sessionList) {
        long result = 0L;
        int nRegister = 0;

        for (SIPSession session : sessionList) {
            if (session.getRequestMethod().equals(Request.REGISTER)) {
                result =+ getRegistrationRequestDelay(session);
                nRegister++;
            }
        }
        
        return nRegister > 0 ? result / nRegister : 0;
    }

    public long getAvgSessionDisconnectDelay(List<SIPSession> sessionList) {
        long result = 0;

        for (SIPSession session : sessionList) {
            result = +getSessionDisconnectDelay(session);
        }

        int listSize = sessionList.size();
        return listSize > 0 ? result / listSize : 0;
    }

    public long getAvgSessionDurationTime(List<SIPSession> sessionList) {
        long result = 0;

        for (SIPSession session : sessionList) {
            result = +getSessionDurationTime(session);
        }

        int listSize = sessionList.size();
        return listSize > 0 ? result / listSize : 0;
    }

    public long getAvgSessionRequestDelay(List<SIPSession> sessionList) {
        long result = 0;

        for (SIPSession session : sessionList) {
            result =+ getSessionRequestDelay(session);
        }

        int listSize = sessionList.size();
        return listSize > 0 ? result / listSize : 0;
    }

    public double getIneffectiveSessionAttempts(List<SIPSession> sessionList) {
        long nFail = 0;
        
        for (SIPSession session : sessionList) {
            if (session.getState() == SIPSessionState.FAILED) {
                nFail++;
            }
        }

        int listSize = sessionList.size();
        return (double) (listSize > 0 ? nFail / listSize : 0);
    }

    public long getRegistrationRequestDelay(SIPSession session) {
        return (session.getEndTime() - session.getStartTime());
    }

    public double getSessionCompletionRate(List<SIPSession> sessionList) {
        int nComplete = 0;
        
        for (SIPSession session : sessionList) {
            if (session.getState() == SIPSessionState.COMPLETED) {
                nComplete++;
            }
        }

        int listSize = sessionList.size();
        return (double) (listSize > 0 ? nComplete / listSize : 0);
    }

    public long getSessionDefects(List<SIPSession> sessionList) {
        // TODO getSessionDefects()
        return 0;
    }

    public long getSessionDisconnectDelay(SIPSession session) {
        return (session.getEndTime() - session.getDisconnectionStart());
    }

    public long getSessionDisconnectFailures(List<SIPSession> sessionList) {
        // TODO getSessionDisconnectFailures()
        return 0;
    }

    public long getSessionDurationTime(SIPSession session) {
        return (session.getDisconnectionStart() - session.getEstablishedTime());
    }

    public long getSessionEstablishmentEfficiencyRate(
            List<SIPSession> sessionList) {
        // TODO getSessionEstablishmentEfficiencyRate()
        return 0;
    }

    public long getSessionEstablishmentRate(List<SIPSession> sessionList) {
        // TODO getSessionEstablishmentRate()
        return 0;
    }

    public long getSessionRequestDelay(SIPSession session) {
        return (session.getSetupTime() - session.getStartTime());
    }

    public double getSessionSuccessRate(List<SIPSession> sessionList) {
        // TODO getSessionSuccessRate()
        return 0;
    }

}
