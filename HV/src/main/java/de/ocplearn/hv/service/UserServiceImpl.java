package de.ocplearn.hv.service;

import de.ocplearn.hv.dao.LoginUserDao;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.mapper.LoginUserMapper;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManager;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.model.Tenant;
import de.ocplearn.hv.util.StaticHelpers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("LoginUserDaoJdbc")
	public LoginUserDao loginUserDao;
	
	@Autowired
	public LoginUserMapper loginUserMapper;

	
    @Override
    public LoginUserDto findUserByLoginUserName(String loginUserName) {
    	
    	Optional<LoginUser> loginUser = loginUserDao.findUserByLoginUserName(loginUserName);
    	if (loginUser.isPresent()) 
    			return loginUserMapper.loginUserToLoginUserDto(loginUser.get());
    	
        return loginUserMapper.loginUserToLoginUserDto(  )  ;
    }

    @Override
    public LoginUserDto findUserById(int id) {
        return loginUserMapper.loginUserToLoginUserDto( LoginUser.findUserById(id) ) ;
    }

    @Override
    public List<LoginUserDto> findAllByRole(Role role) {
        //return LoginUser.findAllByRole(role);
    	return
    	LoginUser.findAllByRole(role)
    		.stream()
    		.map( loginUser -> loginUserMapper.loginUserToLoginUserDto(loginUser) )
    		.collect(Collectors.toList());
    }

    @Override
    public List<Tenant> findTenantsByPropertyManager(PropertyManager propertyManager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tenant> findTenantsByBuilding(Building building) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<LoginUserDto> createUser(LoginUserDto loginUserDto, String password) {
     
    	LoginUser loginUser = loginUserMapper.loginUserDtoToLoginUser(loginUserDto);
    	
        if ( LoginUser.userAlreadyExists(loginUser.getLoginUserName()) ){
            return Optional.empty();
        }
        
        HashMap<String, byte[]> hm = StaticHelpers.createHash(password, null);
        loginUser.setPasswHash( hm.get("hash") );
        loginUser.setSalt(hm.get("salt") );
        if ( loginUser.save() ) {
        	return Optional.of(loginUserMapper.loginUserToLoginUserDto(loginUser) );	
        }else {
        	return Optional.empty();
        }
         
    }

    @Override
    public Optional<LoginUserDto> createUser2(LoginUserDto loginUserDto, String password) {
     
    	de.ocplearn.hv.model2.LoginUser loginUser = loginUserMapper2.loginUserDtoToLoginUser(loginUserDto);
    	
    	// (1) 
        if ( loginUserDao.userAlreadyExists(loginUser.getLoginUserName()) ){
            return Optional.empty();
        }
        
        HashMap<String, byte[]> hm = StaticHelpers.createHash(password, null);
        loginUser.setPasswHash( hm.get("hash") );
        loginUser.setSalt(hm.get("salt") );
        // (2)
        if ( loginUserDao.save(loginUser) ) {
        	return Optional.of(loginUserMapper2.loginUserToLoginUserDto(loginUser) );	
        }else {
        	return Optional.empty();
        }
         
    }    
    
    @Override
    public boolean deleteUser(String loginUserName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateUser(LoginUserDto loginUserDto) {
        return loginUserMapper.loginUserDtoToLoginUser(loginUserDto).save();
    }

    @Override
    public Optional<LoginUserDto> validateUserPassword(String loginUserName, String password) {
        if (LoginUser.validateUser(loginUserName, password)) {
        	LoginUser u = LoginUser.findUserByLoginUserName(loginUserName);
        	if ( u == null ) { System.out.println("u is null"); }
        	
        	LoginUserDto dto = loginUserMapper.loginUserToLoginUserDto( u );
        	if ( dto == null ) { System.out.println("dto is null"); }
        	
        	return Optional.of( dto );
        }else {
        	return Optional.empty();
        }
    }

    @Override
    public List<LoginUserDto> getAllLoginUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
   
    
   
    
    
    
}
