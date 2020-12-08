package de.ocplearn.hv.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.configuration.LoggerConfig;


@Component
public class LoggerBuilder {
	
	/* API Print a brief summary of the LogRecord in a human readable format. */
	private static SimpleFormatter formatter = new SimpleFormatter() {
		
        private static final String format = "[%1$tF %1$tT] [%2$-7s] class:%3$s method:%4$s msg:%5$s thrown:%6$s %n";

        // first [] represents Date in ISO 8601 (%1$tF) and Time in long since 1.1.1970 (%1$tF)
        // second [] represents LogLevel with max number of 7 spaces
        // https://dzone.com/articles/java-string-format-examples
        // class: represents full information a class
        // method: represents the method that calls the the logger
        // msg: represents the supplied log message
        // thrown: represents Throwable => Exception or null if no exception
        
        
        @Override
        public synchronized String format(LogRecord lr) {
        	
            return String.format(format,
                    Instant.now().toEpochMilli(),
                    lr.getLevel().getLocalizedName(),		// 2
                    lr.getSourceClassName(),
                    lr.getSourceMethodName(),	// 3, 4
                    lr.getMessage(),	// 5
                    lr.getThrown() 		// 6
            );	        			
        }
	};
	
	private static LoggerBuilder instance; // new LoggerBuilder()
	
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

	// all *logger* properties from application context
	private LoggerConfig loggerConfig;
	
	// set to true if log directory has been checked
	private boolean logDirChecked = false;
	
	@Autowired
	public LoggerBuilder( @Autowired LoggerConfig loggerConfig) {
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
		
//		if ( ! this.logDirChecked ) {
//			
//			Path pathToLogDir = Paths.get( loggerConfig.getFileHandlerLocation() ); 
//			
//			if ( !Files.exists( pathToLogDir )
//					) {
//				try {
//					Files.createDirectories( pathToLogDir );
//					System.err.println("Logger - build() created missing log directory!");
//				} catch (IOException e) {
//					e.printStackTrace();
//					System.out.println("No acces to data storage. Entry point not available.");
//					System.exit(1);  						
//				}
//				this.logDirChecked = true;
//			}
//		}
		
		Logger logger = Logger.getLogger( name );
		
		// check logger already built
		if ( logger.getHandlers().length > 1 ) {
			return logger;
		}
		
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
	public <T> Logger build(Class<T> c ) {
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
