package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.BuildingType;
import de.ocplearn.hv.model.Ownership;
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
		"addressId", "buildingType", "note", "WEG_FLAG"	),
			new ArrayList<String>()
			);
	
	
	
	private DataSource dataSource;
	
	private AddressDao addressDao;
	
	@Autowired
	@Lazy
	private UnitDao unitDao;
	
	@Autowired
	private AddressDaoJdbc addressDaoJdbc; 
	
	@Autowired
	private BuildingOwnerDaoJdbc builingOwnerDaoJdbc; 	
	
	@Autowired
	private UnitDaoJdbc unitDaoJdbc;
	
	private PropertyManagementDaoJdbc propertyManagementDaoJdbc;
	
	public Logger logger = LoggerBuilder.getInstance().build( PropertyManagementDaoJdbc.class );
		
	
	
	
	@Autowired // DB einbinden über Autowire mit Qualifier Implementierung von AddressDao mit Qualifier spezifiziert
	public BuildingDaoJdbc( @Qualifier ("datasource1") DataSource dataSource, 
							@Qualifier("AddressDaoJdbc") AddressDao addressDao, 
							@Qualifier("BuildingOwnerDaoJdbc") BuildingOwnerDaoJdbc builingOwnerDaoJdbc,
							
							PropertyManagementDaoJdbc propertyManagementDaoJdbc
							) {
		this.dataSource = dataSource;
		this.addressDao = addressDao;
		this.builingOwnerDaoJdbc = builingOwnerDaoJdbc;
		this.unitDaoJdbc = unitDaoJdbc;
		this.propertyManagementDaoJdbc = propertyManagementDaoJdbc;
		//this.unitDao = unitDao;
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
		String sql = "INSERT INTO building (propertyManagementId, buildingName, addressId, buildingType, note, WEG_FLAG) values (?,?,?,?,?,?);"; 

		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); ){
	
			stmt.setInt(1, building.getPropertyManagement().getId());
			stmt.setString(2, building.getName());
			stmt.setInt(3, building.getAddress().getId());
			stmt.setString(4, building.getBuildingType().toString());
			stmt.setString(5, building.getNote());
			stmt.setBoolean(6, building.isWegType());
			

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
		String sql = "UPDATE building SET propertyManagementId=?, buildingName=?, addressId=?, buildingType=?, note=?, WEG_FLAG=? WHERE id =?;"; 
		
		  try(Connection con = this.dataSource.getConnection();
					PreparedStatement stmt = con.prepareStatement(sql);) {
			  stmt.setInt(1, building.getPropertyManagement().getId());
				stmt.setString(2, building.getName());
				stmt.setInt(3, building.getAddress().getId());
				stmt.setString(4, building.getBuildingType().toString());
				stmt.setString(5, building.getNote());
				stmt.setInt(6, building.getId());
				stmt.setBoolean(7, building.isWegType());
				
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

		String sql = "DELETE * FROM " + TABLE_NAME +" WHERE id = ?";
		
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
				String sql = "SELECT " + COLUMNS + ", " + PropertyManagementDaoJdbc.COLUMNS +
						", " + AddressDaoJdbc.COLUMNS +"  FROM " + TABLE_NAME + " AS " + TABLE_NAME_PREFIX 
				+ " JOIN " + PropertyManagementDaoJdbc.TABLE_NAME + " " + PropertyManagementDaoJdbc.TABLE_NAME_PREFIX 
				+ " ON  " + TABLE_NAME_PREFIX + ".propertyManagementId = "+ PropertyManagementDaoJdbc.TABLE_NAME_PREFIX +".id "
				+ "JOIN " + AddressDaoJdbc.TABLE_NAME + " " + AddressDaoJdbc.TABLE_NAME_PREFIX 
				+ " ON " + TABLE_NAME_PREFIX + ".addressId = " + AddressDaoJdbc.TABLE_NAME_PREFIX + ".id " 
				+ "WHERE " + TABLE_NAME_PREFIX + ".id = ?;";
				
				
				
		try( Connection con = this.dataSource.getConnection();
			 PreparedStatement stmt = con.prepareStatement( sql );
			) {
				stmt.setInt(1, id );
				
				System.out.println("BuidlingDao - findByIdPartial()  sql = \n" + stmt.toString());
				
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
		propertyManagement.setId( resultSet.getInt( BuildingDaoJdbc.TABLE_NAME_PREFIX + ".propertyManagementId" ) );
		building.setPropertyManagement(propertyManagement);
		
		// TODO setOwners
		building.setOwners(new ArrayList<BuildingOwner>());
		// TODO setUnits
		building.setUnits(new HashSet<Unit>());
		// TODO setTransactions
		building.setTransactions(new HashSet<Transaction>());
		
		building.setNote( resultSet.getString( BuildingDaoJdbc.TABLE_NAME_PREFIX + ".note" ) );
		
		building.setWegType(resultSet.getBoolean(BuildingDaoJdbc.TABLE_NAME_PREFIX + ".WEG_FLAG"));
		// List of Ownership
		building.setOwnerships( new ArrayList<Ownership>());		
		
		return building;
	}
	
	public Building mapRowToBuilding(ResultSet resultSet) throws SQLException {
		return mapRowToBuilding( resultSet, new Building() );
	}


	@Override
	public List<Building> findBuildingsByPropertyManagement(int propertyManagementId) {
		
		List<Building> buildingList = new ArrayList<>();
		String sql = "SELECT " + COLUMNS + " FROM " + TABLE_NAME +" AS " + TABLE_NAME_PREFIX + " WHERE " + TABLE_NAME_PREFIX +".propertyManagementId = ?;";
		try( Connection con = this.dataSource.getConnection();
			 PreparedStatement stmt = con.prepareStatement( sql );
			) {
		
		stmt.setInt(1, propertyManagementId);
		
		ResultSet resultSet = stmt.executeQuery();
		while(resultSet.next()) {
			
			Building building = mapRowToBuilding(resultSet); 
			
//			// TODO setOwners
//			building.setOwners(new ArrayList<BuildingOwner>());
//			// TODO setUnits
			building.setUnits( this.unitDao.findUnitsByBuildingIdFull(building.getId()) );
//			// TODO setTransactions
//			building.setTransactions(new HashSet<Transaction>());			
			
			// set ownership list for building)
			//building.setOwnerships( );
			building.getOwnerships().addAll( this.getOwnerships(building) );
			
			buildingList.add( building );		
		}
		
		return buildingList;
	
	} catch (SQLException e) {
	    e.printStackTrace(); 
	    logger.log(Level.WARNING, e.getMessage());
	    throw new DataAccessException("Unable to get Data from DB.");            
	    }
	}
	
	/**
	 * returns a List of Ownership instances
	 * */
	private List<Ownership> getOwnerships( Building building  ){
		// return 
		List<Ownership> ownerships = new ArrayList<>();

		// building has no owners!!
		//List<BuildingOwner> owners = building.getOwners();		
		
		// all units of building
		//Set<Unit> units = this.unitDao.findUnitsByBuildingIdFull(building.getId());
		
		String sql_All_UOL_Entries = "SELECT "+UnitDaoJdbc.COLUMNS+","
				+ " "+UnitDaoJdbc.COLUMNS_OWNER_LINK+","
				+ " "+BuildingOwnerDaoJdbc.COLUMNS+" "
				+ "FROM "+ UnitDaoJdbc.TABLE_NAME +" AS "+UnitDaoJdbc.TABLE_NAME_PREFIX+" "
				+ "INNER JOIN "+UnitDaoJdbc.TABLE_NAME_OWNER_LINK+" AS "+UnitDaoJdbc.TABLE_NAME_PREFIX_OWNER_LINK+" "
				+ "ON "+UnitDaoJdbc.TABLE_NAME_PREFIX+".id = "+UnitDaoJdbc.TABLE_NAME_PREFIX_OWNER_LINK+".unitId  "
				+ "INNER JOIN "+BuildingOwnerDaoJdbc.TABLE_NAME+" AS "+BuildingOwnerDaoJdbc.TABLE_NAME_PREFIX+" "
				+ "ON "+UnitDaoJdbc.TABLE_NAME_PREFIX_OWNER_LINK+".buildingOwnerId = "+BuildingOwnerDaoJdbc.TABLE_NAME_PREFIX+".id "
				+ "WHERE "+UnitDaoJdbc.TABLE_NAME_PREFIX+".buildingId = ? ";
		try( Connection conn = this.dataSource.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql_All_UOL_Entries)){
			
			stmt.setInt(1,building.getId());
			
			System.err.println("sql_All_UOL_Entries = " + sql_All_UOL_Entries);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				//owners.add(this.builingOwnerDaoJdbc.mapRowToBuildingOwner(rs));
				Ownership os = new Ownership(this.unitDaoJdbc.mapRowToUnit(rs),
						this.builingOwnerDaoJdbc.mapRowToBuildingOwner(rs),
						rs.getDouble("uol.buildingShare"),
						(rs.getDate("uol.shareStart")).toLocalDate(),
						(rs.getDate("uol.shareEnd")).toLocalDate());
				ownerships.add(os);
			}
			
		}catch (SQLException e) {
		    e.printStackTrace(); 
		    logger.log(Level.WARNING, e.getMessage());
		    throw new DataAccessException("Unable to get Data from DB.");            
		 }
		

		
//		for ( BuildingOwner owner : owners ) {
//			//SELECT * FROM uol WHERE 
//			Set<Unit> units = building.getUnits();
//			for ( Unit unit : units ) {
//				Optional<Ownership> ownerInfo = this.unitDao.getOwnership(unit, owner);
//				if ( ownerInfo.isPresent() ) {
//					ownerships.add(ownerInfo.get());
//				}  
//			} 
//		}

		return ownerships;
	}
	
	@Override
	public List<Integer> findBuildingOwnerIdsByBuildingId(int buildingId, TablePageViewData tablePageViewData) {
		
		
//		SELECT buildingOwnerId 					Auswahl Spalte buildingOwnerId
//		FROM unitOwnerLink 						aus Tabelle unitOwnerLink
//		INNER JOIN unit 						verknüpft mit Tabelle unit
//		ON unit.id = unitOwnerLink.unitId  		ON (Vorbedingung) Spalte unit.id = Spalte unitOwnerLink.unitId
//		WHERE  unit.buildingId = 1 ;			(in der Vorauswahl von ON wird gesucht WHERE unit.buildinId =...
		
		String sql = "SELECT "+UnitDaoJdbc.TABLE_NAME_PREFIX_OWNER_LINK +".buildingOwnerId FROM " +
					UnitDaoJdbc.TABLE_NAME_OWNER_LINK +" AS "+ UnitDaoJdbc.TABLE_NAME_PREFIX_OWNER_LINK 
					+ " INNER JOIN "+UnitDaoJdbc.TABLE_NAME +" AS "+ UnitDaoJdbc.TABLE_NAME_PREFIX
					+ " ON "+UnitDaoJdbc.TABLE_NAME_PREFIX+".id = " +UnitDaoJdbc.TABLE_NAME_PREFIX +".unitId"
					+ " WHERE " +UnitDaoJdbc.TABLE_NAME_PREFIX +".buildingId = ? AND +"
					+ UnitDaoJdbc.TABLE_NAME_PREFIX+".unitType='BUILDING_UNIT';";
					
					
		
		try( Connection connection = dataSource.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql);
			 ){
		 		stmt.setInt( 1, buildingId );
		 		ResultSet resultSet = stmt.executeQuery();
		 		
		 		List<Integer> buildingOwnerList = new ArrayList<>();
		 		while(resultSet.next()) {
		 			buildingOwnerList.add(resultSet.getInt("unitownerlink.buildingOwnerId"));
		 	
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
		
		String sql = "SELECT "+ COLUMNS + " FROM " +TABLE_NAME + " AS " +TABLE_NAME_PREFIX+" WHERE "+TABLE_NAME_PREFIX + ".propertyManagementId = ?;";
		
		List<Building> buildingList = new ArrayList<>();
		
		try( Connection connection = dataSource.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql);
			){
			stmt.setInt(1, propertyManangementId);
			
			ResultSet resultSet = stmt.executeQuery();
			
			while(resultSet.next()) {
				Building building = mapRowToBuilding(resultSet);
				building.setAddress(addressDao.findById(resultSet.getInt(TABLE_NAME_PREFIX+".addressId")).get());
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
	public boolean addOwnerToUnit(BuildingOwner buildingOwner, Unit unit, double buildingShare, LocalDate startShare) {

		String sql = "INSERT INTO unitownerlink (unitId, buildingOwnerId, buildingShare) VALUE (?, ?, ?);";
		try( Connection connection = dataSource.getConnection();
				 PreparedStatement stmt = connection.prepareStatement(sql);
		){
			stmt.setInt(1, unit.getId());
			stmt.setInt(2, buildingOwner.getId());
			stmt.setDouble(3, buildingShare);
			return stmt.executeUpdate() == 1 ? true : false; 			
		}catch( SQLException e ) {
			e.printStackTrace(); 
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB.");
		}
	}

	@Override
	public boolean removeOwnerFromUnit(BuildingOwner buildingOwner, Unit unit) {
		String sql = "DELETE FROM unitownerlink WHERE unitId = ? AND buildingOwnerId = ?; ";
		try( Connection connection = dataSource.getConnection();
				 PreparedStatement stmt = connection.prepareStatement(sql);
		){
			stmt.setInt(1, unit.getId());
			stmt.setInt(2, buildingOwner.getId());
			return stmt.executeUpdate() == 1 ? true : false; 			
		}catch( SQLException e ) {
			e.printStackTrace(); 
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB.");
		}
	}	
	
	@Override
	public boolean removeBuildingOwnerFromBuilding(BuildingOwner buildingOwner, Building building) {
		
		if(building.isWegType()) return false;
		
		int unitId = unitDao.getBuildingUnitFull(building.getId()).getId();
		
		String sql = "DELETE FROM " + UnitDaoJdbc.TABLE_NAME_OWNER_LINK + " WHERE " + 
					UnitDaoJdbc.TABLE_NAME_OWNER_LINK + ".unitId = ? AND " + 
					UnitDaoJdbc.TABLE_NAME_OWNER_LINK + ".buildingOwnerId = ?;";
		
		try (
				Connection connection = dataSource.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);
				) {
				stmt.setInt( 1, unitId );
				stmt.setInt( 1, buildingOwner.getId() );
				return (stmt.executeUpdate() > 0) ?  true :  false;
				
			}catch(SQLException e) {
				 e.printStackTrace(); 
				    logger.log(Level.WARNING, e.getMessage());
				    throw new DataAccessException("Unable to get Data from DB."); 
			}
		
		
	}


	@Override
	public Optional<Building> findByIdFull(int id) {
		
		String sql = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + " AS " + TABLE_NAME_PREFIX + " "
				+ "WHERE " + TABLE_NAME_PREFIX + ".id = ?; ";
		
		try( Connection con = this.dataSource.getConnection();
			PreparedStatement stmt = con.prepareStatement( sql );
			) {
			stmt.setInt(1, id );
			ResultSet resultSet = stmt.executeQuery();
			
			if (resultSet.next()) {
				Building building = this.mapRowToBuilding(resultSet);
				Optional<Address> optAddress = this.addressDao.findById( building.getAddress().getId() );
				if ( optAddress.isPresent() ) {
					building.setAddress(optAddress.get());
				}
				
				building.setUnits(unitDao.findUnitsByBuildingIdFull(id));
			
				building.setOwnerships(this.getOwnerships(building));
				
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


	@Override
	public boolean updateOwnership(BuildingOwner buildingOwner, Unit unit, double buildingShare) {
		// UPDATE unitownerlink SET buildingshare=? WHERE buildingid=? AND unitid=?;

		String sql = "UPDATE "  + UnitDaoJdbc.TABLE_NAME_OWNER_LINK + "SET buildingShare=? WHERE buildingId=? AND unitId=?;" ;

		
		try( Connection connection = dataSource.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);
				){
				
			stmt.setDouble(1, buildingShare);
			stmt.setInt(2, unit.getId());			// this must be a buildingUnit 
			stmt.setInt(3, buildingOwner.getId());
			
			return true;
			
		}catch(SQLException e) {
		    e.printStackTrace(); 
		    logger.log(Level.WARNING, e.getMessage());
		    throw new DataAccessException("Unable to get Data from DB.");

		}
	}
}














