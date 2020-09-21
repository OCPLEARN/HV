package de.ocplearn.hv.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * 
 * https://www.journaldev.com/2509/java-datasource-jdbc-datasource-example
 * https://stackoverflow.com/questions/18157290/javax-sql-datasource-getconnection-returns-null
 * https://www.journaldev.com/2513/tomcat-datasource-jndi-example-java#:~:text=Tomcat%20DataSource%20JNDI%20Configuration%20Example%20%E2%80%93%20server.,-xml&text=Add%20below%20code%20in%20the,present%20in%20the%20tomcat%20lib.
 * */
public class MySQLDataSourceFactory {

	private static DataSource mysqlDS;
	
	/**
	 * Initializes the DataSource from jdbc_con.properties on classpath
	 * 
	 * */
	
	
	public static void initDS() {
		
		if (mysqlDS == null) {
			Properties props = new Properties();
			FileInputStream fis = null;
			MysqlDataSource mysqlDS = null;
			try {
				
				InputStream is = 
			    		MySQLDataSourceFactory.class
			    		.getClassLoader()
			    		.getResourceAsStream("jdbc_con.properties");				
				
				props.load(is);
				mysqlDS = new MysqlDataSource();
				mysqlDS.setURL(props.getProperty("url"));
				mysqlDS.setUser(props.getProperty("user"));
				mysqlDS.setPassword(props.getProperty("password"));
				
				
				
				System.out.println("??????????????????? MySQLDataSourceFactory (with URL : "+mysqlDS.getUrl()+") initiated");
				
			} catch (IOException e) {
				e.printStackTrace();;
			}			
		}
		
	}
	
	static {
		initDS();
	}
	
	/**
	 * 
	 * 
	 * @return DataSource
	 * */
	public static DataSource getMySQLDataSource() {
		return mysqlDS;
	}
	
	
	public static void main(String[] args ) throws SQLException {
		DataSource myds = MySQLDataSourceFactory.getMySQLDataSource();
		
		Connection con = null;
		
		try {
			con = myds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
		if ( con.isValid(1000) ) {
			System.out.println("Connection is valid!");
		}
		
	}
}
