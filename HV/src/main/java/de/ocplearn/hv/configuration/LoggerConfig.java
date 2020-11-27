package de.ocplearn.hv.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="logger")
public class LoggerConfig {
	
	private String level;
	
	private String useFileHandler;
	
	private String fileHandlerLocation;

	/**
	 * @return the info
	 */
	public String getlevel() {
		return level;
	}

	/**
	 * @param info the info to set
	 */
	public void setlevel(String level) {
		this.level = level;
	}

	/**
	 * @return the useFileHandler
	 */
	public String getUseFileHandler() {
		return useFileHandler;
	}

	/**
	 * @param useFileHandler the useFileHandler to set
	 */
	public void setUseFileHandler(String useFileHandler) {
		this.useFileHandler = useFileHandler;
	}

	/**
	 * @return the fileHandlerLocation
	 */
	public String getFileHandlerLocation() {
		return fileHandlerLocation;
	}

	/**
	 * @param fileHandlerLocation the fileHandlerLocation to set
	 */
	public void setFileHandlerLocation(String fileHandlerLocation) {
		this.fileHandlerLocation = fileHandlerLocation;
	}
	
	
	

}
