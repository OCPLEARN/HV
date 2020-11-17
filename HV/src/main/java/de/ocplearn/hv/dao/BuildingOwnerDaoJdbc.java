package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.SQLUtils;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * BuildingOwnerDao for Jdbc
 */
@Component("BuildingOwnerDaoJdbc")
public class BuildingOwnerDaoJdbc implements BuildingOwnerDao {

	public static final String TABLE_NAME = "buildingowner";
	public static final String TABLE_NAME_PREFIX = "bo";
	public static final String COLUMNS = SQLUtils.createSQLString(
			TABLE_NAME_PREFIX, 
			Arrays.asList("id", "timeStmpAdd", "timeStmpEdit", "contactId", "loginUserId"), 
			new ArrayList<String>()
			);

	/* logger */
	private Logger logger = LoggerBuilder.getInstance().build(BuildingOwnerDaoJdbc.class);
	
	/* A factory for connections to the physical data source that this DataSource object represents. */
	private DataSource datasource;		
	
	private ContactDaoJdbc contactDaoJdbc;
	
	private LoginUserDaoJdbc loginUserDaoJdbc;
	
	public BuildingOwnerDaoJdbc( @Qualifier("datasource1") DataSource datasource,
			ContactDaoJdbc contactDaoJdbc,
			LoginUserDaoJdbc loginUserDaoJdbc) {
		this.datasource = datasource;
		this.contactDaoJdbc = contactDaoJdbc;
		this.loginUserDaoJdbc = loginUserDaoJdbc;
	}
	
	@Override
	public boolean save(BuildingOwner buildingOwner) {
        if (buildingOwner.getId() == 0){
            return this.insert(buildingOwner);
        }else{
            return this.update(buildingOwner);
        }
	}

