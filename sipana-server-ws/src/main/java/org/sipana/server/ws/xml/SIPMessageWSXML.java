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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPMessageList;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
@Path("/sipmessages")
public interface SIPMessageWSXML {

    @GET
    @Path("/{messageId}")
    @Produces("application/xml")
    public SIPMessage getMessage(@PathParam("messageId") long messageId);

    @GET
    @Path("/session/{sessionList}")
    @Produces("application/xml")
    public SIPMessageList getMessageList(@PathParam("sessionList") String sessionList);


    @PUT
    @Consumes("application/xml")
    public void saveMessageList(SIPMessageList messageList);
}
