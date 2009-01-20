package org.sipana.protocol.sip;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
@XmlRootElement(name = "sipmessages")
public class SIPMessageList {

    @XmlElementRef
    private List<SIPMessage> messages;

    public SIPMessageList() {
        messages = new LinkedList<SIPMessage>();
    }

    public SIPMessageList(List<SIPMessage> messageList) {
        messages = messageList;
    }

    public List<SIPMessage> getMessages() {
        return messages;
    }

    public void add(SIPMessage message) {
        messages.add(message);
    }
}
