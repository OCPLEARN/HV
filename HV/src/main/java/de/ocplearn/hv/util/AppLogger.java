/**
 * 
 */
package de.ocplearn.hv.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Provides Logging to file or console
 *
 */
@Component("FileAndConsoleLogger")
public class AppLogger {

	@Value("${datavolume.storageEntryPoint}")
	private  String storageEntryPoint;
	@Value("${datavolume.storageEntryPointAbsolutePath}")
	private  String storageEntryPointAbsolutePath;	
	
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
		// immodata/log/
	    // define location for log files

		try {
//			if (configProperties == null) {
//				System.out.println( "configProperties is null" );
//			}	
		//	logger.addHandler(new FileHandler( "%h/"+ c.getName() +"%u.log", 1_048_576, 5 ,  true ));
			// "%h/" + "immodata"
			logger.addHandler(new FileHandler( storageEntryPointAbsolutePath
											+  File.separatorChar 
											+  "log"
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
			
		return logger;
	}			
	
}
