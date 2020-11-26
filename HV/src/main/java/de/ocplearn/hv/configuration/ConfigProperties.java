package de.ocplearn.hv.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

// aim of this class:
// import configuration data from file which is located external of this application


@Configuration
@ConfigurationProperties(prefix = "datavolume") /* dataVolume in application.properties*/
public class ConfigProperties {
	

	private  String storageEntryPoint;
	private  String storageEntryPointAbsolutePath;
	
	
	public String getStorageEntryPoint() {
		return storageEntryPoint;
	}
	public void setStorageEntryPoint(String storageEntryPoint) {
		this.storageEntryPoint = storageEntryPoint;
	}
	public String getStorageEntryPointAbsolutePath() {
		return storageEntryPointAbsolutePath;
	}
	public void setStorageEntryPointAbsolutePath(String storageEntryPointAbsolutePath) {
		this.storageEntryPointAbsolutePath = storageEntryPointAbsolutePath;
	}
	
	

}
