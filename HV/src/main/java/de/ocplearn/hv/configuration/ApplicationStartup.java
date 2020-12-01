package de.ocplearn.hv.configuration;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.HvApplication;
import de.ocplearn.hv.util.LoggerBuilder;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private LoggerBuilder builder;
	private Logger logger;

	@Autowired
	LoggerConfig loggerConfig;
	
	@Value("${datavolume.storageEntryPoint}")
	private  String storageEntryPoint;
	@Value("${datavolume.storageEntryPointAbsolutePath}")
	private  String storageEntryPointAbsolutePath;

//	@Autowired
//	public ApplicationStartup() {
//		this.logger = this.builder.build("de.ocplearn.hv.HvApplication");
//	}
	
  /**
	 * @return the storageEntryPoint
	 */
	private String getStorageEntryPoint() {
		return storageEntryPoint;
	}

	/**
	 * @param storageEntryPoint the storageEntryPoint to set
	 */
	private void setStorageEntryPoint(String storageEntryPoint) {
		this.storageEntryPoint = storageEntryPoint;
	}

	/**
	 * @return the storageEntryPointAbsolutePath
	 */
	private String getStorageEntryPointAbsolutePath() {
		return storageEntryPointAbsolutePath;
	}

	/**
	 * @param storageEntryPointAbsolutePath the storageEntryPointAbsolutePath to set
	 */
	private void setStorageEntryPointAbsolutePath(String storageEntryPointAbsolutePath) {
		this.storageEntryPointAbsolutePath = storageEntryPointAbsolutePath;
	}

/**
   * This event is executed as late as conceivably possible to indicate that 
   * the application is ready to service requests.
   */
  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {

//	  this.logger = this.builder.build("de.ocplearn.hv.HvApplication");
//	  this.logger.setLevel(Level.FINEST);
//	  // log a FINER message
//	  this.logger.log(Level.INFO, "HvApplication starting ...");
	  
	  String environmentVariable;
	  String userVariable;
	  String[] BASE_DIRECTORIES = {"aaatests", "backupdb", "log", "pm", "tmp" };
	  //String storageEntryPointAbsolutePath = storageEntryPointAbsolutePath;
	  
	  //System.err.println("onApplicationEvent() - storageEntryPointAbsolutePath =  " + storageEntryPointAbsolutePath);
	
	  if(!absolutePathExists()) {
	  
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
	  	}
	  
		for(String directory : BASE_DIRECTORIES) {
			File file = new File(storageEntryPointAbsolutePath + File.separatorChar + directory);
			if ( ! file.exists() ) {
				 
				if( !(file.mkdir()) ) {
					System.out.println("Could not create the following directory: " + directory);
					System.exit(1);
				}
			}
		}
	
    return;
  }
  
  	boolean absolutePathExists() {
  		File file = new File( storageEntryPointAbsolutePath );
  		return file.exists();
  	}
 
} 