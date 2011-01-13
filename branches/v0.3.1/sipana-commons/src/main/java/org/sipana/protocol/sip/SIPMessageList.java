package org.sipana.protocol.sip;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
@XmlRootElement(name = "sipmessagelist")
@XmlSeeAlso({SIPMessage.class, SIPRequest.class, SIPResponse.class})
@XmlAccessorType(XmlAccessType.NONE)
public class SIPMessageList extends ArrayList<SIPMessage> {

    public SIPMessageList() {
        super();
    }

    public SIPMessageList(List<SIPMessage> messageList) {
        super(messageList);
    }

    @XmlElementRef
    public List<SIPMessage> getMessages() {
        return this;
    }
}
