package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.sql.DataSourceDefinition;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.util.LoggerBuilder;


@Repository
public class PropertyManagementDaoJdbc implements PropertyManagementDao {
	
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
		
		String sql = "SELECT * FROM propertymanagement where id = ?;";
				
				try ( Connection connection = this.dataSource.getConnection(); 
						  PreparedStatement stmt = connection.prepareStatement(sql); ){
					stmt.setInt(1, id );
					ResultSet resultSet = stmt.executeQuery();
					return resultSet.next() ? Optional.of(this.mapRowToPropertyManagement(resultSet)) : Optional.empty();} 
				catch (SQLException e) {
						e.printStackTrace();
						logger.log(Level.WARNING, e.getMessage());
						throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());		
			        }
					
					
					
					
		// ResultSet = Daten aus der DB in Tabellenform
		// Nummer aus dem ResultSet entspr. Nummer des Eintrags
		
		// TODO Auto-generated method stub
		
	}

	private PropertyManagement mapRowToPropertyManagement(ResultSet resultSet) throws SQLException {
		//id, primaryLoginUserId, paymentType, primaryContactId, companyContactId
		PropertyManagement propertyManagement = new PropertyManagement();
		propertyManagement.setId(resultSet.getInt("id"));
		LoginUser primaryLoginUser = new LoginUser();
		primaryLoginUser.setId(resultSet.getInt("primaryLoginUserId"));
		propertyManagement.setPrimaryLoginUser(primaryLoginUser);
		propertyManagement.setPaymentType(PaymentType.valueOf(resultSet.getString("paymentType")));
		Contact primaryContact = new Contact();
		primaryContact.setId(resultSet.getInt("primaryContactId"));
		propertyManagement.setPrimaryContact(primaryContact);
		Contact companyContact = new Contact();
		companyContact.setId(resultSet.getInt("companyContactId"));
		propertyManagement.setCompanyContact(companyContact);
		return propertyManagement;
	}



	@Override
	public Optional<PropertyManagement> findByPrimaryContact( PropertyManagement propertyManagement ) {
		// TODO Auto-generated method stub
		return null;
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

			if (stmt.executeUpdate() == 0) {
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
			
			System.out.println("=============");
			System.out.println(propertyManagement.getPrimaryLoginUser().getId());
			
			stmt.setInt( 1, propertyManagement.getPrimaryLoginUser().getId() );
			stmt.setString( 2, propertyManagement.getPaymentType().toString() );
			stmt.setInt( 3, propertyManagement.getPrimaryContact().getId() );
			stmt.setInt( 4, propertyManagement.getCompanyContact().getId() );

			if (stmt.executeUpdate() == 0) return false;
			
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
				  PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); ){	
	
			stmt.setInt( 1, propertyManagement.getId() );
			stmt.setInt( 2, loginUser.getId() );
			if (stmt.executeUpdate() == 0) return false;
			
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
	public boolean removeLoginUserFromPropertyManagement(LoginUser loginUser,
			PropertyManagement propertyManagement) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
