/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sipana.agent;

import org.apache.log4j.Logger;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SipanaAgentSignalHandler implements SignalHandler, Runnable {
    private static final int SIGTERM = 15;

    private SipanaAgent agent;
    private Logger logger = Logger.getLogger(SipanaAgentSignalHandler.class);

    public SipanaAgentSignalHandler(SipanaAgent agent) {
        this.agent = agent;

        Signal.handle(new Signal("TERM"), this);
    }

    public void handle(Signal signal) {
        logger.debug("Signal " + signal.getName() + "(" + signal.getNumber() 
                + ")" + " received");

        switch(signal.getNumber()) {
            case SIGTERM:
                stopSipanaAgent();
                break;

            default:
                logger.warn("Unknown signal");
        }
    }

    private void stopSipanaAgent() {
        try {
            agent.stop();

        } catch (Exception e) {
            logger.error("Failed to stop Sipana Agent", e);
        }
    }

    public void run() {
        logger.info("Shutdown hook received");
        stopSipanaAgent();
    }
}
