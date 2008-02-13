package org.sipana;

public class SipanaProperties {
    
    public static String getProperty(SipanaPropertyType p)
    {
        return System.getProperty(p.getKey(), p.getDefaultValue());
    }
    
    public static int getPropertyInt(SipanaPropertyType p) {
        return Integer.parseInt(getProperty(p));
    }
    
    public static float getPropertyFloat(SipanaPropertyType p) {
        return Float.parseFloat(getProperty(p));
    }
    
    public static double getPropertyDouble(SipanaPropertyType p) {
        return Double.parseDouble(getProperty(p));
    }
    
    public static void setProperty(SipanaPropertyType p, String value) {
        System.setProperty(p.getKey(), value);
    }
}
