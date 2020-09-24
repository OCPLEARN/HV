package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;

/**
 * DAO for LoginUser
 * 
 * */
public interface LoginUserDao {

	/**
	 * Store LoginUser
	 * 
	 * @param LoginUser
	 * @return boolean true, if successfull
	 * */
	boolean save( LoginUser loginUser );
	
	/**
	 * Deletes given LoginUser
	 * 
	 * @param LoginUser
	 * @return boolean true, if successful
	 * */
	boolean delete( LoginUser loginUser );

	/**
	 * Deletes by loginUserName
	 * 
	 * @param String loginUserName
	 * @return boolean true, if successful
	 * */
	boolean delete( String loginUserName );	
	
	/**
	 * Search by id
	 * 
	 * @param int id
	 * @return Optional<LoginUser>
	 * */
	Optional<LoginUser> findUserById(int id);
	
	/**
	 * Search by LoginUserName
	 * 
	 * @param String loginUserName
	 * @return Optional<'LoginUser'>
	 * */
	Optional<LoginUser> findUserByLoginUserName(String loginUserName);
	
	/**
	 * Search by role
	 * 
	 * @param Role role
	 * @return List<LoginUser>
	 * */
	List<LoginUser> findAllByRole(Role role);
	
	/**
	 * Checks, if loginUserName is already used
	 * 
	 * @param String loginUserName
	 * @return boolean true if name already in use
	 * */
	boolean userAlreadyExists( String loginUserName );
	
	/**
	 * Checks, if LoginUser exists and if password is correct
	 * 
	 * @param loginUserName
	 * @param password
	 * @return boolean
	 */
	public boolean validateUser( String loginUserName, String password );
}
