package org.sipana.client.config;

public class ConfigException extends Exception {
	private static final long serialVersionUID = -4088134407939225553L;
	
	public ConfigException(String message) { 
		super(message);
	}
	
	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

}
