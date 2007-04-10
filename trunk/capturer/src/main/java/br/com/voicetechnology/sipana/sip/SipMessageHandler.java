package br.com.voicetechnology.sipana.sip;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import org.apache.log4j.Logger;
import br.com.voicetechnology.sipana.SipanaServerClient;
import br.com.voicetechnology.sipana.capturer.Event;
import br.com.voicetechnology.sipana.capturer.EventHandler;
import br.com.voicetechnology.sipana.statistic.StatisticSip;

public class SipMessageHandler implements EventHandler {
    private Logger logger;

    private HashMap<String, SipTransaction> transactions;

    private StatisticSip statistics;

    private SipTimeoutHandler timeout_handler;

    private Timer timeout_timer;
    
    private SipanaServerClient client;
    
    public SipMessageHandler(SipanaServerClient client) throws Exception {
        this.client = client;
        logger = Logger.getLogger(SipMessageHandler.class);
        statistics = StatisticSip.getInstance();
        transactions = new HashMap<String, SipTransaction>();

        startTimeoutHandler();
    }

    public void processEvent(Event event) {
        logger.debug("Processing new Event");

        try {
            SipMessage message = SipMessageFactory
                    .createSipMessage((byte[]) event.getData());
            message.setDate(event.getDate());
            message.setDstIpAddr(event.getDstIpAddr());
            message.setSrcIpAddr(event.getSrcIpAddr());

            if (message instanceof SipRequest) {
                processRequest((SipRequest) message);
            } else if (message instanceof SipResponse) {
                processResponse((SipResponse) message);
            } else {
                logger.error("Unknow message type:\n"
                        + event.getData().toString());
            }

        } catch (ParseException e) {
            logger.error("Error parsing SIP message. Ignoring message.", e);
        } catch (Exception e) {
            logger.error("Error processing event.", e);
        }
    }

    private void processRequest(SipRequest request) {
        String call_id = request.getCallId();
        String method = request.getMethod();
        logger.debug("Processing request " + method + " [" + call_id + "]");

        SipTransaction transaction = getTransaction(call_id);

        // Ignored requests
        if (method.equals(SipRequest.ACK)) {
            logger.debug("Request " + method + " ignored.");
            return;
        }

        // Cancel transaction if exist. Ignore CANCEL of CANCEL (retransmition)
        if (transaction != null && method.equals(SipRequest.CANCEL)
                && !(transaction.getMethod().equals(SipRequest.CANCEL))) {
            logger.debug("Transaction " + method + " canceled [" + call_id
                    + "]");
            removeTransaction(call_id);
            transaction = null;
        }

        // Ignore retransmitions
        if (transaction == null) {
            transaction = new SipTransaction(request);
            addTransaction(transaction);
            logger.debug("Transaction created [" + call_id + "]");
        }
    }

    private void processResponse(SipResponse response) {
        String call_id = response.getCallId();
        String reason_phrase = response.getReasonPhrase();
        int status_code = response.getStatusCode();
        logger.debug("Processing response " + status_code + " ("
                + reason_phrase + ") [" + call_id + "]");

        SipTransaction transaction = getTransaction(call_id);

        if (transaction == null) {
            logger.debug("Transaction not found, response ignored [" + call_id
                    + "]");
            return;
        }

        // Provisional 1xx
        if (status_code >= 100 && status_code < 200) {
            transaction.addResponse(response);
            logger.debug("Provisional response added to transaction ["
                    + call_id + "]");

        // Final Responses. Finalize and add transaction statistics
        } else if (status_code >= 200 && status_code < 700) {
            transaction.addResponse(response);
            finishTransaction(transaction);
        }
    }

    protected SipTransaction getTransaction(String call_id) {
        synchronized (transactions) {
            return transactions.get(call_id);
        }
    }

    private SipTransaction removeTransaction(String call_id) {
        synchronized (transactions) {
            statistics.decPendingTransactionCounter();
            return transactions.remove(call_id);
        }
    }

    private void addTransaction(SipTransaction transaction) {
        synchronized (transactions) {
            statistics.incPendingTransactionCounter();
            transactions.put(transaction.getCallId(), transaction);
        }
    }

    public ArrayList<String> getTransactionList() {
        ArrayList<String> transactionIDs = new ArrayList<String>();

        synchronized (transactions) {
            for (String method : transactions.keySet()) {
                transactionIDs.add(method);
            }
        }

        return transactionIDs;
    }
    
    private void finishTransaction(SipTransaction transaction) {
        String call_id = transaction.getCallId();
        transaction.setFinished();
        removeTransaction(call_id);
        logger.debug("Final response, transaction completed in "
                + transaction.getDuration() + " ms [" + call_id + "]");
        
        //statistics.addTransaction(transaction);
        client.addTransaction(transaction);
    }

    public void timeoutTransaction(String call_id, long time) {
        synchronized (transactions) {
            SipTransaction transaction = removeTransaction(call_id);
            statistics.addTimeout(transaction);
            transaction = null;
        }
    }

    private void startTimeoutHandler() {
        timeout_handler = new SipTimeoutHandler(this);
        int timeout = timeout_handler.getTimeoutTime();
        timeout_timer = new Timer("TimeoutHandler");
        timeout_timer.schedule(timeout_handler, timeout, timeout);
        logger.info("SIP timeout handler scheduled to each " + timeout / 1000
                + " seconds");
    }
}
