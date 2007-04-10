package br.com.voicetechnology.sipana.capturer;

import org.apache.log4j.Logger;

public class Worker extends Thread {
    private static Logger logger = Logger.getLogger(Worker.class);

    private Dispatcher dispatcher;

    private EventHandler handler;

    private boolean canRun;

    public Worker(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        handler = dispatcher.getEventHandler();
        canRun = true;
    }

    public void run() {
        this.setName("Worker");
        logger.debug("Start running");

        while (canRun) {
            Event message = dispatcher.getMessage();

            if (message != null) {
                try {
                    handler.processEvent(message);
                } catch (Exception e) {
                    logger.error("Error processing event", e);
                }
            }

            if (canRun) {
                if (dispatcher.getPendingMessageCounter() == 0) {
                    try {
                        synchronized (dispatcher) {
                            logger.debug("No pending messages. Sleeping");
                            dispatcher.wait();
                        }
                        logger.debug("Wakeup!");
                    } catch (Exception e) {
                        logger.error("Error when trying to wait dispatcher", e);
                    }
                }
            }
        }

        logger.debug("Finished.");
    }

    public void stopRun() {
        logger.debug("Worker scheduled to stop");
        synchronized (this) {
            canRun = false;
        }
    }

}
