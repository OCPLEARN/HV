package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.model.UnitType;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.SQLUtils;

@Repository
public class UnitDaoJdbc implements UnitDao {
	
	public static final String TABLE_NAME = "unit";
	public static final String TABLE_NAME_PREFIX = "un";
	public static final String COLUMNS = SQLUtils.createSQLString(
			TABLE_NAME_PREFIX, 
			Arrays.asList(
		"id", "timeStmpAdd", "timeStmpEdit", "buildingId", "unitName",
		"addressId", "usableFloorSpace", "constructionYear", "note", "unitType"	),
			new ArrayList<String>()
			);
	
	
	public static final String TABLE_NAME_OWNER_LINK = "unitownerlink";
	public static final String TABLE_NAME_PREFIX_OWNER_LINK = "unol";
	public static final String COLUMNS_OWNER_LINK = SQLUtils.createSQLString(
			TABLE_NAME_PREFIX_OWNER_LINK, 
			Arrays.asList(
		"id", "timeStmpAdd", "timeStmpEdit", "unitId", "buildingOwnerId"),
			new ArrayList<String>()
			);
	
	public static final String TABLE_NAME_RENTER_LINK = "unitrenterlink";
	public static final String TABLE_NAME_PREFIX_RENTER_LINK = "unrl";
	public static final String COLUMNS_RENTER_LINK = SQLUtils.createSQLString(
			TABLE_NAME_PREFIX_RENTER_LINK, 
			Arrays.asList(
		"id", "timeStmpAdd", "timeStmpEdit", "unitId", "renterId", 
		"moveIn", "moveOut"),
			new ArrayList<String>()
			);
	
	
	
	
	private DataSource dataSource;
	
	public Logger logger = LoggerBuilder.getInstance().build( PropertyManagementDaoJdbc.class );

	@Autowired
	private BuildingDao buildingDao;
	
	private AddressDao addressDao;
	
	@Autowired
	public UnitDaoJdbc(DataSource dataSource,AddressDao addressDao) {
		super();
		this.dataSource = dataSource;
		//this.buildingDao = buildingDao;
		this.addressDao = addressDao;
	}


