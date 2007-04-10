package br.com.voicetechnology.sipana.http;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;

import br.com.voicetechnology.sipana.SipanaCapturer;

public class HttpServer {
    private static final int DEFAULT_HTTP_PORT = 8080;

    private Server server;

    private Connector connector;

    private Handler handler;

    private Logger logger;

    private int http_port;

    public HttpServer(AbstractHandler handler) {
        this.handler = handler;
        logger = Logger.getLogger(HttpServer.class);

        String str_port = SipanaCapturer.getProperties().getProperty(
                "sipana.http.port");
        if (str_port != null) {
            http_port = Integer.parseInt(str_port);
        } else {
            logger.warn("HTTP server port not set. Using default "
                    + DEFAULT_HTTP_PORT);
            http_port = DEFAULT_HTTP_PORT;
        }

        logger.debug("HTTP server configured on port " + http_port);
    }

    public void start() throws Exception {
        logger.info("Starting HTTP Server");

        server = new Server();
        connector = new SocketConnector();
        connector.setPort(http_port);
        server.setConnectors(new Connector[] { connector });
        server.setHandler(handler);
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
