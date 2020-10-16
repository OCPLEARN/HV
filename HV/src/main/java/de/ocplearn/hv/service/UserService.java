package de.ocplearn.hv.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.model.Tenant;
/**
 * Create, update, delete, read and validate users
 */

public interface UserService {
	
	/**
	 * Max number of selected rows in DB query, if not other specified.
	 */
	int ROW_COUNT = 10; 

    /**
     * Finds user by login name
     * 
     * @param login name of user
     * @return LoginUser instance or null, if not found
     */
    LoginUserDto findUserByLoginUserName( String loginUserName );

    
    /**
     * Tests if user exists
     * 
     * @param login name of user
     * @return boolean
     */
    boolean loginUserExists( String loginUserName );
    
    /**
     * Finds user by id
     * 
     * @param id
     * @return user instance or null, if not found
     */
    LoginUserDto findUserById( int id );
    
    /**
     * Returns all users assigned the given role
     * 
     * @param role
     * @return List of matched users
     */
    List<LoginUserDto> findAllByRole( Role role );
    
    /**
     * Returns all tenants assigned to this property manager
     * 
     * @param propertyManager instance
     * @return List of Tenant instances
     */
    List<Tenant> findTenantsByPropertyManager( PropertyManagement propertyManager );
    
    /**
     * Returns all Tenant instances in units assigned to this Building instance
     * 
     * @param Building instance
     * @return List of tenants
     */
    List<Tenant> findTenantsByBuilding( Building building );
    
    /**
     * Creates new LoginUser from LoginUserDto with password as String
     * 
     * @param loginUser
     * @param password
     * @return boolean
     */
    boolean createUser( LoginUserDto loginUserDto, String password );
 
    /**
     * Creates new LoginUser from LoginUserDto
     * 
     * @param loginUser
     * @return boolean
     */
    boolean createUser( LoginUserDto loginUserDto ); 
    
    
    /**
     * Deletes a user
     * 
     * @param identifier
     * @return true, if successful
     */
    boolean deleteUser( String loginUserName );
    
    /**
     * Deletes a user
     * 
     * @param LoginUser
     * @return true, if successful
     */
    boolean deleteUser( LoginUserDto loginUserDto );
    
    /**
     * Updates an existing user
     * 
     * @param user to update
     * @return true, if successful
     */
    boolean updateUser( LoginUserDto loginUserDto );
    
    /**
     * Validates user with password
     * 
     * @param login name
     * @param password 
     * @return true, if user was validated
     */
    Optional<LoginUserDto> validateUserPassword( String loginUserName, String password );
    
    /**
     * Returns list of all registered users
     * 
     * @return List of Login User
     */
    List<LoginUserDto> getAllLoginUsers();
    
    
    /**
     * Finds and sorts all LoginUsers by table parameters and returns a list of LoginUserDtos.
     * Starting at row indexStart.
     * 
     * @param indexStart
     * @param rowCount
     * @param orderBy
     * @param orderDirection
     * @return List<'LoginUserDto'>
     */
    List<LoginUserDto> findAllLoginUsers(int indexStart, int rowCount, String orderBy, String orderDirection );  

    /**
     * 
     * @return int number of LoginUser objects in datastore
     * */
    int getLoginUserCount();
    
    
}
