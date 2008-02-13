package org.sipana.sip.scenario;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.sipana.protocol.sip.SIPMessage;
import org.sipana.protocol.sip.SIPRequest;
import org.sipana.protocol.sip.SIPResponse;
import org.sipana.protocol.sip.impl.SIPRequestImpl;
import org.sipana.protocol.sip.impl.SIPResponseImpl;

public class SIPScenarioTest {

	@Test
	public void createSIPFlow() throws Exception {

		FileOutputStream osFile = new FileOutputStream("sipscenario-test.jpg");

		SIPScenario scenario = new SIPScenario(createMessageList());
		scenario.create(osFile);

		osFile.flush();
		osFile.close();

		File file = new File("sipscenario-test.jpg");
		assertTrue(file.exists());
	}

	/**
	 * Create an example message list
	 */
	private List<SIPMessage> createMessageList() {
		List<SIPMessage> messages = new ArrayList<SIPMessage>();

		long startTime = GregorianCalendar.getInstance().getTimeInMillis();

		List<String> callIdList = new ArrayList<String>();
		callIdList.add("AAA");
		callIdList.add("BBB");

		for (String callId : callIdList) {
			SIPRequest reqInvite = new SIPRequestImpl();
			reqInvite.setMethod("INVITE");
			reqInvite.setSrcAddress("127.0.0.1:5060");
			reqInvite.setDstAddress("127.0.0.1:5061");
			reqInvite.setTime(startTime += 10);
			reqInvite.setCallID(callId);
			messages.add(reqInvite);

			SIPResponse res180 = new SIPResponseImpl();
			res180.setReasonPhrase("Ringing");
			res180.setStatusCode(180);
			res180.setSrcAddress("127.0.0.1:5061");
			res180.setDstAddress("127.0.0.1:5060");
			res180.setTime(startTime += 10);
			res180.setCallID(callId);
			messages.add(res180);

			SIPResponse res200 = new SIPResponseImpl();
			res200.setReasonPhrase("OK");
			res200.setStatusCode(200);
			res200.setSrcAddress("127.0.0.1:5061");
			res200.setDstAddress("127.0.0.1:5060");
			res200.setTime(startTime += 10);
			res200.setCallID(callId);
			messages.add(res200);

			SIPRequest reqBye = new SIPRequestImpl();
			reqBye.setMethod("BYE");
			reqBye.setSrcAddress("127.0.0.1:5060");
			reqBye.setDstAddress("127.0.0.1:5061");
			reqBye.setTime(startTime += 100);
			reqBye.setCallID(callId);
			messages.add(reqBye);

			SIPResponse res200Bye = new SIPResponseImpl();
			res200Bye.setReasonPhrase("OK");
			res200Bye.setStatusCode(200);
			res200Bye.setSrcAddress("127.0.0.1:5061");
			res200Bye.setDstAddress("127.0.0.1:5060");
			res200Bye.setTime(startTime += 10);
			res200Bye.setCallID(callId);
			messages.add(res200Bye);
		}

		return messages;
	}
}
