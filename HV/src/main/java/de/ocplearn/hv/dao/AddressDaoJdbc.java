/**
 * 
 */
package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * AddressDao for jdbc (MySQL)
 *
 */
@Component("AddressDaoJdbc")
public class AddressDaoJdbc implements AddressDao {

	/* logger */
	private Logger logger = LoggerBuilder.getInstance().build(LoginUserDaoJdbc.class);
	
	/* A factory for connections to the physical data source that this DataSource object represents. */
	private DataSource datasource;	
	
	/**
	 * Builds the dao for the adress
	 *  */
    public AddressDaoJdbc(@Qualifier("datasource1") DataSource datasource ) {
    	this.datasource = datasource;
    }	
	
	@Override
	public Address save(Address address) throws DataAccessException {
        if (address.getId() == 0){
            return this.insert(address);
        }else{
            return this.update(address);
        }
	}
	
	private Address insert(Address address){
		
		String sql = "INSERT INTO address (id,street,houseNumber,adrline1,"
				+ "adrline2,city,zip,province,country,coordinate) VALUE ( null, ?,?,?,?,?,?,?,?,? );";
		//INSERT INTO address (id,street,houseNumber,adrline1,adrline2,city,zip,province,country,coordinate)  
		//VALUE ( null, 'Platz der Republik','1','adrline1','adrline2','Berlin','11011','Berlin','DE',POINT(52.518672, 13.376118) );
		
        try(Connection con = this.datasource.getConnection();  
        	PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS );) {	
        	
            stmt.setString(1, address.getStreet()  );
            stmt.setInt(2, address.getHouseNumber()  );
            stmt.setString(3, address.getApartment() );
            stmt.setString(4, "" );
            stmt.setString(5, address.getCity() );
            stmt.setString(6, address.getZipCode() );
            stmt.setString(7, address.getProvince() );
            stmt.setString(8, address.getCountry() );
            stmt.setString(9, (address.getLatitude() +  ", " + address.getLongitude()) );
            
            // get generated key
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected != 1){
                throw new SQLException("No row insert for address with id = " + address.getId());
            }            
            
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            address.setId(rs.getInt(1));        	
		
        } catch (SQLException e) {
       	 e.printStackTrace(); 
         logger.log(Level.WARNING, e.getMessage());
         throw new DataAccessException("Unable to get Data from DB.");
    }   
        
        return address;
	}
	
	private Address update(Address address){
		
		String sql = "UPDATE address SET street = ?, houseNumber = ?, adrline1 = ?,"
				+ " adrline2 = ?, city = ?, zip = ?, province = ?, country = ?, coordinate = ? WHERE id = ?;";
		
		  try(Connection con = this.datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement(sql);) {

	            stmt.setString(1, address.getStreet()  );
	            stmt.setInt(2, address.getHouseNumber()  );
	            stmt.setString(3, address.getApartment() );
	            stmt.setString(4, "" );
	            stmt.setString(5, address.getCity() );
	            stmt.setString(6, address.getZipCode() );
	            stmt.setString(7, address.getProvince() );
	            stmt.setString(8, address.getCountry() );
	            stmt.setString(9, (address.getLatitude() +  ", " + address.getLongitude()) );
	            stmt.setInt(10,address.getId());
	                
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected != 1){
                    throw new SQLException("No row was updated for address with id = " + address.getId());
                }

            } catch (SQLException e) {
            	 e.printStackTrace(); 
                 logger.log(Level.WARNING, e.getMessage());
                 throw new DataAccessException("Unable to get Data from DB.");
            }   	
		
		return address;
	}
	

	@Override
	public boolean delete(Address address) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Address findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Address> findByContactId(int contactId, TablePageViewData tablePageViewData) {
		// TODO Auto-generated method stub
		return null;
	}

}
