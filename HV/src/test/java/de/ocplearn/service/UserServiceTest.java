/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ocplearn.service;


import java.util.Locale;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;

import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.service.UserServiceImpl;
import de.ocplearn.hv.util.Config;
import de.ocplearn.hv.util.MySQLDataSourceFactory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 *
 * @author Andreas Mann (lokal)
 */
public class UserServiceTest {
    
    private UserService us = new UserServiceImpl();  
    
    private Supplier<LoginUser> loginUserSuppl = LoginUser::new;
    
    private static DataSource ds;
    
        
    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {

    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    	
    	
    	System.out.println("useDBConnectionPool() = " + Config.useDBConnectionPool());
    	Config.useDBConnectionPool();
    	
    	MySQLDataSourceFactory.initDS();
    	ds = MySQLDataSourceFactory.getMySQLDataSource();
    }    
    
    /**
     * Test of values method, of class Role.
     */
    @org.junit.jupiter.api.Test
    public void testCreateUser() {
        System.out.println("testing UserService insert ...");
        
        us = new UserServiceImpl();
        
        LoginUser adminUser = getLoginUser();
        
        boolean result = us.createUser(adminUser, "Pa$$w0rd");
        Assertions.assertEquals(true, result);
        
        boolean idIsSet = true;
        boolean value = adminUser.getId() > 0 ;
        Assertions.assertEquals(idIsSet, value);
        
    }    

    /**
     * Test of values method, of class Role.
     */
    @org.junit.jupiter.api.Test
    public void testUpdateUser() {
        System.out.println("testing UserService update ...");
        
        us = new UserServiceImpl();
        
        // Given
        //LoginUser u = us.findUserById(1);
        LoginUser u = us.findUserByLoginUserName("admin");
        if ( u != null ) {
            // When
        	Role role = u.getRole();
            u.setRole(Role.OWNER);
            u.save();
            u.setRole(role);
            u.save();
            // Then
            Assertions.assertEquals(role, u.getRole());        	
        }else {
        	System.out.println("!!! check findUserById(1) !!! table empty?");
        	Assertions.assertEquals(true, false);
        }

        
    }

    private LoginUser getLoginUser() {
    	
    	us = new UserServiceImpl();
        
        String loginUserName = "admin" + System.currentTimeMillis();
        LoginUser adminUser = loginUserSuppl.get();
        
        adminUser.setLoginUserName(loginUserName);
        adminUser.setRole(Role.ADMIN);
        adminUser.setLocale( Locale.GERMANY );
        
        return adminUser; 	
    }
    
}
