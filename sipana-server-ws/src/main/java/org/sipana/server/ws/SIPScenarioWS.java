package org.sipana.server.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author mhack
 */
@Path("/sipscenario")
public interface SIPScenarioWS {
    @GET
    @Path("/")
    @Produces("text/plain")
    public String getSIPScenario(@QueryParam("sessionId") String sessionList);
}
