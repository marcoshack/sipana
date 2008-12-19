package org.sipana.server.test.sip;

import java.util.ArrayList;
import java.util.List;
import javax.sip.message.Request;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPSession;
import org.sipana.server.sip.SIPPerformanceMetrics;
import org.sipana.server.sip.SIPPerformanceMetricsBean;

public class SIPPerformanceMetricsTest {
    private SIPPerformanceMetrics metrics;

    @Before
    public void setUp() {
        metrics = new SIPPerformanceMetricsBean();
    }
    
    @Test
    public void avgHopsPerRequest() {
        List<SIPSession> sessionList = new ArrayList<SIPSession>();
        int nSessions = 10;
        int nHosts    = 5;
        
        // 10 sessions
        for (int i = 1; i <= nSessions; i++) {
            SIPSession session = new SIPSession();
            session.setMethod(Request.INVITE);
            
            // 5 hosts = 4 hops ser session
            for (int j = 1; j <= nHosts; j++) {
                SIPRequest request = new SIPRequest();
                request.setMethod(Request.INVITE);
                request.setMaxForwards(70 - j);
                session.addRequest(request);
            }
            
            sessionList.add(session);
        }
        
        // (nHosts - 1) because the initial host doesn't count as a hop
        long expected = ((nHosts -1) * nSessions) / nSessions;
        
        long result = metrics.getAvgHopsPerRequest(sessionList);
        Assert.assertTrue(result == expected);
    }

}
