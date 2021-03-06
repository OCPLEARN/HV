/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ocplearn.hv.test.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import de.ocplearn.hv.dao.LoginUserDaoInMemory;
import de.ocplearn.hv.dao.LoginUserDaoJdbc;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.mapper.LoginUserMapper;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.service.UserServiceImpl;
//import de.ocplearn.hv.service.UserServiceImpl;
import de.ocplearn.hv.util.Config;
//import de.ocplearn.hv.util.MySQLDataSourceFactory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Tests for UserService
 */
@SpringBootTest
public class UserServiceTest {

	private UserService userService;

	@Autowired
	public UserServiceTest(UserService userService) {
		this.userService = userService;
	}

	private Supplier<LoginUserDto> loginUserDtoSuppl = LoginUserDto::new;

	@AfterAll
	public static void tearDownClass() throws Exception {
	}

	@BeforeEach
	public void setUp() throws Exception {

	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@BeforeAll
	public static void setUpClass() throws Exception {

	}

	/**
	 * Test of values method, of class Role.
	 */
	@Test
	public void testCreateUser() {
		//System.out.println("testing UserService insert ...");

		LoginUserDto adminUser = getLoginUser();

		boolean userCreateOk = userService.createUser(adminUser, "Pa$$w0rd");

		Assertions.assertEquals(true, userCreateOk);

		boolean idIsSet = true;
		boolean value = adminUser.getId() > 0;
		Assertions.assertEquals(idIsSet, value);

	}

	/**
	 * Test of values method, of class Role.
	 */
	@Test
	public void testUpdateUser() {
		//System.out.println("testing UserService update ...");

		// Given
		// LoginUser u = us.findUserById(1);
		LoginUserDto loginUserDto = userService.findUserByLoginUserName("admin");
		if (loginUserDto != null) {
			// When
			Role role = loginUserDto.getRole(); // remember current role
			loginUserDto.setRole(Role.OWNER); // set to owner
			userService.updateUser(loginUserDto);
			// loginUserDto.save();
			loginUserDto.setRole(role); // set to previous role
			userService.updateUser(loginUserDto);
			// Then
			Assertions.assertEquals(role, loginUserDto.getRole()); // both should be same
		} else {
			//System.out.println("!!! check findUserById(1) !!! table empty?");
			Assertions.assertEquals(true, false);
		}

	}

	/*
	 * create an admin class loginUser with german locale
	 */
	private LoginUserDto getLoginUser() {

		String loginUserName = "admin" + System.currentTimeMillis();
		LoginUserDto adminUser = loginUserDtoSuppl.get();

		adminUser.setLoginUserName(loginUserName);
		adminUser.setRole(Role.ADMIN);
		adminUser.setLocale(Locale.GERMANY);

		return adminUser;
	}

	
	@Test
	public void testDelete_loginUserByLoginUserName_booleanTrue() {
		
		// Given
		LoginUserDto loginUser = loginUserDtoSuppl.get();
		loginUser.setLoginUserName("ABCDEFGHÖKYJBFVPHYD BFPÖYH DOOF");
		loginUser.setRole(Role.OWNER);
		loginUser.setLocale(Locale.FRENCH);
		userService.createUser(loginUser, "Pa$$w0rd");
		
		// Then
		Assertions.assertTrue(userService.deleteUser(loginUser.getLoginUserName()));
		}
	
	@Test
	public void testDelete_loginUserByLoginUser_booleanTrue() {
		
		// Given
		LoginUserDto loginUser = loginUserDtoSuppl.get();
		loginUser.setLoginUserName("ABCDEFGHÖKYJBFVPHYD BFPÖYH DOOF");
		loginUser.setRole(Role.OWNER);
		loginUser.setLocale(Locale.FRENCH);
		userService.createUser(loginUser, "Pa$$w0rd");
		
		// Then
		Assertions.assertTrue(userService.deleteUser(loginUser));
		}
	
//	@Test
	public void testfindAllLoginUsers_sortedByName_sortedAscending() {

		// Given

		LoginUserDto loginUser1 = loginUserDtoSuppl.get();
		LoginUserDto loginUser2 = loginUserDtoSuppl.get();
		LoginUserDto loginUser3 = loginUserDtoSuppl.get();

		loginUser1.setLoginUserName("alberto"/* + System.currentTimeMillis()*/);
		loginUser2.setLoginUserName("bert" );
		loginUser3.setLoginUserName("cesaria");

		loginUser1.setRole(Role.ADMIN);
		loginUser1.setLocale(Locale.FRENCH);
		loginUser2.setRole(Role.ADMIN);
		loginUser2.setLocale(Locale.FRENCH);
		loginUser3.setRole(Role.ADMIN);
		loginUser3.setLocale(Locale.FRENCH);

		userService.createUser(loginUser1, "Pa$$w0rd");
		userService.createUser(loginUser2, "Pa$$w0rd");
		userService.createUser(loginUser3, "Pa$$w0rd");

		int id1 = loginUser1.getId();
		int id2 = loginUser1.getId();
		int id3 = loginUser1.getId();

		// When
		
				List<LoginUserDto> loginUserDtoList = userService.findAllLoginUsers(0, 300, "loginUserName", "ASC");
		
		//System.out.print(loginUserDtoList.toString().toLowerCase());
		//System.out.println("XXXXXX");
		
		List<LoginUserDto> loginUserDtoList2 = loginUserDtoList.stream()
				.sorted((T1,T2)->{return T1.getLoginUserName().toLowerCase().compareTo(T2.getLoginUserName().toLowerCase());}).collect(Collectors.toList());
		
		//System.out.println(loginUserDtoList2);
		
		//System.out.print(loginUserDtoList.stream().sorted().collect(Collectors.toList()).toString().toLowerCase());
		
		
		
		
		// Then
		
//		List<LoginUserDto> loginUserDtoList2 = loginUserDtoList.stream()
//				.sorted((T1,T2)->{return T1.getLoginUserName().toLowerCase().compareTo(T2.getLoginUserName().toLowerCase());}).collect(Collectors.toList());
//		
		Assertions.assertTrue(loginUserDtoList.equals(loginUserDtoList2));
		
		//Assertions.assertTrue(loginUserDtoList.stream().sorted().collect(Collectors.toList()).equals(loginUserDtoList));

//		Assertions.assertTrue(loginUserDtoList.get(0).getLoginUserName().contains("Alberta"));
//		Assertions.assertTrue(loginUserDtoList.get(2).getLoginUserName().contains("Cesar"));
	}

}
