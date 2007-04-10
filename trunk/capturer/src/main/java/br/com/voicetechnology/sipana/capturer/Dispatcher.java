package br.com.voicetechnology.sipana.capturer;

import java.util.LinkedList;
import java.util.Properties;
import org.apache.log4j.Logger;
import br.com.voicetechnology.sipana.SipanaCapturer;
import br.com.voicetechnology.sipana.capturer.EventHandler;

public class Dispatcher implements EventHandler {
    private Logger logger = Logger.getLogger(Dispatcher.class);

    private static Properties properties = SipanaCapturer.getProperties();

    private int message_buffer;

    private LinkedList<Event> eventList;

    private LinkedList<Worker> workerList;

    private EventHandler handler;

    public Dispatcher(EventHandler handler) {
        this.handler = handler;
        eventList = new LinkedList<Event>();
        workerList = new LinkedList<Worker>();

        // Configure message buffer. If not specified use 1 (no buffer)
        String buffer = properties.getProperty("sipana.capturer.buffer");
        if (buffer != null) {
            message_buffer = Integer.parseInt(buffer);
        } else {
            message_buffer = 1;
        }
        logger.debug("Message buffer length: " + message_buffer);
    }

    public void start() throws Exception {
        addWorker();
    }

    public void addWorker() {
        logger.debug("Adding Worker thread");
        Worker worker = new Worker(this);

        synchronized (workerList) {
            workerList.add(worker);
        }

        worker.start();
    }

    public void processEvent(Event event) {
        logger.debug("Adding message");

        synchronized (eventList) {
            eventList.addLast(event);
        }

        if (getPendingMessageCounter() >= message_buffer) {
            synchronized (this) {
                this.notify();
            }
        }
    }

    public Event getMessage() {
        synchronized (eventList) {
            if (eventList.size() == 0) {
                return null;
            } else {
                return eventList.removeFirst();
            }
        }
    }

    public int getPendingMessageCounter() {
        return eventList.size();
    }

    public EventHandler getEventHandler() {
        return handler;
    }
}
