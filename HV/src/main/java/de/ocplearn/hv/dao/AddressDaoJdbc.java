/**
 * 
 */
package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.SQLUtils;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * AddressDao for jdbc (MySQL)
 *
 */
@Component("AddressDaoJdbc")
public class AddressDaoJdbc implements AddressDao {

	private static final String TABLE_NAME = "address";
	private static final String TABLE_NAME_PREFIX = "ad";
	private static final String COLUMNS = SQLUtils.createSQLString(TABLE_NAME_PREFIX, Arrays.asList(
		"id", "timeStmpAdd", "timeStmpEdit", "street", "houseNumber",
		"adrLine1", "adrLine2", "city", "zip", "province", "country", "ST_X(coordinate)", "ST_Y(coordinate)"
			));	
	
	// ST_X(coordinate),ST_Y(coordinate)
	
	/* logger */
	private Logger logger = LoggerBuilder.getInstance().build(AddressDaoJdbc.class);
	
	/* A factory for connections to the physical data source that this DataSource object represents. */
	private DataSource datasource;	
	
	/**
	 * Builds the dao for the adress
	 *  */
    public AddressDaoJdbc(@Qualifier("datasource1") DataSource datasource ) {
    	this.datasource = datasource;
    }	
	
	@Override
	public boolean save(Address address) throws DataAccessException {
        if (address.getId() == 0){
            return this.insert(address);
        }else{
            return this.update(address);
        }
	}
	
	private boolean insert(Address address){
		
		String sql = "INSERT INTO address (id,street,houseNumber,adrline1,"
				+ "adrline2,city,zip,province,country,coordinate) VALUE ( null,?,?,?,?,?,?,?,?,POINT(?,?) );";
		//INSERT INTO address (id,street,houseNumber,adrline1,adrline2,city,zip,province,country,coordinate)  
		//VALUE ( null, 'Platz der Republik','1','adrline1','adrline2','Berlin','11011','Berlin','DE',POINT(52.518672, 13.376118) );
		
        try(Connection con = this.datasource.getConnection();  
        	PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS );) {	
        	
        	//String coordinate = "POINT("+address.getLatitude()+", "+address.getLongitude()+"";
        	
            stmt.setString(1, address.getStreet()  );
            stmt.setString(2, address.getHouseNumber()  );
            stmt.setString(3, address.getApartment() );
            stmt.setString(4, "" );
            stmt.setString(5, address.getCity() );
            stmt.setString(6, address.getZipCode() );
            stmt.setString(7, address.getProvince() );
            stmt.setString(8, address.getCountry() );
            stmt.setDouble(9, address.getLatitude() );
            stmt.setDouble(10, address.getLongitude() );
            //stmt.setString(9, coordinate );
            
            //System.out.println(sql);
            
            // get generated key
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected != 1){
                return false;
            }            
            
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            address.setId(rs.getInt(1));        	
		
        } catch (SQLException e) {
       	 e.printStackTrace(); 
         logger.log(Level.WARNING, e.getMessage());
         throw new DataAccessException("Unable to get Data from DB.");
    }   
        
        return true;
	}
	
	private boolean update(Address address){
		
		String sql = "UPDATE address SET street = ?, houseNumber = ?, adrline1 = ?,"
				+ " adrline2 = ?, city = ?, zip = ?, province = ?, country = ?, coordinate = POINT(?,?) WHERE id = ?;";
		
		  try(Connection con = this.datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement(sql);) {

			  //String coordinate = "POINT("+address.getLatitude()+", "+address.getLongitude()+"";
			  
	            stmt.setString(1, address.getStreet()  );
	            stmt.setString(2, address.getHouseNumber()  );
	            stmt.setString(3, address.getApartment() );
	            stmt.setString(4, "" );
	            stmt.setString(5, address.getCity() );
	            stmt.setString(6, address.getZipCode() );
	            stmt.setString(7, address.getProvince() );
	            stmt.setString(8, address.getCountry() );
	            stmt.setDouble(9, address.getLatitude());
	            stmt.setDouble(10, address.getLongitude());
	            stmt.setInt(11,address.getId());
	                
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected != 1){
                    return false;
                	//throw new SQLException("No row was updated for address with id = " + address.getId());
                }

            } catch (SQLException e) {
            	 e.printStackTrace(); 
                 logger.log(Level.WARNING, e.getMessage());
                 throw new DataAccessException("Unable to get Data from DB.");
            }   	
		
		return true;
	}
	

	@Override
	public boolean delete(Address address) {
		try(	
				Connection con =  this.datasource.getConnection();
	        	PreparedStatement stmt = con.prepareStatement( "DELETE FROM address WHERE id = ?;" );
			)
		{
			stmt.setInt(1, address.getId()  );
	        return (stmt.executeUpdate()==1)?true:false;	                
	    } catch (SQLException e) {
	    	e.printStackTrace(); 
	        logger.log(Level.WARNING, e.getMessage());
	        throw new DataAccessException("Unable to get Data from DB.");            
	    }
	}

	@Override
	public Optional<Address> findById(int id) {

		try(
				Connection con = this.datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement( "SELECT "+AddressDaoJdbc.COLUMNS+" FROM address WHERE id = ?;" );
				)
		{
			stmt.setInt(1, id );
			ResultSet resultSet = stmt.executeQuery();
			return resultSet.next() ? Optional.of(this.mapRowToAddress(resultSet)) : Optional.empty();
		}
		catch (SQLException e) {
	    	e.printStackTrace(); 
	        logger.log(Level.WARNING, e.getMessage());
	        throw new DataAccessException("Unable to get Data from DB.");            
	    }		
	}



	
	private Address mapRowToAddress (ResultSet resultSet) throws SQLException {
		// id,street,houseNumber,adrline1,adrline2,city,zip,province,country,coordinate
		Address address = new Address();
		address.setId( resultSet.getInt( AddressDaoJdbc.TABLE_NAME_PREFIX + ".id") );
		address.setHouseNumber( resultSet.getString(AddressDaoJdbc.TABLE_NAME_PREFIX + ".houseNumber") );
		address.setApartment( resultSet.getString(AddressDaoJdbc.TABLE_NAME_PREFIX + ".adrline1") );
		//address.set???( resultSet.getString("adrline2") );
		address.setCity( resultSet.getString(AddressDaoJdbc.TABLE_NAME_PREFIX + ".city") );
		address.setZipCode( resultSet.getString(AddressDaoJdbc.TABLE_NAME_PREFIX + ".zip") );
		address.setProvince(resultSet.getString(AddressDaoJdbc.TABLE_NAME_PREFIX + ".province") );
		address.setCountry( resultSet.getString(AddressDaoJdbc.TABLE_NAME_PREFIX + ".country") );
		address.setLatitude( resultSet.getDouble(AddressDaoJdbc.TABLE_NAME_PREFIX + ".ST_X(coordinate)") ); // ST_X(coordinate)
		address.setLongitude( resultSet.getDouble(AddressDaoJdbc.TABLE_NAME_PREFIX + ".ST_Y(coordinate)") ); // ST_Y(coordinate)
		return address;
	}
	
	
}
