package com.buptmap.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

public class ProcessProperties {
	private static final Logger logger = Logger.getLogger(ProcessProperties.class);
	private static final String propsFileName = "/vmap.properties"; 
	public static Properties props = new Properties();

	public ProcessProperties() {
		try {
			loadProperties();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}

	public void loadProperties() throws IOException {
		InputStream fis = null;
		try {
			fis = getClass().getResourceAsStream(propsFileName);
			props.load(fis);
			System.out.println("LOAD ACCESS PROPERTIES.");
			logger.info("LOAD ACCESS PROPERTIES.");
		} catch(Exception e) {
			System.out.println("ERROR LOADING PROPERTIES.");
			logger.info("ERROR LOADING PROPERTIES.");
			e.printStackTrace();
		} finally {
			fis.close();
		}
	}
	
	public Set<Object> getKeySet() {
		return props.keySet();
	}

	public String getValue(String key) {
		try {
			if (props != null && props.containsKey(key)) {
				return props.getProperty(key);
			}
		} catch(Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
