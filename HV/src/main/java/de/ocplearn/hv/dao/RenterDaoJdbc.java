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
import de.ocplearn.hv.model.BuildingOwner;
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
	
	private PropertyManagementDao propertyManagementDao;	
	
	public RenterDaoJdbc(@Qualifier("datasource1") DataSource datasource,
			ContactDaoJdbc contactDaoJdbc,
			LoginUserDaoJdbc loginUserDaoJdbc) {
		this.datasource = datasource;
		this.contactDaoJdbc = contactDaoJdbc;
		this.loginUserDaoJdbc = loginUserDaoJdbc;		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Renter> findByIdFull(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
