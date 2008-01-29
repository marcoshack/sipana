package org.sipana.server.service;

import org.sipana.server.sip.SIPPerformanceMetricsBean;
import org.sipana.server.sip.SIPSessionManagerBean;


public interface Service {
	public static final String SIP_SESSION_MANAGER = SIPSessionManagerBean.class.getSimpleName();
	public static final String SIP_PERFORMANCE_METRICS = SIPPerformanceMetricsBean.class.getSimpleName();
}
