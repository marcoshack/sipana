package org.sipana.server.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * SIP Session web services.
 * 
 * @author mhack
 */

@Path("/sessions")
public interface SIPSession {
    @GET
    @Path("list")
    @Produces("text/plain")
    public String getSessionList();
}
