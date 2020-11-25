package de.ocplearn.hv;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HvApplication {
	
	private static String environmentVariable;
	private static String userVariable;
	private static String storageEntryPoint = "immodata";
	public static final String storageEntryPointAbsolutePath;
	
	static {
		
		if( File.separatorChar == '/') {
			userVariable = "USER";
			environmentVariable = "HOME";
			
		}else if ( File.separatorChar == '\\' ){
			userVariable = "USERDOMAIN";
			environmentVariable = "USERPROFILE";
		}else {
			throw new IllegalStateException("Environment is neither MACOSX nor Windows. Buy a new computer!");
		}		
		// Get the entry point to data storage
		File storageContainer = new File( System.getenv(environmentVariable) + File.separatorChar + storageEntryPoint );
		storageEntryPointAbsolutePath = storageContainer.getAbsolutePath();
		
		if(!storageContainer.exists()) { 
			System.exit(1);
		}	
	}
	
	public static void main(String[] args) {
		SpringApplication.run(HvApplication.class, args);		
	}
}
