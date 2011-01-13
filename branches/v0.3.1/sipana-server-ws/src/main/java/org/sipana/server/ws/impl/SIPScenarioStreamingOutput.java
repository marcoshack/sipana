package org.sipana.server.ws.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.sip.scenario.SIPScenario;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class SIPScenarioStreamingOutput implements StreamingOutput {
    private SIPScenario sipScenario;

    public SIPScenarioStreamingOutput() {
    }

    public SIPScenarioStreamingOutput(List<SIPMessage> messageList) {
        sipScenario = new SIPScenario(messageList);
    }

    public void write(OutputStream output) throws IOException, WebApplicationException {
        sipScenario.create(output);
    }
}
