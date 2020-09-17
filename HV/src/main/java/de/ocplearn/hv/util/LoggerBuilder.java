package de.ocplearn.hv.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerBuilder {
	
	private static LoggerBuilder instance = new LoggerBuilder();
	
	private LoggerBuilder() {
		
	}
	
	/**
	 * Returns builder instance
	 * 
	 * @return LoggerBuilder
	 * */
	public static LoggerBuilder getInstance() {
		return instance;
	}
	
	/**
	 * Returns a combined ConsoleHandler and FileHandler Logger
	 * 
	 * @param c Class for which Logger is provided
	 * @return Logger
	 * */
	public Logger build( Class c ) {
		
		Logger logger = null;
		
		// load custom props
	    InputStream stream = 
	    		c
	    		.getClassLoader()
	    		.getResourceAsStream("logging.properties");
	    
	    try {
	          LogManager.getLogManager().readConfiguration(stream);
	          logger = Logger.getLogger(c.getName());
	
	    } catch (IOException e) {
	          e.printStackTrace(); System.exit(-1);
	    }		
	      
		// add a FileHandler default pattern %h/java%u.log
		try {
			logger.addHandler(new FileHandler( "%h/"+ c.getName() +"%u.log", 1_048_576, 5 ,  true ));
		} catch (SecurityException e) {
			e.printStackTrace(); System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();System.exit(-1);
		}	
			
		return logger;
	}		
	
}
