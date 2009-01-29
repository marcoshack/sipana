package org.sipana.client.capture;

import java.util.Date;

/**
 * A transport independent packet representation to abstract underlying packet
 * object.
 * 
 * @author mhack
 */
public class Packet {
    private String srcIPAddr;
    private String dstIPAddr;
    private int srcIPPort;
    private int dstIPPort;
    private Date date;
    private String data;

    public String getSrcIPAddr() {
        return srcIPAddr;
    }

    public void setSrcIPAddr(String srcIPAddr) {
        this.srcIPAddr = srcIPAddr;
    }

    public String getDstIPAddr() {
        return dstIPAddr;
    }

    public void setDstIPAddr(String dstIPAddr) {
        this.dstIPAddr = dstIPAddr;
    }

    public int getSrcIPPort() {
        return srcIPPort;
    }

    public void setSrcIPPort(int srcIPPort) {
        this.srcIPPort = srcIPPort;
    }

    public int getDstIPPort() {
        return dstIPPort;
    }

    public void setDstIPPort(int dstIPPort) {
        this.dstIPPort = dstIPPort;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date time) {
        this.date = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
