package org.sipana.server.ws.impl;

import org.apache.log4j.Logger;
import org.sipana.server.ws.SIPScenarioWS;

/**
 *
 * @author mhack
 */
public class SIPScenarioWSBean implements SIPScenarioWS {

    private Logger logger = Logger.getLogger(SIPScenarioWSBean.class);

    public String getSIPScenario(String sessionList) {
        // TODO [mhack] fake method to test resteasy

        String[] idList = null;
        StringBuilder sbList = new StringBuilder();

        if (sessionList != null) {
            idList = sessionList.split(",");
            sbList = new StringBuilder("{ ");

            for (String id : idList) {
                sbList.append(id).append(" ");
            }

            sbList.append(" }");

        } else {
            sbList.append("sessionId undefined");
        }

        return sbList.toString();
    }
}