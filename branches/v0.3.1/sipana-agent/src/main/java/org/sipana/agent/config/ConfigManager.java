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
package org.sipana.agent.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigManager {
    // Config properties
    public static final String PROPERTY_SENDER_MODE = "org.sipana.agent.sender.mode";
    public static final String PROPERTY_SENDER_INTERVAL = "org.sipana.agent.sender.interval";
    public static final String PROPERTY_SENDER_RECONNECTION_INTERVAL = "org.sipana.agent.sender.reconnection_interval";
    public static final String PROPERTY_SENDER_DESTINATION_NAME = "org.sipana.agent.sender.destination";
    public static final String PROPERTY_BUFFER_SIZE = "org.sipana.agent.buffer.size";
    public static final String PROPERTY_CAPTURE_INTERFACE = "org.sipana.agent.capture.interface";
    public static final String PROPERTY_CAPTURE_FILTER = "org.sipana.agent.capture.filter";
    
    // Config constants
    private static final String CONFIG_SENDER_MODE_DELAYED = "delayed";
    private static final String CONFIG_SENDER_MODE_IMMEDIATELY = "immediately";
//    private static final String CONFIG_BUFFER_OVERFLOW_ACTION_DISCARD = "discard";
    
    // Config default values
    private static final String DEFAULT_SENDER_PERIOD = "60";
    private static final String DEFAULT_SENDER_RECONNECTION_PERIOD = "60";
    private static final String DEFAULT_SENDER_DESTINATION_NAME = "queue/org.sipana.sip";
    private static final String DEFAULT_BUFFER_SIZE = "1000";
    
    private static final String CONFIG_FILE = System.getProperty("sipana.agent.properties");

    private static Logger logger = Logger.getLogger(ConfigManager.class);
    private static ConfigManager instance = null;
    private static Properties configProperties;
    
    private ConfigManager() {
        configProperties = new Properties();
    }
    
    public static ConfigManager getInstance() throws Exception {
        if (instance == null) {
            instance = new ConfigManager(); 
            instance.start();
            
        }
        
        return instance;
    }
    
    private void start() throws Exception {
        configureClientProperties();
    }

    private void configureClientProperties() throws ConfigException {
        try {
        	if (ConfigManager.CONFIG_FILE == null) {
            	throw new ConfigException("Configuration file name cannot be null, "
            			+ "set property sipana.agent.properties with "
            			+ "client.properties file path");
            }
        	
        	FileInputStream configFile = new FileInputStream(ConfigManager.CONFIG_FILE);
        	configProperties.load(configFile);
        	
        	if (logger.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Client config properties: ");
                sb.append(configProperties);
                logger.debug(sb);
            }
        
        } catch (IOException e) {
			throw new ConfigException("Fail loading client config file", e);
		}
    }
    
    public boolean isSenderModeDelayed() {
        String sendMode = configProperties.getProperty(PROPERTY_SENDER_MODE, CONFIG_SENDER_MODE_IMMEDIATELY);
        return (sendMode.toLowerCase().equals(CONFIG_SENDER_MODE_DELAYED));
    }

    public long getDelayedSenderInterval() {
        String interval = configProperties.getProperty(PROPERTY_SENDER_INTERVAL, DEFAULT_SENDER_PERIOD);
        return Integer.parseInt(interval);
    }

    public long getSenderReconnectionInterval() {
        String interval = configProperties.getProperty(PROPERTY_SENDER_RECONNECTION_INTERVAL, DEFAULT_SENDER_RECONNECTION_PERIOD);
        return Integer.parseInt(interval);
    }

    public int getBufferSize() {
        String size = configProperties.getProperty(PROPERTY_BUFFER_SIZE, DEFAULT_BUFFER_SIZE);
        return Integer.parseInt(size);
    }
    
	public String getSenderDestination() {
		String destination = configProperties.getProperty(PROPERTY_SENDER_DESTINATION_NAME, DEFAULT_SENDER_DESTINATION_NAME);
		return destination;
	}
	
	public String getCaptureInterface() {
		String iface = configProperties.getProperty(PROPERTY_CAPTURE_INTERFACE, null);
		return iface;
	}
	
	public String getCaptureFilter() {
		String filter = configProperties.getProperty(PROPERTY_CAPTURE_FILTER, null);
		return filter;
	}

    public String getConfigFileName() {
        return ConfigManager.CONFIG_FILE;
    }
}
