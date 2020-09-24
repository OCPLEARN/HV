/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ocplearn.hv.test.service;


import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.mapper.LoginUserMapper;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.model2.LoginUserDaoJdbc;
import de.ocplearn.hv.model2.LoginUserMapper2;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.service.UserServiceImpl;
import de.ocplearn.hv.util.Config;
import de.ocplearn.hv.util.MySQLDataSourceFactory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Tests for UserService
 */
public class UserServiceTest {
    
	private LoginUserMapper loginUserMapper;
	
	private static UserService userService;
	
	private static UserServiceImpl userServiceImpl;
	
	private static DataSource dataSource;
	
    private Supplier<LoginUserDto> loginUserDtoSuppl = LoginUserDto::new;
    
    // -------------------------------------------------------------------------
    
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
    	
    	userServiceImpl = new UserServiceImpl();
    	
    	userServiceImpl.loginUserMapper = LoginUserMapper.INSTANCE;
    	
    	userServiceImpl.loginUserMapper2 = LoginUserMapper2.INSTANCE;
    	
    	userService = userServiceImpl;
    	
    	userServiceImpl.loginUserDao = new LoginUserDaoJdbc();
    	
    	//System.out.println("useDBConnectionPool() = " + Config.useDBConnectionPool());
    	
    	//MySQLDataSourceFactory.initDS();
    	//dataSource = MySQLDataSourceFactory.getMySQLDataSource();
    }    
    
    /**
     * Test of values method, of class Role.
     */
    @org.junit.jupiter.api.Test
    public void testCreateUser() {
        System.out.println("testing UserService insert ...");
        
        LoginUserDto adminUser = getLoginUser();
        
        Optional<LoginUserDto> opt = userService.createUser(adminUser, "Pa$$w0rd");
        
        Assertions.assertEquals(true, opt.isPresent());
        
        boolean idIsSet = true;
        boolean value = opt.get().getId() > 0 ;
        Assertions.assertEquals(idIsSet, value);
        
    }    

    /**
     * Test of values method, of class Role.
     */
    @org.junit.jupiter.api.Test
    public void testCreateUser2() {
        System.out.println("testing UserService insert [model2] ...");
        
        LoginUserDto adminUser = getLoginUser();
        
        Optional<LoginUserDto> opt = userService.createUser2(adminUser, "Pa$$w0rd");
        
        Assertions.assertEquals(true, opt.isPresent());
        
        boolean idIsSet = true;
        boolean value = opt.get().getId() > 0 ;
        Assertions.assertEquals(idIsSet, value);
        
    }       
    
    /**
     * Test of values method, of class Role.
     */
    @org.junit.jupiter.api.Test
    public void testUpdateUser() {
        System.out.println("testing UserService update ...");
        
        // Given
        //LoginUser u = us.findUserById(1);
        LoginUserDto loginUserDto = userService.findUserByLoginUserName("admin");
        if ( loginUserDto != null ) {
            // When
        	Role role = loginUserDto.getRole();	// remember current role
        	loginUserDto.setRole(Role.OWNER);	// set to owner
        	userService.updateUser(loginUserDto);
        	//loginUserDto.save();
        	loginUserDto.setRole(role);			// set to previous role
        	userService.updateUser(loginUserDto);
            // Then								
            Assertions.assertEquals(role, loginUserDto.getRole());    // both should be same    	
        }else {
        	System.out.println("!!! check findUserById(1) !!! table empty?");
        	Assertions.assertEquals(true, false);
        }

        
    }

    /*
     * create an admin class loginUser with german locale
     * */
    private LoginUserDto getLoginUser() {
        
        String loginUserName = "admin" + System.currentTimeMillis();
        LoginUserDto adminUser = loginUserDtoSuppl.get();
        
        adminUser.setLoginUserName(loginUserName);
        adminUser.setRole(Role.ADMIN);
        adminUser.setLocale( Locale.GERMANY );
        
        return adminUser; 	
    }
    
}
