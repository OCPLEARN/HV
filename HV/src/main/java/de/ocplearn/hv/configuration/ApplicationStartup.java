package de.ocplearn.hv.configuration;

import java.io.File;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

@Autowired
ConfigProperties configProperties;

	
  /**
   * This event is executed as late as conceivably possible to indicate that 
   * the application is ready to service requests.
   */
  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {
 
	  
	  String environmentVariable;
	  String userVariable;
	  String[] BASE_DIRECTORIES = {"aaatests", "backupdb", "log", "pm", "tmp" };
	  String storageEntryPointAbsolutePath = configProperties.getStorageEntryPointAbsolutePath();
	  
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
			File storageContainer = new File( System.getenv(environmentVariable) + File.separatorChar + configProperties.getStorageEntryPoint()  );
			storageEntryPointAbsolutePath = storageContainer.getAbsolutePath();
	
			if(!storageContainer.exists()) { 
				System.out.println("No acces to data storage. Entry point not available.");
				System.exit(1);
				
			}
			
			configProperties.setStorageEntryPointAbsolutePath(storageEntryPointAbsolutePath);
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
  		File file = new File( configProperties.getStorageEntryPoint() );
  		return file.exists();
  	}
 
} 