	private boolean insert(BuildingOwner buildingOwner){
		
		String sql = "INSERT INTO buildingowner (id,contactId,loginUserId) "
				+ "VALUE ( null,?,?);";
		
        try(Connection con = this.datasource.getConnection();  
        	PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS );) {	
        	
        	//String coordinate = "POINT("+address.getLatitude()+", "+address.getLongitude()+"";
        	
            stmt.setInt(1, buildingOwner.getContact().getId() );
            stmt.setInt(1, buildingOwner.getLoginUser().getId() );
            
            //System.out.println(sql);
            
            // get generated key
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected != 1){
                return false;
            }            
            
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            buildingOwner.setId(rs.getInt(1));        	
		
        } catch (SQLException e) {
       	 e.printStackTrace(); 
         logger.log(Level.WARNING, e.getMessage());
         throw new DataAccessException("Unable to insert BuildingOwner");
    }   
        
        return true;
	}	
	
	private boolean update(BuildingOwner buildingOwner){
		
		String sql = "UPDATE buildingowner SET contactId = ?, loginUserId = ?"
				+ " WHERE id = ?;";
		
		  try(Connection con = this.datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement(sql);) {

			  //String coordinate = "POINT("+address.getLatitude()+", "+address.getLongitude()+"";
			  
	            stmt.setInt(1, buildingOwner.getContact().getId()  );
	            stmt.setInt(1, buildingOwner.getLoginUser().getId()  );
	            stmt.setInt(1, buildingOwner.getId()  );

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
	public boolean delete(BuildingOwner buildingOwner) {
		try(	
				Connection con =  this.datasource.getConnection();
	        	PreparedStatement stmt = con.prepareStatement( "DELETE FROM buildingowner WHERE id = ?;" );
			)
		{
			stmt.setInt(1, buildingOwner.getId()  );
	        return (stmt.executeUpdate()==1)?true:false;	                
	    } catch (SQLException e) {
	    	e.printStackTrace(); 
	        logger.log(Level.WARNING, e.getMessage());
	        throw new DataAccessException("Unable to delete data from DB.");            
	    }
	}

	@Override
	public Optional<BuildingOwner> findByIdPartial(int id) {
		try(
				Connection con = this.datasource.getConnection();
				// TODO replace SELECT * by SELECT mapRowTo
				PreparedStatement stmt = con.prepareStatement( "SELECT * FROM buildingowner WHERE id = ?;" );
				)
		{
			stmt.setInt(1, id );
			ResultSet resultSet = stmt.executeQuery();
			return resultSet.next() ? Optional.of(this.mapRowToBuildingOwner(resultSet)) : Optional.empty();
		}
		catch (SQLException e) {
	    	e.printStackTrace(); 
	        logger.log(Level.WARNING, e.getMessage());
	        throw new DataAccessException("Unable to get Data from DB.");            
	    }
	}

	@Override
	public Optional<BuildingOwner> findByIdFull(int id) {
		
		BuildingOwner buildingOwner = null;
		//TODO SELECT contact and loginUser
		//String sql = "SELECT * FROM buildingowner WHERE id = ?;";
		String sql = "SELECT " + COLUMNS + ", " + ContactDaoJdbc.COLUMNS + "," + LoginUserDaoJdbc.COLUMNS + "  FROM buildingOwner AS " + TABLE_NAME_PREFIX + " " 
				+ "JOIN contact " + ContactDaoJdbc.TABLE_NAME_PREFIX + " ON bo.contactId = co.id "
				+ "JOIN loginuser " + LoginUserDaoJdbc.TABLE_NAME_PREFIX + " ON bo.loginUserId = lu.id "
				+ "WHERE bo.id = ?;";
		
		System.out.println("sql = " + sql);
		
		try(
				Connection con = this.datasource.getConnection();
				PreparedStatement stmt = con.prepareStatement( sql );
				)
		{
			stmt.setInt(1, id );
			ResultSet resultSet = stmt.executeQuery();
			//return resultSet.next() ? Optional.of(this.mapRowToBuildingOwner(resultSet)) : Optional.empty();
			if (! resultSet.next()) return Optional.empty();
			buildingOwner = this.mapRowToBuildingOwner(resultSet);
			buildingOwner.setContact( this.contactDaoJdbc.mapRowToContact(resultSet,buildingOwner.getContact() ) );
			buildingOwner.setLoginUser( this.loginUserDaoJdbc.mapRowToLoginUser(resultSet, buildingOwner.getLoginUser()  ) );
		}
		catch (SQLException e) {
	    	e.printStackTrace(); 
	        logger.log(Level.WARNING, e.getMessage());
	        throw new DataAccessException("Unable to get Data from DB.");            
	    }
		
		return Optional.of(buildingOwner);
	}	
	
	@Override
	public List<Integer> findAllBuildingIdsByOwnerId(int id, TablePageViewData tablePageViewData) {

		List<Integer> list = new ArrayList<Integer>();
		
		String sql = "SELECT un.buildingId FROM unitownerlink AS uol"
		+ " JOIN unit AS un" 
		+ " ON uol.unitId = un.id"
		+ " WHERE un.unitType = 'BUILDING_UNIT' AND uol.buildingOwnerId = ?"
		+ " ORDER BY ?"
		+ " LIMIT ?, ? ;";	
		
		try( 	Connection connection = this.datasource.getConnection();
				PreparedStatement prepStmt = connection.prepareStatement(sql);
				){
			
			prepStmt.setInt(1, id);
			prepStmt.setString(2, tablePageViewData.getOrderBy());
			prepStmt.setInt(3, tablePageViewData.getOffset());
			prepStmt.setInt(4, tablePageViewData.getRowCount());
			ResultSet resultSet = prepStmt.executeQuery();
			while( resultSet.next() ) {
				list.add(resultSet.getInt("un.buildingId"));
			}
		}
		catch (SQLException e) {
	    	e.printStackTrace(); 
	        logger.log(Level.WARNING, e.getMessage());
	        throw new DataAccessException("Unable to get Data from DB.");            
	    }		
		return list;
	}

	/**
	 * maps a row to BuildingOwnerObject
	 * 
	 * @param  resultSet
	 * @return BuildingOwner
	 * */
	public BuildingOwner mapRowToBuildingOwner( ResultSet resultSet ) throws SQLException {

		BuildingOwner buildingOwner = new BuildingOwner();
		
		buildingOwner.setId( resultSet.getInt( BuildingOwnerDaoJdbc.TABLE_NAME_PREFIX +  ".id" ) );
		
		Contact contact = new Contact();
		contact.setId( resultSet.getInt( BuildingOwnerDaoJdbc.TABLE_NAME_PREFIX + ".contactId" ) );
		buildingOwner.setContact( contact );

		LoginUser loginUser = new LoginUser();
		loginUser.setId( resultSet.getInt( BuildingOwnerDaoJdbc.TABLE_NAME_PREFIX + ".loginUserId" ) );
		buildingOwner.setLoginUser(loginUser);
		
		
		return buildingOwner;		
	}
}
