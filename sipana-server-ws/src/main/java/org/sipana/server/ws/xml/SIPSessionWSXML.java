/**
 * This file is part of Sipana project <http://sipana.org/>
 *
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sipana.server.ws.xml;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.sipana.protocol.sip.SIPSession;
import org.sipana.protocol.sip.SIPSessionList;

/**
 * 
 * @author mhack
 */

@Path("/sipsessions")
public interface SIPSessionWSXML {

    @GET
    @Path("/{sessionId}")
    @Produces("application/xml")
    public SIPSession getSIPSession(@PathParam("sessionId") long sessionId);

    @GET
    @Path("/")
    @Produces("application/xml")
    @Wrapped
    public SIPSessionList getSIPSessionList(
            @QueryParam("startTime") Long startTime,
            @QueryParam("endTime") Long endTime,
            @QueryParam("method") String method,
            @QueryParam("fromUser") String fromUser,
            @QueryParam("toUser") String toUser,
            @QueryParam("callId") String callId,
            @QueryParam("ipAddrList") String ipAddrList
            );
}
