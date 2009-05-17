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
import javax.ejb.Stateless;
import javax.sip.message.Request;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPSessionState;
import org.sipana.protocol.sip.SIPSession;

@Stateless
public class SIPPerformanceMetricsBean implements SIPPerformanceMetrics
{
    public int getHopsPerRequest(SIPSession session) {
        String initialMethod = session.getRequestMethod();
        List<SIPMessage> messageList = session.getMessageList();
        SIPRequest first = null;
        int result = 0;

        // Get the last request within the session with the same method of
        // initial request to discard requests captured on intermediate
        // hosts (Average Hops is an end-to-end metric)
        SIPRequest last = null;
        for (SIPMessage m : messageList) {
            // TODO Use CSeq to get only the first request
            if (m instanceof SIPRequest && ((SIPRequest) m).getMethod().equals(initialMethod)) {
                if (first == null) {
                    first = (SIPRequest) m;
                }
                last = (SIPRequest) m;
            }
        }

        if (first != null && last != null) {
            result += (first.getMaxForwards() - last.getMaxForwards());
        }
        
        return result;
    }


    public double getAvgHopsPerRequest(List<SIPSession> sessionList) {
        double result = 0;

        for (SIPSession session : sessionList) {
            result += getHopsPerRequest(session);
        }

        int listSize = sessionList.size();
        return listSize > 0 ? result / listSize : 0L;
    }

    public double getIneffectiveRegistrationAttempts(List<SIPSession> sessionList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getAvgRegistrationRequestDelay(List<SIPSession> sessionList) {
        double result = 0L;
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

    public double getAvgSessionRequestDelay(List<SIPSession> sessionList) {
        double result = 0;

        for (SIPSession session : sessionList) {
            result =+ getSessionRequestDelay(session);
        }

        int listSize = sessionList.size();
        return listSize > 0 ? result / listSize : 0L;
    }

    public double getIneffectiveSessionAttempts(List<SIPSession> sessionList) {
        double nFail = 0;
        
        for (SIPSession session : sessionList) {
            if (session.getState() == SIPSessionState.FAILED) {
                nFail++;
            }
        }

        int listSize = sessionList.size();
        return (double) (listSize > 0 ? nFail / listSize : 0L);
    }

    public long getRegistrationRequestDelay(SIPSession session) {
        return (session.getEndTime() - session.getStartTime());
    }

    public double getSessionCompletionRatio(List<SIPSession> sessionList) {
        int nComplete = 0;
        
        for (SIPSession session : sessionList) {
            if (session.getState() == SIPSessionState.COMPLETED) {
                nComplete++;
            }
        }

        int listSize = sessionList.size();
        return (double) (listSize > 0 ? nComplete / listSize : 0L);
    }

    public double getSessionDefectsRatio(List<SIPSession> sessionList) {
        // TODO getSessionDefects()
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getSessionDisconnectDelay(SIPSession session) {
        return (session.getEndTime() - session.getDisconnectionStart());
    }

    public double getSessionDisconnectFailures(List<SIPSession> sessionList) {
        // TODO getSessionDisconnectFailures()
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getSessionDurationTime(SIPSession session) {
        return (session.getDisconnectionStart() - session.getEstablishedTime());
    }

    public double getSessionEstablishmentEffectivenessRatio(
            List<SIPSession> sessionList) {
        // TODO getSessionEstablishmentEfficiencyRate()
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getSessionEstablishmentRatio(List<SIPSession> sessionList) {
        // TODO getSessionEstablishmentRate()
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getSessionRequestDelay(SIPSession session) {
        return (session.getSetupTime() - session.getStartTime());
    }

    public double getSessionSuccessRatio(List<SIPSession> sessionList) {
        // TODO getSessionSuccessRate()
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
