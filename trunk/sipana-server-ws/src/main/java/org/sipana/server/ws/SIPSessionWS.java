package org.sipana.server.ws;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.sipana.protocol.sip.SIPSession;

/**
 * SIP Session web services.
 * 
 * @author mhack
 */

@Path("/sipsessions")
public interface SIPSessionWS {

    @GET
    @Path("/{sessionId}")
    @Produces("application/*+json")
    public SIPSession getSIPSession(@PathParam("sessionId") long sessionId);

    @GET
    @Path("/list")
    @Produces("application/*+json")
    public List<SIPSession> getSIPSessionList();
}