	@Override
	public Unit getBuildingUnitFull(int buildingId) {
		String sql = "select " + COLUMNS + " from " + TABLE_NAME + " AS " + TABLE_NAME_PREFIX +" where " + TABLE_NAME_PREFIX +".buildingId=? and "+ TABLE_NAME_PREFIX+".unitType=?;";
		
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql); ){
			
			stmt.setInt(1, buildingId);
			stmt.setString(2, UnitType.BUILDING_UNIT.name());
			
			ResultSet resultSet = stmt.executeQuery();
			resultSet.next();
			Unit unit = mapRowToUnit(resultSet);
			
			// #1 building
			Optional<Building> optBuilding = this.buildingDao.findByIdFull( unit.getBuilding().getId() );
			if( !optBuilding.isPresent() ) throw new IllegalStateException("building id does not exist " + unit.getBuilding().getId() );
			unit.setBuilding( optBuilding.get() );
			// #2 Address
			Optional<Address> optAddress = this.addressDao.findById( unit.getAddress().getId() );
			if( !optAddress.isPresent() ) throw new IllegalStateException("address id does not exist " + unit.getAddress().getId() );
			unit.setAddress( optAddress.get() );
			
			return unit;
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());	
		}
	}


	private Unit mapRowToUnit(ResultSet resultSet) throws SQLException{
		return mapRowToUnit(resultSet, new Unit());
	}


	private Unit mapRowToUnit(ResultSet resultSet, Unit unit) throws SQLException{
		unit.setId(resultSet.getInt( UnitDaoJdbc.TABLE_NAME_PREFIX + ".id"));
		
		Building building = new Building();
		building.setId(resultSet.getInt( UnitDaoJdbc.TABLE_NAME_PREFIX + ".buildingId"));
		unit.setBuilding(building);
		
		unit.setUnitName(resultSet.getString(UnitDaoJdbc.TABLE_NAME_PREFIX + ".unitName"));
		
		Address address = new Address();
		address.setId(resultSet.getInt(UnitDaoJdbc.TABLE_NAME_PREFIX + ".addressId"));
		unit.setAddress(address);
		
		unit.setUsableFloorSpace(resultSet.getDouble(UnitDaoJdbc.TABLE_NAME_PREFIX + ".usableFloorSpace"));
		unit.setConstructionYear(resultSet.getInt(UnitDaoJdbc.TABLE_NAME_PREFIX + ".constructionYear"));
		unit.setNote(resultSet.getString(UnitDaoJdbc.TABLE_NAME_PREFIX + ".note"));
		unit.setUnitType(UnitType.valueOf(resultSet.getString(UnitDaoJdbc.TABLE_NAME_PREFIX + ".unitType")));
		
		return unit;
	}


	@Override
	public boolean save(Unit unit) {
		if (unit.getId()==0) {
			return insert(unit);
		}else {
			return update(unit);
		}
	}

	private boolean insert(Unit unit) {
		String sql = "INSERT INTO unit (buildingId, unitName, addressId, "
				+ "usableFloorSpace, constructionYear, note, unitType) "
				+ "values (?,?,?,?,?,?,?);";
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); ){
			System.out.println("private boolean insert(Unit unit)" + unit);
			stmt.setInt(1, unit.getBuilding().getId());
			stmt.setString(2, unit.getUnitName());
			stmt.setInt(3, unit.getAddress().getId());
			stmt.setDouble(4, unit.getUsableFloorSpace());
			stmt.setInt(5, unit.getConstructionYear());
			stmt.setString(6, unit.getNote());
			stmt.setString(7, unit.getUnitType().toString());
			
			
			if (stmt.executeUpdate() !=1) {
				return false;
			}
			else {
				ResultSet resultSet = stmt.getGeneratedKeys();	
				resultSet.next();
				unit.setId(resultSet.getInt(1)); 
										
				return true;
				}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());	
		}
	}

	private boolean update(Unit unit) {
		String sql = "UPDATE unit set buildingId=?, unitName=?, addressId=?, usableFloorSpace=?, constructionYear=?, note=?, unitType=?;";
		
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); ){
	
			stmt.setInt(1, unit.getBuilding().getId());
			stmt.setString(2, unit.getUnitName());
			stmt.setInt(3, unit.getAddress().getId());
			stmt.setDouble(4, unit.getUsableFloorSpace());
			stmt.setInt(5, unit.getConstructionYear());
			stmt.setString(6, unit.getNote());
			stmt.setString(7, unit.getUnitType().toString());
			
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
	public boolean delete(Unit unit) {
		String sql = "DELETE FROM unit where id= ?";
		
		try (	Connection connection = dataSource.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);
				) {
				stmt.setInt( 1, unit.getId() );
				return (stmt.executeUpdate() > 0) ?  true :  false;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());	
		}
	}


	@Override
	public Optional<Unit> findUnitByIdPartial(int id) {
		String sql = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + " AS " + TABLE_NAME_PREFIX + " WHERE " + TABLE_NAME_PREFIX + ".id=?;";
		try( Connection con = this.dataSource.getConnection();
				 PreparedStatement stmt = con.prepareStatement( sql );
				) {
					stmt.setInt(1, id );
					ResultSet resultSet = stmt.executeQuery();
					
					if (resultSet.next()) {
					
						Unit unit = this.mapRowToUnit(resultSet);
						return Optional.of(unit);
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
	public Optional<Unit> findUnitByIdFull(int id) {
		String sql = "SELECT "+COLUMNS + " FROM " + TABLE_NAME + " AS " +TABLE_NAME_PREFIX+
				" WHERE " +TABLE_NAME_PREFIX + ".id =?;";
		
		try( Connection con = this.dataSource.getConnection();
				 PreparedStatement stmt = con.prepareStatement( sql );
				) {
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			
			resultSet.next();  
			
				Optional<Building> optBuilding = this.buildingDao.findByIdPartial(resultSet.getInt(TABLE_NAME_PREFIX+".buildingId"));
				if ( !optBuilding.isPresent() ) throw new IllegalStateException("UniDao -  findUnitsByBuildingIdFull() given building not found by id!");
				Unit unit = new Unit();
				unit = this.mapRowToUnit(resultSet, unit );
				unit.setBuilding( optBuilding.get() );
				Optional<Address> optAddress = this.addressDao.findById( unit.getAddress().getId() );
				if (! optAddress.isPresent()) throw new IllegalStateException("given address not found by id!");
				unit.setAddress( optAddress.get() );
				return Optional.ofNullable(unit);
			
		
		} catch (SQLException e) {
		    e.printStackTrace(); 
		    logger.log(Level.WARNING, e.getMessage());
		    throw new DataAccessException("Unable to get Data from DB.");            
		}
	}


	@Override
	public Set<Unit> findUnitsByBuildingIdFull(int buildingId) {
		
		Set<Unit> units = new LinkedHashSet<Unit>();
		
		String sql = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + " AS " +
				TABLE_NAME_PREFIX + " WHERE " + TABLE_NAME_PREFIX + ".buildingId=?;";
		
		Optional<Building> optBuilding = this.buildingDao.findByIdPartial(buildingId);
		if ( !optBuilding.isPresent() ) throw new IllegalStateException("UniDao -  findUnitsByBuildingIdFull() given building not found by id!");
		
		try( Connection con = this.dataSource.getConnection();
				 PreparedStatement stmt = con.prepareStatement( sql );
				) {
			stmt.setInt(1, buildingId);
			ResultSet resultSet = stmt.executeQuery();
			while( resultSet.next() ) {
				Unit unit = new Unit();
				unit = this.mapRowToUnit(resultSet, unit );
				unit.setBuilding( optBuilding.get() );
				Optional<Address> optAddress = this.addressDao.findById( unit.getAddress().getId() );
				if (! optAddress.isPresent()) throw new IllegalStateException("given address not found by id!");
				unit.setAddress( optAddress.get() );
				units.add(unit);
			}
		
		} catch (SQLException e) {
		    e.printStackTrace(); 
		    logger.log(Level.WARNING, e.getMessage());
		    throw new DataAccessException("Unable to get Data from DB.");            
		}
		return units;
	}
}
