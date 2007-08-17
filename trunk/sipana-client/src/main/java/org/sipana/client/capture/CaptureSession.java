
/**
 * This file is part of Sipana project <http://sipana.org/>
 * 
 * Sipana is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sipana is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */ 

package org.sipana.client.capture;

import net.sourceforge.jpcap.capture.PacketListener;

public interface CaptureSession {
    public static int DEFAULT_SNAPLEN = 1500;
    public static int DEFAULT_TIMEOUT = -1;
    public static int DEFAULT_COUNT = -1; // Infinite
    
    public static int RUNINNG = 1;
    public static int IDLE = 2;
    public static int FAIL = 3;
    
    public void setListener(PacketListener listener);
    public void setFilter(String filter);
    public void setDevice(String device);
    public void setPromiscuous(boolean promiscuous);
    public void setId(String id);
    
    public PacketListener getListener();
    public String getFilter();
    public String getDevice();
    public int getState();
    public boolean isPromiscuous();
    public String getId();

    public void start() throws Exception;
    public void stop() throws Exception;
}
