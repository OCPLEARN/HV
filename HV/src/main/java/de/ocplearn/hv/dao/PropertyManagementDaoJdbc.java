package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.SQLUtils;


@Repository
public class PropertyManagementDaoJdbc implements PropertyManagementDao {
	
	public static final String TABLE_NAME = "propertyManagement";
	public static final String TABLE_NAME_PREFIX = "pm";
	public static final String COLUMNS = SQLUtils.createSQLString(
			TABLE_NAME_PREFIX, 
			Arrays.asList("id", "timeStmpAdd", "timeStmpEdit", "primaryLoginUserId", "paymentType", "primaryContactId", "companyContactId"), 
			new ArrayList<String>() 
			);
	
	private DataSource dataSource;
		
	public Logger logger = LoggerBuilder.getInstance().build( PropertyManagementDaoJdbc.class );
	
	@Autowired // DB einbinden über Autowire mit Qualifier
	public PropertyManagementDaoJdbc( @Qualifier ("datasource1") DataSource dataSource ) {
		this.dataSource = dataSource;
	}

	@Override
	public boolean save( PropertyManagement propertyManagement ) {
		if (propertyManagement.getId() == 0) {
			return this.insert(propertyManagement);
		}else {
			return this.update(propertyManagement);
		}
	}

	@Override
	public boolean delete( PropertyManagement propertyManagement ) {

		String sql ="DELETE FROM propertymanagement WHERE id = ?;";
		
		try( Connection connection = dataSource.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, propertyManagement.getId());
			if (stmt.executeUpdate() == 1) return true;
			
		} catch (SQLException e) {
			e.printStackTrace(); 
	        logger.log(Level.WARNING, e.getMessage());
	        throw new DataAccessException("Unable to get Data from DB."); 
		}		
		return false;
	}

	@Override
	public Optional<PropertyManagement> findById( int  id ) {
		String sql = "SELECT " 	+ PropertyManagementDaoJdbc.COLUMNS 
								+ " FROM " + PropertyManagementDaoJdbc.TABLE_NAME 
								+ " AS " + PropertyManagementDaoJdbc.TABLE_NAME_PREFIX 
								+ " WHERE " + PropertyManagementDaoJdbc.TABLE_NAME_PREFIX + ".id = ?;";
				 
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql); ){
			stmt.setInt(1, id );
			ResultSet resultSet = stmt.executeQuery();
			return resultSet.next() ? Optional.of(this.mapRowToPropertyManagement(resultSet)) : Optional.empty();
		} 
		catch (SQLException e) {
				e.printStackTrace();
				logger.log(Level.WARNING, e.getMessage());
				throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());		
	    }
	}

	public PropertyManagement mapRowToPropertyManagement(ResultSet resultSet, PropertyManagement propertyManagement) throws SQLException {
		//id, primaryLoginUserId, paymentType, primaryContactId, companyContactId
		
		propertyManagement.setId(resultSet.getInt(PropertyManagementDaoJdbc.TABLE_NAME_PREFIX + ".id"));
		
		LoginUser primaryLoginUser = new LoginUser();
		primaryLoginUser.setId(resultSet.getInt(PropertyManagementDaoJdbc.TABLE_NAME_PREFIX + ".primaryLoginUserId"));
		
		propertyManagement.setPrimaryLoginUser(primaryLoginUser);
		propertyManagement.setPaymentType(PaymentType.valueOf(resultSet.getString(PropertyManagementDaoJdbc.TABLE_NAME_PREFIX + ".paymentType")));
		Contact primaryContact = new Contact();
		primaryContact.setId(resultSet.getInt(PropertyManagementDaoJdbc.TABLE_NAME_PREFIX + ".primaryContactId"));
		propertyManagement.setPrimaryContact(primaryContact);
		Contact companyContact = new Contact();
		companyContact.setId(resultSet.getInt(PropertyManagementDaoJdbc.TABLE_NAME_PREFIX + ".companyContactId"));
		propertyManagement.setCompanyContact(companyContact);
		return propertyManagement;
	}

	public PropertyManagement mapRowToPropertyManagement(ResultSet resultSet) throws SQLException {
		return mapRowToPropertyManagement (resultSet, new PropertyManagement());
	}

	@Override
	public Optional<PropertyManagement> findByPrimaryContact( Contact primaryContact ) {
		
		// SELECT pm.id  AS "pm.Id" FROM propertyManagement AS pm WHERE pm.primaryContactId = 74;

		String sql ="SELECT " + PropertyManagementDaoJdbc.COLUMNS 
				   +" FROM"   + PropertyManagementDaoJdbc.TABLE_NAME 
				   +" AS "    + PropertyManagementDaoJdbc.TABLE_NAME_PREFIX 
				   +" WHERE " + PropertyManagementDaoJdbc.TABLE_NAME_PREFIX + ".primaryContactId =  ?;";
		
		try( 
		// 1. Connection to DB		
			 Connection connection = dataSource.getConnection();
		// 2. SQL Statement		
			 PreparedStatement stmt = connection.prepareStatement(sql);
			){
			stmt.setInt( 1, primaryContact.getId() );
		// 3. Return ResultSet	
			ResultSet resultSet = stmt.executeQuery();
		// 4. do something with ResultSet				
		return resultSet.next() ? Optional.of( this.mapRowToPropertyManagement(resultSet) ) : Optional.empty();				
					
		}catch( SQLException e ) {
	    	e.printStackTrace(); 
	        logger.log( Level.WARNING, e.getMessage() );
	        throw new DataAccessException( "Unable to get Data from DB." );
		}
	}
		
	@Override
	public Optional<PropertyManagement> findByPrimaryLoginUserId(int id) {
		String sql = "SELECT * FROM propertymanagemt WHERE primaryLoginUserId = ?;";
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql); ){
			stmt.setInt(1,  id);
			ResultSet resultSet = stmt.executeQuery();
			resultSet.next();
			return Optional.of(this.mapRowToPropertyManagement(resultSet));
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());		
	    }		
	}	
	
	private boolean update(PropertyManagement propertyManagement) {
		String sql = "UPDATE propertymanagement SET primaryLoginUserId=?, paymentType=?, primaryContactId=?, companyContactId=? WHERE id = ?";
		
		try ( Connection connection = this.dataSource.getConnection(); 
			  PreparedStatement stmt = connection.prepareStatement(sql); ){
			
			stmt.setInt( 1, propertyManagement.getPrimaryLoginUser().getId() );
			stmt.setString( 2, propertyManagement.getPaymentType().toString() );
			stmt.setInt( 3, propertyManagement.getPrimaryContact().getId() );
			stmt.setInt( 4, propertyManagement.getCompanyContact().getId() );
			stmt.setInt(5, propertyManagement.getId());

			if (stmt.executeUpdate() != 1) {
				return false;
			}else {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());		
        }
		
	}
	
	private boolean insert (PropertyManagement propertyManagement ) {
		
		String sql = "INSERT INTO propertymanagement (id, primaryLoginUserId, paymentType, primaryContactId, companyContactId) VALUES (null, ?, ?, ?, ?)";
		
		try ( Connection connection = this.dataSource.getConnection(); 
			  PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); ){
						
			stmt.setInt( 1, propertyManagement.getPrimaryLoginUser().getId() );
			stmt.setString( 2, propertyManagement.getPaymentType().toString() );
			stmt.setInt( 3, propertyManagement.getPrimaryContact().getId() );
			stmt.setInt( 4, propertyManagement.getCompanyContact().getId() );

			if (stmt.executeUpdate() != 1) return false;
			else {
				ResultSet resultSet = stmt.getGeneratedKeys();	
				resultSet.next();
				propertyManagement.setId(resultSet.getInt(1)); 
										
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());		
        }
				
	}

	@Override
	public boolean addLoginUserToPropertyManagement(LoginUser loginUser, PropertyManagement propertyManagement) {
		String sql = "INSERT INTO propertymanagementloginuserlink (id, propertyManagementId, loginUserId) VALUES (null, ?, ?)";
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql); ){	
	
			stmt.setInt( 1, propertyManagement.getId() );
			stmt.setInt( 2, loginUser.getId() );
			if (stmt.executeUpdate() == 0) return false;
			else {
				return true;
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());		
	    }
	}

	@Override
	public boolean removeLoginUserFromPropertyManagement(LoginUser loginUser,
		PropertyManagement propertyManagement) {
		
		//System.out.println("removeLoginUserFromPropertyManagement: "+loginUser.getId() + " " + propertyManagement.getId());
		
		String sql ="DELETE FROM propertymanagementloginuserlink WHERE loginUserId = ? AND propertyManagementId=?;";
		
		try ( Connection connection = dataSource.getConnection();
			  PreparedStatement stmt = connection.prepareStatement(sql)	) {
			
			stmt.setInt(1, loginUser.getId());
			stmt.setInt(2, propertyManagement.getId());
			return (stmt.executeUpdate() == 1 ) ? true : false;
			
		} catch (SQLException e) {
			 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
		}	
	}
	
	@Override
	public List<Integer> getLoginUsersByPropertyManagement(PropertyManagement propertyManagement) {
		
		List <Integer> loginUserIds = new ArrayList<Integer>();
		
		//System.out.println("\t getLoginUsersByPropertyManagement" + propertyManagement.getId());
		
		String sql = "select * from propertymanagementloginuserlink where propertyManagementId = ?;";
		
		try ( Connection connection = dataSource.getConnection();
				  PreparedStatement stmt = connection.prepareStatement(sql)	) {
			stmt.setInt(1, propertyManagement.getId());
			ResultSet resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				int int1 = resultSet.getInt("loginUserId");
				loginUserIds.add(int1);
				System.out.println("\t getLoginUsersByPropertyManagement" + int1);
			}
			
			return loginUserIds;
			
		} catch (SQLException e) {
			 e.printStackTrace(); 
            logger.log(Level.WARNING, e.getMessage());
            throw new DataAccessException("Unable to get Data from DB.");
		}	
	}
	
	@Override
	public List<PropertyManagement> findPropertyManagementByComanyName( String companyName ) {
		
		String sql;
		sql = "SELECT "
			+ PropertyManagementDaoJdbc.COLUMNS + " FROM " + PropertyManagementDaoJdbc.TABLE_NAME 
			+ " AS " + PropertyManagementDaoJdbc.TABLE_NAME_PREFIX 
			+ " INNER JOIN " 
			+ ContactDaoJdbc.TABLE_NAME 
			+ " AS " + ContactDaoJdbc.TABLE_NAME_PREFIX
			+ " ON " + PropertyManagementDaoJdbc.TABLE_NAME_PREFIX + ".companyContactId  = " + ContactDaoJdbc.TABLE_NAME_PREFIX + ".id" 
			+ " WHERE " + ContactDaoJdbc.TABLE_NAME_PREFIX + ".companyName LIKE ?;";
		
		List<PropertyManagement> propertyManagements = new ArrayList<PropertyManagement>();
		
		try ( Connection connection = dataSource.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, companyName);
			ResultSet resultSet = stmt.executeQuery(sql);
			
			while (resultSet.next()) {
				propertyManagements.add(this.mapRowToPropertyManagement(resultSet));
			}
			return propertyManagements;

		} catch (SQLException e) {
			 e.printStackTrace(); 
	            logger.log(Level.WARNING, e.getMessage());
	            throw new DataAccessException("Unable to get Data from DB.");
		}
	}



}