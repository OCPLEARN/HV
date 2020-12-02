/**
 * 
 */
package de.ocplearn.hv.test;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.util.AppLogger;
import de.ocplearn.hv.util.LoggerBuilder;

/**
 * Tests folder structure for immodata
 */
@SpringBootTest
//@ConfigurationProperties(prefix = "datavolume")
public class ImmoDataTest {

	private Logger logger;
	
	@Autowired
	public ImmoDataTest(@Autowired LoggerBuilder loggerBuilder ) {
		this.logger = loggerBuilder.build("de.ocplearn.hv");
	}
	
	@Value("${datavolume.storageEntryPoint}")
	private String storageEntryPoint;
	@Value("${datavolume.storageEntryPointAbsolutePath}")
	private String storageEntryPointAbsolutePath;
	
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
	
	
	@Test
	public void test_datavolume_exists() {
		
		File file = new File(storageEntryPointAbsolutePath);
		Assertions.assertTrue(file.exists());
		if ( file.exists() ) {
			logger.log(Level.INFO,"datavolume ok" );
		}else {
			logger.log(Level.SEVERE,"datavolume missing" );
		}
		
	}
	
}
