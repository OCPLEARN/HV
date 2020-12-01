package de.ocplearn.hv.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Formatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.HvApplication;
import de.ocplearn.hv.configuration.DataSourceConfig;
import de.ocplearn.hv.configuration.LoggerConfig;


@Component
public class LoggerBuilder implements ApplicationContextAware {
	
	/* API Print a brief summary of the LogRecord in a human readable format. */
	private static SimpleFormatter formatter = new SimpleFormatter() {
        private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

        @Override
        public synchronized String format(LogRecord lr) {
            return String.format(format,
                    new Date(lr.getMillis()),
                    lr.getLevel().getLocalizedName(),
                    lr.getMessage()
            );		
        }
	};
	
	private static ApplicationContext context;
	
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
         
        // store ApplicationContext reference to access required beans later on
    	LoggerBuilder.context = context;
    }	
	
	private static LoggerBuilder instance;
	
	static {
		// load custom props
	    InputStream stream = 
	    		LoggerBuilder.class
	    		.getClassLoader()
	    		.getResourceAsStream("logging.properties");
	    
	    try {
	          LogManager.getLogManager().readConfiguration(stream);
	          //Logger logger = Logger.getLogger(name)
	
	    } catch (IOException e) {
	          e.printStackTrace(); System.exit(-1);
	    }	
	    
//	    System.setProperty("java.util.logging.SimpleFormatter.format", 
//	              "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");	    
	    
	}
	
	private LoggerConfig loggerConfig;
	
	@Autowired
	public LoggerBuilder(@Autowired LoggerConfig loggerConfig) {
		this.loggerConfig = loggerConfig;
		LoggerBuilder.instance = this;
	}
	
	/**
	 * Returns builder instance
	 * 
	 * @return LoggerBuilder
	 * */
	public static LoggerBuilder getInstance() {
//		if ( LoggerBuilder.instance == null ) {
//			//LoggerBuilder.instance = LoggerBuilder.appContext.getBean(LoggerBuilder.class);
//		}
		return instance;
	}
	
	/**
	 * Returns a combined ConsoleHandler and FileHandler Logger
	 * 
	 * @param from https://docs.oracle.com/javase/8/docs/api/
		name - A name for the logger. This should be a dot-separated name and should
 		normally be based on the package name or class name of the subsystem, such as java.net or javax.swing 
	 * @return Logger
	 * */	
	public Logger build( String name ) {
		Logger logger = Logger.getLogger( name );
		
		// #1 console handler
		ConsoleHandler consoleHandler = new ConsoleHandler();
		
		logger.addHandler(consoleHandler);
		
		if(loggerConfig.getUseFileHandler().equalsIgnoreCase("TRUE")) {
			try {
				
				FileHandler fileHandler = new FileHandler(
						loggerConfig.getFileHandlerLocation() +  File.separatorChar +  name +  "%u.log", 
						   1_048_576,
						   5 , 
						   true 
						 );
				
				// #2 file handler
				logger.addHandler(
						fileHandler
				);
				
				//fileHandler.setFormatter( LoggerBuilder.formatter );
				
			} catch (SecurityException e) {
				e.printStackTrace(); System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();System.exit(-1);
			}			
		}
		
		//Handler[] handler = ;
		for( Handler handler : logger.getHandlers() ) {
			handler.setFormatter(formatter);
		}
		
	    logger.setLevel(Level.parse(loggerConfig.getlevel()));
	    
		return logger;		
	}
	
	/**
	 * Returns a combined ConsoleHandler and FileHandler Logger
	 * 
	 * @param c Class for which Logger is provided
	 * @return Logger
	 * */
	public Logger build( Class c ) {
		return this.build(c.getName());
//		Logger logger = Logger.getLogger(c.getName());
//
//	    System.err.println( "LoggerBuilder build() called from " + c.getName() );
//	    
//	    if(loggerConfig.getUseFileHandler().equalsIgnoreCase("TRUE")) {
//	    
//			try {
//		
//			//	logger.addHandler(new FileHandler( "%h/"+ c.getName() +"%u.log", 1_048_576, 5 ,  true ));
//				// "%h/" + "immodata"
//				logger.addHandler(new FileHandler(loggerConfig.getFileHandlerLocation()
//												+  File.separatorChar
//												+  c.getName() 
//												+  "%u.log", 
//												   1_048_576,
//												   5 , 
//												   true 
//												   ));
//				
//			} catch (SecurityException e) {
//				e.printStackTrace(); System.exit(-1);
//			} catch (IOException e) {
//				e.printStackTrace();System.exit(-1);
//			}	
//	  }	
//	    logger.setLevel(Level.parse(loggerConfig.getlevel()));
//		return logger;
	}		

}
