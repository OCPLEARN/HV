package de.ocplearn.hv.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * Starts DBPoolCheckerThread when context initialized
 * 
 * https://stackoverflow.com/questions/3468150/using-special-auto-start-servlet-to-initialize-on-startup-and-share-application
 * 
 * 
 * http://fruzenshtein.com/setup-of-dynamic-web-project-using-maven/
 * 
 * */

@WebListener
public class Config {

	// logger
	private static Logger logger = LoggerBuilder.getInstance().build(Config.class);
	// db pool chker thread
	private Thread dbPoolCheckerThread;
	// ref to immo app
//	private static ImmoApplication application;	
	// use DB ConnectionPool
	private static final boolean useDBConnectionPool = true;
	
	/**
	 * Indicates if DBConnectionPool is provided
	 * 
	 * @return true if DBConnectionPool instance exists;
	 * */
	public static boolean useDBConnectionPool() {
		return useDBConnectionPool;
	}
	

}
