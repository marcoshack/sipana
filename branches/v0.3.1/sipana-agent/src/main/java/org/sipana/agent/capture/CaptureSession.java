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


public interface CaptureSession {   
    public static int STATE_RUNINNG = 1;
    public static int STATE_IDLE = 2;
    public static int STATE_ERROR = 3;
    
    public void setListener(CaptureListener listener);
    public void setFilter(String filter);
    public void setDevice(String device);
    public void setPromiscuous(boolean promiscuous);
    
    public CaptureListener getListener();
    public String getFilter();
    public String getDevice();
    public int getState();
    public boolean isPromiscuous();
    public String getId();

    public void start() throws Exception;
    public void stop() throws Exception;
}
