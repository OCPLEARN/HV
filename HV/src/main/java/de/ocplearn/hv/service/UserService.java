package de.ocplearn.hv.service;
import java.util.List;

import org.springframework.stereotype.Service;

import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManager;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.model.Tenant;
/**
 * Create, update, delete, read and validate users
 */

public interface UserService {
	
	

    /**
     * Finds user by login name
     * 
     * @param login name of user
     * @return LoginUser instance or null, if not found
     */
    LoginUser findUserByLoginUserName( String loginUserName );

    /**
     * Finds user by id
     * 
     * @param id
     * @return user instance or null, if not found
     */
    LoginUser findUserById( int id );
    
    /**
     * Returns all users assigned the given role
     * 
     * @param role
     * @return List of matched users
     */
    List<LoginUser> findAllByRole( Role role );
    
    /**
     * Returns all tenants assigned to this property manager
     * 
     * @param propertyManager instance
     * @return List of Tenant instances
     */
    List<Tenant> findTenantsByPropertyManager( PropertyManager propertyManager );
    
    /**
     * Returns all Tenant instances in units assigned to this Building instance
     * 
     * @param Building instance
     * @return List of tenants
     */
    List<Tenant> findTenantsByBuilding( Building building );
    
    /**
     * Creates new LoginUser
     * 
     * @param loginUser
     */
    boolean createUser( LoginUser loginUser, String password );
    
    /**
     * Deletes a user
     * 
     * @param identifier
     * @return true, if successful
     */
    boolean deleteUser( String loginUserName );
    
    /**
     * Updates an existing user
     * 
     * @param user to update
     * @return true, if successful
     */
    boolean updateUser( LoginUser loginUser );
    
    /**
     * Validates user with password
     * 
     * @param login name
     * @param password 
     * @return true, if user was validated
     */
    boolean validateUserPassword( String loginUserName, String password );
    
    /**
     * Returns list of all registered users
     * 
     * @return List of Login User
     */
    List<LoginUser> getAllLoginUsers();
    

}
