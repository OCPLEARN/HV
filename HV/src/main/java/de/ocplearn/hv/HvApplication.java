package de.ocplearn.hv;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.ocplearn.hv.util.LoggerBuilder;

@SpringBootApplication
public class HvApplication {
	
//	private static String environmentVariable;
//	private static String userVariable;
//	private static String storageEntryPoint = "immodata";
//	private static final String[] BASE_DIRECTORIES = {"aaatests", "backupdb", "log", "pm", "tmp" };
//	public static final String STORAGE_ENTRY_POINT_ABSOLUTE_PATH;
//	
//	
//	static {
//		
//		if( File.separatorChar == '/') {
//			userVariable = "USER";
//			environmentVariable = "HOME";
//			
//		}else if ( File.separatorChar == '\\' ){
//			userVariable = "USERDOMAIN";
//			environmentVariable = "USERPROFILE";
//		}else {
//			throw new IllegalStateException("Environment is neither MACOSX nor Windows. Buy a new computer!");
//		}		
//		// Get the entry point to data storage
//		File storageContainer = new File( System.getenv(environmentVariable) + File.separatorChar + storageEntryPoint );
//		STORAGE_ENTRY_POINT_ABSOLUTE_PATH = storageContainer.getAbsolutePath();
//		
//		if(!storageContainer.exists()) { 
//			System.out.println("No acces to data storage. Entry point not available.");
//			System.exit(1);
//		}
//		for(String directory : BASE_DIRECTORIES) {
//			File file = new File(STORAGE_ENTRY_POINT_ABSOLUTE_PATH + File.separatorChar + directory);
//			if ( ! file.exists() ) {
//				 
//				if( !(file.mkdir()) ) {
//					System.out.println("Could not create the following directory: " + directory);
//					System.exit(1);
//				}
//			}
//		}
//		
//		
//	}
	
	
	public static void main(String[] args) {
		
		SpringApplication.run(HvApplication.class, args);		
	}
}
