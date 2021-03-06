package de.ocplearn.hv.service;

import de.ocplearn.hv.dao.LoginUserDao;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.mapper.LoginUserMapper;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.model.Renter;
import de.ocplearn.hv.model.Role;
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

	
	public LoginUserDao loginUserDao;
	
	public LoginUserMapper loginUserMapper;

	public UserServiceImpl( LoginUserMapper loginUserMapper, @Qualifier("LoginUserDaoJdbc") LoginUserDao loginUserDao ) {
		this.loginUserMapper = loginUserMapper;
		this.loginUserDao = loginUserDao;
	}
	
    @Override
    public LoginUserDto findUserByLoginUserName(String loginUserName) {
    	
    	Optional <LoginUser>  loginUser = loginUserDao.findUserByLoginUserName(loginUserName);
    	
    	if (loginUser.isPresent()) 
    			return loginUserMapper.loginUserToLoginUserDto(loginUser.get());
    	
        return null;
    }
    
    @Override
    public boolean loginUserExists(String loginUserName) {

    	Optional<LoginUser> loginUser = loginUserDao.findUserByLoginUserName(loginUserName);
    	if(loginUser.isPresent()) {
    		return true;
    	} else {
    	return false;
    	}
    }

    @Override
    public LoginUserDto findUserById(int id) {
    	
 	Optional<LoginUser> loginUser = loginUserDao.findUserById(id);
    	
    	if (loginUser.isPresent()) 
    			return loginUserMapper.loginUserToLoginUserDto(loginUser.get());
    	
        return null;
    }

    @Override
    public List<LoginUserDto> findAllByRole(Role role) {
    	
    	return loginUserDao.findAllByRole(role)
    			.stream()
    			.map( loginUser -> loginUserMapper.loginUserToLoginUserDto(loginUser) )
    			.collect(Collectors.toList());
    }

    @Override
    public List<Renter> findRenterByPropertyManager(PropertyManagement propertyManager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Renter> findRenterByBuilding(Building building) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public boolean createUser(LoginUserDto loginUserDto, String password) {
     
    	LoginUser loginUser = loginUserMapper.loginUserDtoToLoginUser(loginUserDto);
    	
        if ( loginUserDao.userAlreadyExists(loginUser.getLoginUserName()) ){
            return false;
        }
        
        HashMap<String, byte[]> hm = StaticHelpers.createHash(password, null);
        loginUser.setPasswHash( hm.get("hash") );
        loginUser.setSalt(hm.get("salt") );
        
        if ( loginUserDao.save(loginUser) ) {
        	loginUserDto.setId(loginUser.getId());
        	return true;	
        }else {
        	return false;
        }
         
    }    
    @Override
    public boolean createUser(LoginUserDto loginUserDto) {
    	LoginUser loginUser = loginUserMapper.loginUserDtoToLoginUser(loginUserDto);
    	//System.out.println("US -- createUser() " + loginUser.getLoginUserName() );	
        if ( loginUserDao.userAlreadyExists(loginUser.getLoginUserName()) ){
            return false;
        }
        
        if ( loginUserDao.save(loginUser) ) {
        	loginUserDto.setId(loginUser.getId());
        	return true;	
        }
    	return false;
    }
    
    
    @Override
    public boolean deleteUser(String loginUserName) {
    	
    	return loginUserDao.delete(loginUserName);
    }
    
    @Override
    public boolean deleteUser(LoginUserDto loginUserDto) {
    	
    	return loginUserDao.delete(loginUserMapper.loginUserDtoToLoginUser(loginUserDto));
    }

    
    @Override
    public boolean updateUser(LoginUserDto loginUserDto) {
        return loginUserDao.save(loginUserMapper.loginUserDtoToLoginUser(loginUserDto));
    }

    @Override
    public Optional<LoginUserDto> validateUserPassword(String loginUserName, String password) {
    	
        if (loginUserDao.validateUser(loginUserName, password)) {
        	return Optional.of(loginUserMapper.loginUserToLoginUserDto(loginUserDao.findUserByLoginUserName(loginUserName).get()));       
        }else {
        	return Optional.empty();
        }
    }

    @Override
    public List<LoginUserDto> getAllLoginUsers() {   
    		    	  
    		return loginUserDao.findAllLoginUsers(0, UserService.ROW_COUNT, "loginUserName", "ASC").stream()
    	   			.map(loginUser -> {return loginUserMapper.loginUserToLoginUserDto(loginUser);})
    	   			.collect(Collectors.toList()); 
    }
    
    @Override
    public List<LoginUserDto> findAllLoginUsers(int offset, int rowCount, String orderBy, String orderDirection ) {   
    		    	  
    		return loginUserDao.findAllLoginUsers(offset, rowCount, orderBy, orderDirection).stream()
    	   			.map(loginUser -> {return loginUserMapper.loginUserToLoginUserDto(loginUser);})
    	   			.collect(Collectors.toList()); 
    }

	@Override
	public int getLoginUserCount() {
		return loginUserDao.getLoginUserCount();
	} 
    
   
   
    
    
    
}
