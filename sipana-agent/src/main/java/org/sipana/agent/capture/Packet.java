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
package org.sipana.agent.capture;

import java.util.Date;

/**
 * A transport independent packet representation to abstract underlying packet
 * object.
 * 
 * @author Marcos Hack <marcoshack@gmail.com>
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
