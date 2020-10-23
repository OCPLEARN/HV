/**
 * Tests the LoginUserDao interface
 * 
 * */
package de.ocplearn.hv.test.dao;

import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dao.LoginUserDao;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.mapper.LoginUserMapper;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.test.service.UserServiceTest;
import de.ocplearn.hv.util.StaticHelpers;

/**
 * Tests the LoginUserDao interface
 * */
@SpringBootTest
public class LoginUserDaoTest {

	private static String[] NAMES = { 	"Peter","Lisa","Steve","Tony","Monica",
										"Bruce","Eva","Bill","Rachel","Clark",
										"Frank","Helena","Rudy","Joey","Homer",
										"Bart","Cindy","Rita","Bert","Chandler",
										"Greta", "Lara", "Hilbert", "Martin", "Alan"};
	
	private static Random random = new Random(); 
	
	
//	@Autowired
//	private UserService userService;
	
	@Autowired
	@Qualifier("LoginUserDaoJdbcTemplate")	// LoginUserDaoJdbcTemplate LoginUserDaoJdbc LoginUserDaoInMemory
	private LoginUserDao loginUserDao;	
	
	@Autowired
	public LoginUserMapper loginUserMapper;	
	
	private Supplier<LoginUserDto> loginUserDtoSuppl = LoginUserDto::new;
	
    /*
     * create an admin class loginUser with german locale
     * */
    public LoginUserDto getLoginUser() {
        
        //String loginUserName = "admin" + System.currentTimeMillis();
    	String loginUserName = LoginUserDaoTest.getRandomName();
        LoginUserDto adminUser = loginUserDtoSuppl.get();
        
        adminUser.setLoginUserName(loginUserName);
        adminUser.setRole(Role.ADMIN);
        adminUser.setLocale( Locale.GERMANY );
        
        return adminUser; 	
    }	
	
	/**
	 * @return String a random name see NAMES
	 * */
	public static String getRandomName() {
		StringBuilder name = new StringBuilder();
		name.append( NAMES[ random.nextInt(NAMES.length) ]  );
		name.append( random.nextInt(1_000_000) );
		return name.toString();
	}
	
	/**
	 * @return build me a LoginUser
	 * */
	public LoginUserDto buildLoginUser( String password )  {
		LoginUserDto loginUserDto = this.getLoginUser();
		
		String loginUserName = getRandomName();		
		Optional<LoginUser> opt = loginUserDao.findUserByLoginUserName(loginUserName);
		//LoginUserDto nameToTest = userService.findUserByLoginUserName(name.toString());
		if ( opt.isPresent() ) {
			System.out.println("### LoginUserName already exists!");
			throw new RuntimeException("### random LoginUserName already exists!");
		}	
		
		loginUserDto.setLoginUserName(loginUserName);
		
		//String password = "Pa$$w0rd";
		
        HashMap<String, byte[]> hm = StaticHelpers.createHash(password, null);
        loginUserDto.setPasswHash( hm.get("hash") );
        loginUserDto.setSalt(hm.get("salt") );		
        //System.out.println("### creating user : " + loginUserName);		
		
		return loginUserDto;
	}
	
    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }
	
	@Test
	/**
	 * Test for boolean save( LoginUser loginUser );
	 * */
	public void testSaveAndUpdate() {
		
//		System.out.println( "### userService == null : " + (userService == null) );
//		System.out.println( "### loginUserDao == null : " + (loginUserDao == null) );
//		System.out.println( "### loginUserMapper == null : " + (loginUserMapper == null) );
		
		// (1) insert
		LoginUserDto loginUserDto = this.buildLoginUser( "Pa$$w0rd" );
		
        System.out.println("### creating user : " + loginUserDto.getLoginUserName());
        
        boolean created = loginUserDao.save( loginUserMapper.loginUserDtoToLoginUser(loginUserDto) );
		
        Assertions.assertTrue(created);
        
        // (2) update
        Optional<LoginUser> optUpdated = loginUserDao.findUserByLoginUserName(loginUserDto.getLoginUserName());
        if ( optUpdated.isPresent() ) {
        	// assign returned object from optional
        	loginUserDto = loginUserMapper.loginUserToLoginUserDto(optUpdated.get());
        	
            //Role roleOriginal = loginUserDto.getRole();
            loginUserDto.setRole(Role.OWNER);
            boolean updated = loginUserDao.save( loginUserMapper.loginUserDtoToLoginUser(loginUserDto) );
            Assertions.assertTrue(updated);        	
        }
   
	}

	@Test
	/**
	 * Test for boolean save( LoginUser loginUser );
	 * 
	 * */
	public void testDelete() {
		LoginUserDto loginUserDto = this.buildLoginUser("Pa$$w0rd");
		System.out.println("### build and delete user by reference : " + loginUserDto.getLoginUserName());
		
        boolean updated = loginUserDao.save( loginUserMapper.loginUserDtoToLoginUser(loginUserDto) );
        Assertions.assertTrue(updated);     		
		
        Optional<LoginUser> optUpdated = loginUserDao.findUserByLoginUserName(loginUserDto.getLoginUserName());
        if ( optUpdated.isPresent() ) {
        	// assign returned object from optional
        	loginUserDto = loginUserMapper.loginUserToLoginUserDto(optUpdated.get());
        }        
        
		boolean ok = loginUserDao.delete(loginUserMapper.loginUserDtoToLoginUser(loginUserDto) );
		Assertions.assertTrue(ok);    
	}	
	
	@Test
	/**
	 * Test for boolean save( LoginUser loginUser );
	 * 
	 * */
	public void testDeleteByName() {
		LoginUserDto loginUserDto = this.buildLoginUser("Pa$$w0rd");
		System.out.println("### build and delete user by name : " + loginUserDto.getLoginUserName());

        boolean updated = loginUserDao.save( loginUserMapper.loginUserDtoToLoginUser(loginUserDto) );
        Assertions.assertTrue(updated);  		
		
		boolean ok = loginUserDao.delete( loginUserDto.getLoginUserName() );
		Assertions.assertTrue(ok);    
	}	
	

	@Test
	/**
	 * Test for boolean save( LoginUser loginUser );
	 * 
	 * */
	public void validateUser() {
		String password = "jhks&52?!#;;..";
		LoginUserDto loginUserDto = this.buildLoginUser(password);
		System.out.println("### build and validate user : " + loginUserDto.getLoginUserName());

        boolean updated = loginUserDao.save( loginUserMapper.loginUserDtoToLoginUser(loginUserDto) );
        Assertions.assertTrue(updated); 		
        
        Assertions.assertTrue( loginUserDao.validateUser(loginUserDto.getLoginUserName(), password) );
        
	}	
	
}
