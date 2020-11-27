package de.ocplearn.hv.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.HvApplication;
import de.ocplearn.hv.configuration.DataSourceConfig;
import de.ocplearn.hv.configuration.LoggerConfig;



public class LoggerBuilder {
	
	private static LoggerBuilder instance = new LoggerBuilder();
	
	@Autowired
	private LoggerConfig loggerConfig;
	
	@Autowired
	private DataSourceConfig dataSourceConfig;
	
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
		
		System.err.println(dataSourceConfig.getJdbcUrl());
		
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
		// immodata/log/
	    // define location for log files
	    System.err.println(loggerConfig.getUseFileHandler());
	    if(loggerConfig.getUseFileHandler().equalsIgnoreCase("TRUE")) {
	    
		try {
//			if (configProperties == null) {
//				System.out.println( "configProperties is null" );
//			}	
		//	logger.addHandler(new FileHandler( "%h/"+ c.getName() +"%u.log", 1_048_576, 5 ,  true ));
			// "%h/" + "immodata"
			logger.addHandler(new FileHandler(loggerConfig.getFileHandlerLocation()
											+  File.separatorChar
											+  c.getName() 
											+  "%u.log", 
											   1_048_576,
											   5 , 
											   true 
											   ));
	
			
		} catch (SecurityException e) {
			e.printStackTrace(); System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();System.exit(-1);
		}	
	  }	
	    logger.setLevel(Level.parse(loggerConfig.getlevel()));
		return logger;
	}		
	
}
