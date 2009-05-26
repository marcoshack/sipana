/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sipana.server.ejb.impl;

import java.util.Date;
import java.util.List;
import org.sipana.protocol.sip.SIPSessionState;

/**
 *
 * @author mhack
 */
public class SIPSessionFindParams {
    private Date startDate;
    private Date endDate;
    private String requestMethod;
    private String callID;
    private Integer responseCode;
    private String fromUser;
    private String toUser;
    private List<String> srcAddressList;
    private List<String> dstAddressList;
    private SIPSessionState state;

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public List<String> getDstAddressList() {
        return dstAddressList;
    }

    public void setDstAddressList(List<String> dstAddressList) {
        this.dstAddressList = dstAddressList;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public List<String> getSrcAddressList() {
        return srcAddressList;
    }

    public void setSrcAddressList(List<String> srcAddressList) {
        this.srcAddressList = srcAddressList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public SIPSessionState getState() {
        return state;
    }

    public void setState(SIPSessionState state) {
        this.state = state;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
}
