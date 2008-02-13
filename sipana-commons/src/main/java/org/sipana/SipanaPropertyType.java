package org.sipana;

public enum SipanaPropertyType {

    SIPSESSION_STATUS_INITIATED("sipana.sip.status.initiated", "INITIATED"),
    
    SIPSESSION_STATUS_ESTABLISHED("sipana.sip.status.established", "ESTABLISHED"),
    
    SIPSESSION_STATUS_COMPLETED("sipana.sip.status.completed", "COMPLETED"),
    
    SIPSESSION_STATUS_FAILED("sipana.sip.status.failed", "FAILED"),
    
    SIPSESSION_STATUS_TIMEOUT("sipana.sip.status.timeout", "TIMEOUT"),
    
    SIPSESSION_STATUS_UNKNOWN("sipana.sip.status.unknown", "UNKNOWN"),
    
    SIPSCENARIO_ARROW_WIDTH("sipana.sip.scenario.arrowsize", "8"),
    
    SIPSCENARIO_ARROW_HEIGHT("sipana.sip.scenario.arrowsize", "3"),
    
    SIPSCENARIO_COLORS("sipana.sip.scenario.colors", "64ff64,79b8ff,fbff93,ff9a56,b0ff63,fc8bf0,ff8d8d");
    
    private String key;
    private String defaultValue;

    private SipanaPropertyType(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return this.key;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public String toString() {
        return this.key;
    }
}
