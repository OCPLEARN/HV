package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.BuildingType;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.model.Transaction;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.SQLUtils;
import de.ocplearn.hv.util.TablePageViewData;

@Repository
public class BuildingDaoJdbc implements BuildingDao{
	
	public static final String TABLE_NAME = "building";
	public static final String TABLE_NAME_PREFIX = "bu";
	public static final String COLUMNS = SQLUtils.createSQLString(
			TABLE_NAME_PREFIX, 
			Arrays.asList(
		"id", "timeStmpAdd", "timeStmpEdit", "propertyManagementId", "buildingName",
		"addressId", "buildingType", "note"	),
			new ArrayList<String>()
			);
	
	private DataSource dataSource;
	
	private AddressDao addressDao;
	
	private AddressDaoJdbc addressDaoJdbc; 
	
	private PropertyManagementDaoJdbc propertyManagementDaoJdbc;
	
	public Logger logger = LoggerBuilder.getInstance().build( PropertyManagementDaoJdbc.class );
		
	
	
	@Autowired // DB einbinden über Autowire mit Qualifier Implementierung von AddressDao mit Qualifier spezifiziert
	public BuildingDaoJdbc( @Qualifier ("datasource1") DataSource dataSource, 
							@Qualifier("AddressDaoJdbc") AddressDao addressDao, 
							AddressDaoJdbc addressDaoJdbc, PropertyManagementDaoJdbc propertyManagementDaoJdbc) {
		this.dataSource = dataSource;
	}
	
	
	@Override
	public boolean save(Building building) {
		if (building.getId()==0) {
			return insert(building);
		}else {
			return update(building);
		}
	}



