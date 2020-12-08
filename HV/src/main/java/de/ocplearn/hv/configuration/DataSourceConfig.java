package de.ocplearn.hv.configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import de.ocplearn.hv.util.LoggerBuilder;

@Configuration
@ConfigurationProperties("database1.datasource")
public class DataSourceConfig {
	
	private String jdbcUrl;
	private String username;
	private String password;
	private String driver_class_name;

	private Logger logger; // = LoggerBuilder.getInstance().build(DataSourceConfig.class);
	
	/**
	 * @return the jdbcUrl
	 */
	public String getJdbcUrl() {
		//System.out.println("DataSourceConfig getJdbcUrl() = " + jdbcUrl);
		return jdbcUrl;
	}

	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the driver_class_name
	 */
	public String getDriver_class_name() {
		return driver_class_name;
	}

	/**
	 * @param driver_class_name the driver_class_name to set
	 */
	public void setDriver_class_name(String driver_class_name) {
		this.driver_class_name = driver_class_name;
	}
	
	
    @Bean(name = "datasource1")
    @ConfigurationProperties("database1.datasource")
    @Primary
    public DataSource dataSource(){
    	//logger.log(Level.INFO, "### datasource requested");
        return DataSourceBuilder.create().build();
    }	
	
    @Bean(name="tm1")
    @Autowired
    @Primary
    DataSourceTransactionManager tm1(@Qualifier ("datasource1") DataSource datasource) {
        DataSourceTransactionManager txm  = new DataSourceTransactionManager(datasource);
        return txm;
    }    
    
//    @Bean(name="mySqlDataSource")
//    @Primary
//    public DataSource getDataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
//        dataSourceBuilder.url("jdbc:mysql://localhost:3306/immodb?serverTimezone=Europe/Rome");
//        dataSourceBuilder.username("immodb");
//        dataSourceBuilder.password("Pa$$w0rd");
//        
//        logger.log(Level.WARNING, "OK?");
//        
//        return dataSourceBuilder.build();
//    }	
	
}
