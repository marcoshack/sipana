package br.com.voicetechnology.sipana;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import br.com.voicetechnology.sipana.capturer.Dispatcher;
import br.com.voicetechnology.sipana.capturer.pcap.CapturerPcap;
import br.com.voicetechnology.sipana.http.HttpServer;
import br.com.voicetechnology.sipana.http.HttpSipStatisticHandler;
import br.com.voicetechnology.sipana.sip.SipMessageHandler;
import br.com.voicetechnology.sipana.statistic.StatisticSip;

public class SipanaCapturer extends Thread {
	private static Logger logger;
	private static Properties properties;
	private Dispatcher dispatcher;
	private SipMessageHandler handler;
	private StatisticSip statistics;
	private HttpServer http_server;
	private CapturerPcap capturer;
	
	public SipanaCapturer() throws Exception {
		logger = Logger.getLogger(SipanaCapturer.class);
		properties = new Properties();

		// Get configuration properties
		
		// TODO: Obter e validar opcoes de linha de comando usando Jakarta CLI
		// TODO: Validar configuracoes obritatorias
		// TODO: Implementar mecanismo padrao de propriedades com valores default. 

		String config_file = System.getProperty("sipana.config");
		if (config_file == null) {
			throw new Exception("Config file not set.");
		}

		try {
			FileInputStream in = new FileInputStream(config_file);
			properties.load((InputStream) in);
			properties.list(System.out);
			
			handler = new SipMessageHandler();
			dispatcher = new Dispatcher(handler);
			statistics = StatisticSip.getInstance();
			http_server = new HttpServer(new HttpSipStatisticHandler(statistics));
			
		} catch (IOException e) {
			logger.fatal("Cannot open config file", e);
			System.exit(1);
		} catch (Exception e) {
			logger.fatal("Error starting Sipana", e);
			System.exit(2);
		}
	}
	
	public static Properties getProperties() {
		return properties;
	}
		
	public void run() {
		this.setName("SipanaCapturer");
		
		try {
			// Start capturer
			capturer = new CapturerPcap(dispatcher);
			dispatcher.start();
			capturer.start();
			http_server.start();			
		} catch (Exception e) {
			logger.fatal("Error running JSipana", e);
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		try {
			SipanaCapturer sipana = new SipanaCapturer();
			sipana.start();
		} catch (Exception e) {
			logger.fatal(e);
		}
	}
}