	private boolean insert(Building building) {
		String sql = "INSERT INTO building (propertyManagementId, buildingName, addressId, buildingType, note) values (?,?,?,?,?);"; 
	System.out.println("INSERT BUILDING: "+building);
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); ){
	
			stmt.setInt(1, building.getPropertyManagement().getId());
			stmt.setString(2, building.getName());
			stmt.setInt(3, building.getAddress().getId());
			stmt.setString(4, building.getBuildingType().toString());
			stmt.setString(5, building.getNote());
			

			if (stmt.executeUpdate() !=1) {
				return false;
			}
			else {
				ResultSet resultSet = stmt.getGeneratedKeys();	
				resultSet.next();
				building.setId(resultSet.getInt(1)); 
										
				return true;
				}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());	
		}
	}
	
	
	private boolean update(Building building) {
		String sql = "UPDATE building SET propertyManagementId=?, buildingName=?, addressId=?, buildingType=?, note=? WHERE id =?;"; 
		
		  try(Connection con = this.dataSource.getConnection();
					PreparedStatement stmt = con.prepareStatement(sql);) {
			  stmt.setInt(1, building.getPropertyManagement().getId());
				stmt.setString(2, building.getName());
				stmt.setInt(3, building.getAddress().getId());
				stmt.setString(4, building.getBuildingType().toString());
				stmt.setString(5, building.getNote());
				stmt.setInt(6, building.getId());
				
				 int rowsAffected = stmt.executeUpdate();
	                
	                if (rowsAffected != 1){
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

	
	@Override
	public boolean deleteById(int buildingId) {
		// TODO please code check, Dominik

		String sql = "DELETE * FROM building WHERE id = ?";
		
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			) {
			stmt.setInt( 1, buildingId );
			return (stmt.executeUpdate() > 0) ?  true :  false;
			
		}catch(SQLException e) {
			 e.printStackTrace(); 
			    logger.log(Level.WARNING, e.getMessage());
			    throw new DataAccessException("Unable to get Data from DB."); 
		}
	}



	@Override
	public Optional<Building> findByIdPartial(int id) {
				String sql = "SELECT " + COLUMNS + ", pm.*, " + AddressDaoJdbc.COLUMNS +"  FROM building AS " + TABLE_NAME_PREFIX 
				+ "JOIN propertymanagement pm ON bu.propertyManagementId = bu.id "
				+ "JOIN " + AddressDaoJdbc.TABLE_NAME + " AS " + AddressDaoJdbc.TABLE_NAME_PREFIX 
				+ " ON " + TABLE_NAME_PREFIX + ".addressId = " + AddressDaoJdbc.TABLE_NAME_PREFIX + ".id " 
				+ "WHERE " + TABLE_NAME_PREFIX + ".id = ?;";
				
		try( Connection con = this.dataSource.getConnection();
			 PreparedStatement stmt = con.prepareStatement( sql );
			) {
				stmt.setInt(1, id );
				ResultSet resultSet = stmt.executeQuery();
				
				if (resultSet.next()) {
				
					Building building = this.mapRowToBuilding(resultSet);
					building.setAddress(addressDaoJdbc.mapRowToAddress(resultSet, building.getAddress()));
					building.setPropertyManagement(propertyManagementDaoJdbc.mapRowToPropertyManagement(resultSet, building.getPropertyManagement()));
					return Optional.of(building);
				} else {
					return Optional.empty();
				}
			
		} catch (SQLException e) {
			    e.printStackTrace(); 
			    logger.log(Level.WARNING, e.getMessage());
			    throw new DataAccessException("Unable to get Data from DB.");            
			    }		
	}

	public Building mapRowToBuilding( ResultSet resultSet, Building building ) throws SQLException {
		// 
		building.setId( resultSet.getInt( BuildingDaoJdbc.TABLE_NAME_PREFIX + ".id" ) );
		building.setName( resultSet.getString( BuildingDaoJdbc.TABLE_NAME_PREFIX + ".buildingName" ) );
		building.setBuildingType( BuildingType.valueOf( resultSet.getString( BuildingDaoJdbc.TABLE_NAME_PREFIX + ".buildingType" ) ) );		
		
		Address address = new Address();
		address.setId(resultSet.getInt( BuildingDaoJdbc.TABLE_NAME_PREFIX + ".addressId") );
		building.setAddress(address);
		
		PropertyManagement propertyManagement = new PropertyManagement();
		propertyManagement.setId( resultSet.getInt( BuildingDaoJdbc.TABLE_NAME_PREFIX + ".propertyManangementId" ) );
		building.setPropertyManagement(propertyManagement);
		
		// TODO setOwners
		building.setOwners(new ArrayList<BuildingOwner>());
		// TODO setUnits
		building.setUnits(new HashSet<Unit>());
		// TODO setTransactions
		building.setTransactions(new HashSet<Transaction>());
		
		building.setNote( resultSet.getString( BuildingDaoJdbc.TABLE_NAME_PREFIX + "note" ) );
	
				
		return building;
	}
	
	public Building mapRowToBuilding(ResultSet resultSet) throws SQLException {
		return mapRowToBuilding( resultSet, new Building() );
	}


	@Override
	public List<Integer> findBuildingOwnerIdsByBuildingId(int buildingId, TablePageViewData tablePageViewData) {
		// TODO exchange SQL hardcode with SQLUtils code
		
//		SELECT buildingOwnerId 					Auswahl Spalte buildingOwnerId
//		FROM unitOwnerLink 						aus Tabelle unitOwnerLink
//		INNER JOIN unit 						verknüpft mit Tabelle unit
//		ON unit.id = unitOwnerLink.unitId  		ON (Vorbedingung) Spalte unit.id = Spalte unitOwnerLink.unitId
//		WHERE  unit.buildingId = 1 ;			(in der Vorauswahl von ON wird gesucht WHERE unit.buildinId =...
		
		String sql = "SELECT unitOwnerLink.buildingOwnerId FROM unitOwnerLink" 
					+ "INNER JOIN unit"
					+ "ON unit.id = unitOwnerLink.unitId"
					+ "WHERE unit.buildingId = ?;";
					
					
		
		try( Connection connection = dataSource.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql);
			 ){
		 		stmt.setInt( 1, buildingId );
		 		ResultSet resultSet = stmt.executeQuery();
		 		
		 		List<Integer> buildingOwnerList = new ArrayList<>();
		 		while(resultSet.next()) {
		 			buildingOwnerList.add(resultSet.getInt("unitOwnerLink.buildingOwnerId"));
		 	
			}
			 	return 	buildingOwnerList;

		}catch( SQLException e ) {
				e.printStackTrace(); 
			    logger.log(Level.WARNING, e.getMessage());
			    throw new DataAccessException("Unable to get Data from DB.");
		}
		
	}

	@Override
	public List<Building> getAllBuildingsOfPropertyManagementById(int propertyManangementId) {
		// TODO change SQL to STATIC FIELDS AND CALL mapToRow(resultSet) instead while loop code
		
		String sql = "SELECT * FROM building WHERE building.propertyManagementId = ?;";
		
		List<Building> buildingList = new ArrayList<>();
		
		try( Connection connection = dataSource.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql);
			){
			stmt.setInt(1, propertyManangementId);
			
			ResultSet resultSet = stmt.executeQuery();
			
			while(resultSet.next()) {
				PropertyManagement propertyManagement = new PropertyManagement();
				propertyManagement.setId(propertyManangementId);
				Address address = new Address();
				address.setId(resultSet.getInt("addressId"));
				
				Building building = new Building();
				building.setId(resultSet.getInt( "id" ));
				building.setPropertyManagement( propertyManagement );
				building.setName(resultSet.getString( "name" ));	
				building.setNote(resultSet.getString("note"));
				building.setBuildingType(BuildingType.valueOf(resultSet.getString("buildingType")));
			//	building.setAddress(addressDao.findById(resultSet.getInt("addressId")).get());
				building.setAddress(address);
				building.setUnits(new HashSet<Unit>());
				building.setTransactions(new HashSet<Transaction>());
				building.setOwners(new ArrayList<BuildingOwner>());
				
				buildingList.add(building);
			}
			
			return buildingList;
						
			
		}catch( SQLException e ) {
				e.printStackTrace(); 
				logger.log(Level.WARNING, e.getMessage());
				throw new DataAccessException("Unable to get Data from DB.");
		}
	}

	@Override
	public boolean addBuildingOwnerToBuilding(BuildingOwner buildingOwner, Building building) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeBuildingOwnerFromBuilding(BuildingOwner buildingOwner, Building building) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Optional<Building> findByIdFull(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
