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
package org.sipana.sip.scenario;

import org.sipana.sip.SIPScenario;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;

public class SIPScenarioTest {

    private List<SIPMessage> messages;

    /**
     * Create a sample message list
     *
     * @author Marcos Hack <marcoshack@gmail.com>
     */
    @Before
    public void createMessageList() {
        messages = new ArrayList<SIPMessage>();

        long startTime = GregorianCalendar.getInstance().getTimeInMillis();

        List<String> callIdList = new ArrayList<String>();
        callIdList.add("AAA");
        callIdList.add("BBB");

        for (String callId : callIdList) {
            SIPRequest reqInvite = new SIPRequest();
            reqInvite.setMethod("INVITE");
            reqInvite.setSrcAddress("127.0.0.1");
            reqInvite.setSrcPort(5060);
            reqInvite.setDstAddress("127.0.0.1");
            reqInvite.setDstPort(5061);
            reqInvite.setTime(startTime += 10);
            reqInvite.setCallID(callId);
            messages.add(reqInvite);

            SIPResponse res180 = new SIPResponse();
            res180.setReasonPhrase("Ringing");
            res180.setStatusCode(180);
            res180.setSrcAddress("127.0.0.1");
            res180.setSrcPort(5061);
            res180.setDstAddress("127.0.0.1");
            res180.setDstPort(5060);
            res180.setTime(startTime += 10);
            res180.setCallID(callId);
            messages.add(res180);

            SIPResponse res200 = new SIPResponse();
            res200.setReasonPhrase("OK");
            res200.setStatusCode(200);
            res200.setSrcAddress("127.0.0.1");
            res200.setSrcPort(5061);
            res200.setDstAddress("127.0.0.1");
            res200.setDstPort(5060);
            res200.setTime(startTime += 10);
            res200.setCallID(callId);
            messages.add(res200);

            SIPRequest reqBye = new SIPRequest();
            reqBye.setMethod("BYE");
            reqBye.setSrcAddress("127.0.0.1");
            reqBye.setSrcPort(5060);
            reqBye.setDstAddress("127.0.0.1");
            reqBye.setDstPort(5061);
            reqBye.setTime(startTime += 100);
            reqBye.setCallID(callId);
            messages.add(reqBye);

            SIPResponse res200Bye = new SIPResponse();
            res200Bye.setReasonPhrase("OK");
            res200Bye.setStatusCode(200);
            res200Bye.setSrcAddress("127.0.0.1");
            res200Bye.setSrcPort(5061);
            res200Bye.setDstAddress("127.0.0.1");
            res200Bye.setDstPort(5060);
            res200Bye.setTime(startTime += 10);
            res200Bye.setCallID(callId);
            messages.add(res200Bye);
        }
    }

    @Test
    public void createSIPScenario() throws Exception {
        FileOutputStream osFile = new FileOutputStream("sipscenario-test-createSIPScenario.jpg");
        SIPScenario scenario = new SIPScenario(messages, "jpg");
        scenario.create(osFile);
        osFile.flush();
        osFile.close();

        File file = new File("sipscenario-test-createSIPScenario.jpg");
        assertTrue(file.exists());
    }

    @Test
    public void createEmptySIPScenario() throws Exception {
        List<SIPMessage> messages = new ArrayList<SIPMessage>();
        SIPScenario scenario = new SIPScenario(messages, "png");
        FileOutputStream osFile = new FileOutputStream("sipscenario-test-createEmptySIPScenario.png");
        scenario.create(osFile);
        osFile.flush();
        osFile.close();

        File file = new File("sipscenario-test-createEmptySIPScenario.png");
        assertTrue(file.exists());
    }
}
