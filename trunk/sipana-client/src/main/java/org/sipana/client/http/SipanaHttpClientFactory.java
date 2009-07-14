package org.sipana.client.http;

import org.jboss.resteasy.client.ProxyFactory;
import org.sipana.client.SipanaClient;

/**
 *
 * @author mhack
 */
public class SipanaHttpClientFactory {

    public static SipanaClient createSipanaClient() {
        // TODO [mhack] Load from configuration file
        String url = "http://localhost:8080/sipana-ws/";
        SipanaClient client = ProxyFactory.create(SipanaHttpClient.class, url);
        return client;
    }
}
