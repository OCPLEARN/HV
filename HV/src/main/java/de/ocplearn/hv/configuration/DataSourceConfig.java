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
public class DataSourceConfig {

	private Logger logger = LoggerBuilder.getInstance().build(DataSourceConfig.class);
	
    @Bean(name = "datasource1")
    @ConfigurationProperties("database1.datasource")
    @Primary
    public DataSource dataSource(){
    	logger.log(Level.INFO, "### datasource requested");
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
