package br.com.voicetechnology.sipana.sip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import br.com.voicetechnology.sipana.SipanaCapturer;

public class SipTimeoutHandler extends TimerTask {
    private static final int DEFAULT_SIP_TIMEOUT = 30;

    private static final int MILLI = 1000;

    private int SIP_TIMEOUT;

    private Logger logger;

    private SipMessageHandler handler;

    public SipTimeoutHandler(SipMessageHandler handler) {
        this.handler = handler;
        logger = Logger.getLogger(SipTimeoutHandler.class);
        String timeout = SipanaCapturer.getProperties().getProperty(
                "sipana.sip.timeout");

        if (timeout != null) {
            SIP_TIMEOUT = Integer.parseInt(timeout) * MILLI;
        } else {
            logger.warn("SIP timeout not configured. Using default "
                    + DEFAULT_SIP_TIMEOUT + " seconds");
            SIP_TIMEOUT = DEFAULT_SIP_TIMEOUT * MILLI;
        }
    }

    @Override
    public void run() {
        try {
            logger.debug("Running SIP timeout task");
            ArrayList<String> transaction_list = handler.getTransactionList();
            
            // I know, I know, some timeouts can run away from this execution,
            // but I think that its better than call getTimeInMillis() for each
            // iteration in the for loop.
            long time_now = Calendar.getInstance().getTimeInMillis();

            for (String call_id : transaction_list) {
                SipTransaction transaction = handler.getTransaction(call_id);
                long t_start_time = transaction.getStartDate().getTime();
                long delta = time_now - t_start_time;

                if (delta > SIP_TIMEOUT) {
                    logger.warn("Transaction " + transaction.getMethod()
                            + " timed out [" + call_id + "]");
                    handler.timeoutTransaction(call_id, time_now);
                }
            }

            logger.debug("SIP timeout task finished.");
        } catch (Exception e) {
            logger.error("Error running timeout task", e);
        }
    }

    public int getTimeoutTime() {
        return SIP_TIMEOUT;
    }

}
