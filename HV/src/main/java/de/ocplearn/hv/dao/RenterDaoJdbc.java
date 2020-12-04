/**
 * 
 */
package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.model.Renter;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.SQLUtils;

/**
 * Jdbc implementation of RenterDaao
 *
 */
@Component("RenterDaoJdbc")
public class RenterDaoJdbc implements RenterDao {

	public static final String TABLE_NAME = "renter";
	public static final String TABLE_NAME_PREFIX = "re";
	public static final String COLUMNS = SQLUtils.createSQLString(
			TABLE_NAME_PREFIX, 
			Arrays.asList("id", "timeStmpAdd", "timeStmpEdit", "loginUserId", "contactId", "propertyManagementId"), 
			new ArrayList<String>()
			);	
	
	/* logger */
	private Logger logger = LoggerBuilder.getInstance().build(RenterDaoJdbc.class);
	
	private DataSource datasource;	
	
	private ContactDaoJdbc contactDaoJdbc;
	
	private LoginUserDaoJdbc loginUserDaoJdbc;
	
	private PropertyManagementDaoJdbc propertyManagementDaoJdbc;	
	
	private PropertyManagementDao propertyManagementDao;	
	
	public RenterDaoJdbc(@Qualifier("datasource1") DataSource datasource,
			ContactDaoJdbc contactDaoJdbc,
			LoginUserDaoJdbc loginUserDaoJdbc,
			PropertyManagementDaoJdbc propertyManagementDaoJdbc
			) {
		this.datasource = datasource;
		this.contactDaoJdbc = contactDaoJdbc;
		this.loginUserDaoJdbc = loginUserDaoJdbc;		
		this.propertyManagementDaoJdbc = propertyManagementDaoJdbc;
	}
	
	@Override
	public boolean save(Renter renter) {
        if (renter.getId() == 0){
            return this.insert(renter);
        }else{
            return this.update(renter);
        }
	}
	
