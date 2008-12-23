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
