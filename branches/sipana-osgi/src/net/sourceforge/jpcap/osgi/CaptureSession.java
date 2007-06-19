package net.sourceforge.jpcap.osgi;

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
    
    public PacketListener getListener();
    public String getFilter();
    public String getDevice();
    public int getState();
    public boolean isPromiscuous();

    public void start() throws Exception;
    public void stop() throws Exception;
}
