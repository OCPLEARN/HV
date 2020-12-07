package de.ocplearn.hv.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.HvApplication;
import de.ocplearn.hv.util.DBConnectionPool;
import de.ocplearn.hv.util.LoggerBuilder;

// ApplicationReadyEvent
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationPreparedEvent> {

	private static final String[] BASE_DIRECTORIES = {"aaatests", "backupdb", "log", "pm", "tmp" };
	
	// trigger LoggerBuilder creation
	//@Autowired 
	//LoggerBuilder builder;
	
	// trigger DBConnectionPool creation
	@Autowired
	private DBConnectionPool dBConnectionPool;		
	
	// trigger LoggerBuilder creation
	//@Autowired
	//private LoggerBuilder builder;
	// (1)
	@Value("${datavolume.storageEntryPoint}")
	private  String storageEntryPoint;
	@Value("${datavolume.storageEntryPointAbsolutePath}")
	private  String storageEntryPointAbsolutePath;

	// (2) uses (1)
	{
//		System.err.println("storageEntryPointAbsolutePath:   "+storageEntryPointAbsolutePath);
//		if (storageEntryPointAbsolutePath == null) System.exit(-1);
//		this.checkImmoDataDirectories2();
	}	

	@Autowired
	LoggerConfig loggerConfig;
	
	@Autowired
	public ApplicationStartup( ) {
		//this.logger = this.builder.build("de.ocplearn.hv.HvApplication");
		//this.checkImmoDataDirectories2();
		
		System.err.println("storageEntryPointAbsolutePath:   "+storageEntryPointAbsolutePath);
		if (storageEntryPointAbsolutePath == null) System.exit(-1);
		this.checkImmoDataDirectories2();		
	}
	
  /**
	 * @return the storageEntryPoint
	 */
	public String getStorageEntryPoint() {
		return storageEntryPoint;
	}

	/**
	 * @param storageEntryPoint the storageEntryPoint to set
	 */
	public void setStorageEntryPoint(String storageEntryPoint) {
		this.storageEntryPoint = storageEntryPoint;
	}

	/**
	 * @return the storageEntryPointAbsolutePath
	 */
	public String getStorageEntryPointAbsolutePath() {
		return storageEntryPointAbsolutePath;
	}

	/**
	 * @param storageEntryPointAbsolutePath the storageEntryPointAbsolutePath to set
	 */
	public void setStorageEntryPointAbsolutePath(String storageEntryPointAbsolutePath) {
		this.storageEntryPointAbsolutePath = storageEntryPointAbsolutePath;
	}

/**
   * This event is executed as late as conceivably possible to indicate that 
   * the application is ready to service requests.
   */
  @Override
  public void onApplicationEvent(final ApplicationPreparedEvent event) {

//	  this.logger = this.builder.build("de.ocplearn.hv.HvApplication");
//	  this.logger.setLevel(Level.FINEST);
//	  // log a FINER message
//	  this.logger.log(Level.INFO, "HvApplication starting ...");
	  
	  //String storageEntryPointAbsolutePath = storageEntryPointAbsolutePath;
	  
	  //System.err.println("onApplicationEvent() - storageEntryPointAbsolutePath =  " + storageEntryPointAbsolutePath);
	
	  if(!absolutePathExists()) {
		  this.checkImmoDataDirectories2();
	  	}

    return;
  }
  
  	boolean absolutePathExists() {
  		File file = new File( storageEntryPointAbsolutePath );
  		return file.exists();
  	}
 
  	
  	/**
  	 * 20201207 fin
  	 * checks for data storage by looking up application.properties variable 
  	 * datavolume.storageEntryPointAbsolutePath via bean DataVolumeProperties
  	 * */
  	private void checkImmoDataDirectories2() {
  		Path storageContainer = Paths.get( getStorageEntryPointAbsolutePath() );
  		if ( !Files.exists(storageContainer) ) {
			System.out.println("No acces to data storage. Entry point not available.");
			System.exit(1);  			
  		}
  		
  		for(String directory : BASE_DIRECTORIES) {
				try {
				Files.createDirectory( storageContainer.resolve(directory) );
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("No acces to data storage. Entry point "+ directory +" could not be created.");
				System.exit(1);					
			}
		} 		
  		
  	}
  	
  	/**
  	 * checks for data storage by looking up folder immodata in users home directory
  	 * */
  	private void checkImmoDataDirectories() {
  			
  	  String environmentVariable;
  	  String userVariable;
  		
		if( File.separatorChar == '/') {
			userVariable = "USER";
			environmentVariable = "HOME";
			
		}else if ( File.separatorChar == '\\' ){
			userVariable = "USERDOMAIN";
			environmentVariable = "USERPROFILE";
		}else {
			throw new IllegalStateException("Environment is neither MACOSX nor Windows. Buy a new computer!");
		}		
		// Get the entry point to data storage if not existent
		File storageContainer = new File( System.getenv(environmentVariable) + File.separatorChar + storageEntryPoint  );
		storageEntryPointAbsolutePath = storageContainer.getAbsolutePath();

		if(!storageContainer.exists()) { 
			System.out.println("No acces to data storage. Entry point not available.");
			System.exit(1);
		}  	
	
		  // check for HV_IMMO_DATA directories
		for(String directory : BASE_DIRECTORIES) {
			File file = new File(storageEntryPointAbsolutePath + File.separatorChar + directory);
			if ( ! file.exists() ) {
				if( !(file.mkdir()) ) {
					System.out.println("Could not create the following directory: " + directory);
					System.exit(1);
				}
			System.out.println("ApplicationStartup - created = " + file.getAbsolutePath());
			}
		}		
		
  	}
  	
  	/**
  	 * initializes application before spring boot
  	 * 
  	 * */
  	public static void init() {
  		
  	}
  	
} 