	private boolean insert(Renter renter){
		
		String sql = "INSERT INTO renter (id,contactId,loginUserId, propertyManagementId) "
				+ "VALUES ( null,?,?,?);";
		
        try(Connection con = this.datasource.getConnection();  
        	PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS );) {	
        	
        	//String coordinate = "POINT("+address.getLatitude()+", "+address.getLongitude()+"";
        	
            stmt.setInt(1, renter.getContact().getId() );
            stmt.setInt(2, renter.getLoginUser().getId() );
            stmt.setInt(3, renter.getPropertyManagement().getId() );
            
            //System.out.println(sql);
            
            // get generated key
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected != 1){
                return false;
            }            
            
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            renter.setId(rs.getInt(1));        	
		
        } catch (SQLException e) {
       	 e.printStackTrace(); 
         logger.log(Level.SEVERE, e.getMessage());
         throw new DataAccessException("Unable to insert renter");
    }   
        
        return true;
	}		

	private boolean update(Renter renter){
		
		String sql = "UPDATE renter SET contactId = ?, loginUserId = ?, propertyManagementId = ?"
				+ " WHERE id = ?;";
		
		  try(Connection con = this.datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement(sql);) {

			  //String coordinate = "POINT("+address.getLatitude()+", "+address.getLongitude()+"";
			  
	            stmt.setInt(1, renter.getContact().getId()  );
	            stmt.setInt(2, renter.getLoginUser().getId()  );
	            stmt.setInt(3, renter.getPropertyManagement().getId() );
	            stmt.setInt(4, renter.getId()  );
	            

                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected != 1){
                    return false;
                	//throw new SQLException("No row was updated for address with id = " + address.getId());
                }

            } catch (SQLException e) {
            	 e.printStackTrace(); 
                 logger.log(Level.WARNING, e.getMessage());
                 throw new DataAccessException("Unable to update data in DB.");
            }   	
		
		return true;
	}		
	
	@Override
	public boolean delete(Renter renter) {
		return this.delete(renter.getId());
	}

	@Override
	public boolean delete(int renterId) {
		try(	
				Connection con =  this.datasource.getConnection();
	        	PreparedStatement stmt = con.prepareStatement( "DELETE FROM renter WHERE id = ?;" );
			)
		{
			stmt.setInt(1, renterId  );
	        return (stmt.executeUpdate()==1)?true:false;	                
	    } catch (SQLException e) {
	    	e.printStackTrace(); 
	        logger.log(Level.WARNING, e.getMessage());
	        throw new DataAccessException("Unable to delete data from DB.");            
	    }	
	}

	@Override
	public Optional<Renter> findByIdPartial(int id) {
		try(
				Connection con = this.datasource.getConnection();
				// TODO replace SELECT * by SELECT mapRowTo
				PreparedStatement stmt = con.prepareStatement( "SELECT * FROM renter WHERE id = ?;" );
				)
		{
			stmt.setInt(1, id );
			ResultSet resultSet = stmt.executeQuery();
			return resultSet.next() ? Optional.of(this.mapRowToRenter(resultSet)) : Optional.empty();
		}
		catch (SQLException e) {
	    	e.printStackTrace(); 
	        logger.log(Level.WARNING, e.getMessage());
	        throw new DataAccessException("Unable to get Data from DB.");            
	    }
	}

	@Override
	public Optional<Renter> findByIdFull(int id) {
		// SELECT FROM * FROM renter AS re
		// JOIN loginuser AS lu ON re.loginUserId = lu.id
		// JOIN contact AS co ON re.contactId = contact.id
		// WHERE re.id = ?
		String sql = 
				  "SELECT " + COLUMNS + ", "+ LoginUserDaoJdbc.COLUMNS +", " + ContactDaoJdbc.COLUMNS + " "
				
				+ "FROM " + TABLE_NAME + " AS " + TABLE_NAME_PREFIX + ", "
				
				+ "INNER JOIN "+loginUserDaoJdbc.TABLE_NAME+" AS "+loginUserDaoJdbc.TABLE_NAME_PREFIX+" "
					+ "ON "+RenterDaoJdbc.TABLE_NAME_PREFIX+".loginUserId = "+LoginUserDaoJdbc.TABLE_NAME_PREFIX+".id "
				+ "INNER JOIN "+ContactDaoJdbc.TABLE_NAME+" AS "+ContactDaoJdbc.TABLE_NAME_PREFIX+" "
					+ "ON "+TABLE_NAME_PREFIX+".contactId = "+ContactDaoJdbc.TABLE_NAME_PREFIX+".id "
				+ "WHERE " + TABLE_NAME_PREFIX + ".id = ?; ";
		
		try( Connection con = this.datasource.getConnection();
			PreparedStatement stmt = con.prepareStatement( sql );
			) {
			stmt.setInt(1, id );
			ResultSet resultSet = stmt.executeQuery();
			
			if (resultSet.next()) {
				// get renter
				Renter renter = this.mapRowToRenter(resultSet);
				// get login user
				renter.setLoginUser( this.loginUserDaoJdbc.mapRowToLoginUser(resultSet, renter.getLoginUser()) );
				// get contact
				renter.setContact( this.contactDaoJdbc.mapRowToContact(resultSet, renter.getContact()) );
				// get pm as Optional
				renter.setPropertyManagement( (this.propertyManagementDao.findById( renter.getPropertyManagement().getId() )).get() );
				
				return Optional.of(renter);
			} else {
				return Optional.empty();
			}
				
		} catch (SQLException e) {
			    e.printStackTrace(); 
			    logger.log(Level.WARNING, e.getMessage());
			    throw new DataAccessException("Unable to get Data from DB.");            
		}		
		
	}

	/**
	 * maps a row to a RenterObject
	 * 
	 * @param  resultSet
	 * @return Renter
	 * */
	public Renter mapRowToRenter( ResultSet resultSet ) throws SQLException {
		return this.mapRowToRenter(resultSet, new Renter());
	}	
	
	/**
	 * maps a row to BuildingOwnerObject
	 * @param BuildingOwner instance
	 * @param resultSet
	 * @return BuildingOwner
	 * */
	public Renter mapRowToRenter( ResultSet resultSet, Renter renter ) throws SQLException {
		
		renter.setId( resultSet.getInt( RenterDaoJdbc.TABLE_NAME_PREFIX +  ".id" ) );
		
		Contact contact = new Contact();
		contact.setId( resultSet.getInt( RenterDaoJdbc.TABLE_NAME_PREFIX + ".contactId" ) );
		renter.setContact( contact );

		LoginUser loginUser = new LoginUser();
		loginUser.setId( resultSet.getInt( RenterDaoJdbc.TABLE_NAME_PREFIX + ".loginUserId" ) );
		renter.setLoginUser(loginUser);
		
		PropertyManagement propertyManagement = new PropertyManagement();
		propertyManagement.setId(resultSet.getInt(BuildingOwnerDaoJdbc.TABLE_NAME_PREFIX + ".propertyManagementId"));
		renter.setPropertyManagement(propertyManagement);
		
		return renter;	
	}	
	
}
