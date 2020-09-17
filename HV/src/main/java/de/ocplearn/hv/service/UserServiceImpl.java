package de.ocplearn.hv.service;

import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManager;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.model.Tenant;
import de.ocplearn.hv.util.StaticHelpers;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserServiceImpl implements UserService {

    @Override
    public LoginUser findUserByLoginUserName(String loginUserName) {
        return LoginUser.findUserByLoginUserName(loginUserName);
    }

    @Override
    public LoginUser findUserById(int id) {
        return LoginUser.findUserById(id);
    }

    @Override
    public List<LoginUser> findAllByRole(Role role) {
        return LoginUser.findAllByRole(role);
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
    public boolean createUser(LoginUser loginUser, String password) {
     
        if ( LoginUser.userAlreadyExists(loginUser.getLoginUserName()) ){
            return false;
        }
        
        HashMap<String, byte[]> hm = StaticHelpers.createHash(password, null);
        loginUser.setPasswHash( hm.get("hash") );
        loginUser.setSalt(hm.get("salt") );
        
        return loginUser.save();
    }

    @Override
    public boolean deleteUser(String loginUserName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateUser(LoginUser loginUser) {
        return loginUser.save();
    }

    @Override
    public boolean validateUserPassword(String loginUserName, String password) {
        return LoginUser.validateUser(loginUserName, password);
    }

    @Override
    public List<LoginUser> getAllLoginUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
   
    
   
    
    
    
}
