package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.dto.RenterDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.Ownership;
import de.ocplearn.hv.model.Renter;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.model.UnitRental;
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
		"id", "timeStmpAdd", "timeStmpEdit", "unitId", "buildingOwnerId", "buildingShare", "shareStart", "shareEnd"),
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
	
	
	
	public static final String TABLE_NAME_RENTER = "renter";
	public static final String TABLE_NAME_PREFIX_RENTER = "re";
	public static final String COLUMNS_RENTER = SQLUtils.createSQLString( 
						TABLE_NAME_PREFIX_RENTER, 
						Arrays.asList( "id" ),
						new ArrayList<String>()
						);
	
	
	
	
	
	
	private DataSource dataSource;
	
	public Logger logger = LoggerBuilder.getInstance().build( PropertyManagementDaoJdbc.class );

	@Autowired
	private BuildingDao buildingDao;
	
	private AddressDao addressDao;
	
	private RenterDaoJdbc renterDaoJdbc;
	
	@Autowired
	public UnitDaoJdbc(DataSource dataSource,AddressDao addressDao, RenterDaoJdbc renterDaoJdbc) {
		super();
		this.dataSource = dataSource;
		//this.buildingDao = buildingDao;
		this.addressDao = addressDao;
		this.renterDaoJdbc = renterDaoJdbc;
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


	public Unit mapRowToUnit(ResultSet resultSet) throws SQLException{
		return mapRowToUnit(resultSet, new Unit());
	}


	public Unit mapRowToUnit(ResultSet resultSet, Unit unit) throws SQLException{
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
			//System.out.println("private boolean insert(Unit unit)" + unit);
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



	
	private boolean insertUnitRental(UnitRental unitRental) {
		
		String sql = "INSERT INTO "+ UnitDaoJdbc.TABLE_NAME_RENTER_LINK +" (id, unitId, renterId, moveIn, moveOut )"
				+ " VALUES ( NULL, ?, ?, ?, ? ); ";
		try( Connection con = this.dataSource.getConnection();
				 PreparedStatement stmt = con.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS);
				) {
			
			stmt.setInt(1, unitRental.getUnit().getId());
			stmt.setInt(2, unitRental.getRenter().getId());
			stmt.setDate(3, Date.valueOf(unitRental.getMoveIn()));
			stmt.setDate(4,(unitRental.getMoveOut()==null?null:Date.valueOf(unitRental.getMoveOut())));
			
						
			if (stmt.executeUpdate() !=1) {
				return false;
			}
			
			ResultSet resultSet = stmt.getGeneratedKeys();	
			resultSet.next();
			unitRental.setId(resultSet.getInt(1)); 
			return true;
			
		} catch (SQLException e) {
		    e.printStackTrace(); 
		    logger.log(Level.WARNING, e.getMessage());
		    throw new DataAccessException("Unable to insert Data into  DB.");            
		}
	}

	//old design, switched to updated dates to indicate old entries
	
//	@Override
//	public boolean removeRenterFromUnit(Renter renter, Unit unit) {
//		
//		String sql = "DELETE FROM "	+ UnitDaoJdbc.TABLE_NAME_RENTER_LINK
//					+ " WHERE renterId= ? AND unitId= ? ;"; 
//		
//		
//		try( Connection connection = dataSource.getConnection();
//			 PreparedStatement stmt = connection.prepareStatement(sql) ){
//			
//			stmt.setInt(1,  renter.getId() );
//			stmt.setInt(2,  unit.getId() );
//			
//			return ( stmt.executeUpdate() > 0 ) ? true : false;
//			
//		}catch(SQLException e) {
//			e.printStackTrace();
//			logger.log( Level.WARNING, e.getMessage() );
//			throw new DataAccessException("Unable to change data in DB");
//		}
//		
//	}

	
	

// SQL # find units by renter id
	
//	SELECT * FROM unit AS un
//	JOIN renter AS re
//	JOIN unitrenterlink AS unrl
//	ON re.id = unrl.renterid
//	WHERE un.id = unrl.unitid AND re.id = 2 ;



	
//	SELECT un.id, ... FROM unit AS un
//	JOIN unitrenterlink AS unrl
//	ON un.id = unrl.unitid
//	JOIN renter AS re
//	ON re.id = unrl.renterid
//	WHERE un.id = unrl.unitid AND re.id = 3 ;
	
	
	
	@Override
	public Set<Unit> findUnitsByRenterId(int renterId) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT " + COLUMNS + " FROM " + UnitDaoJdbc.TABLE_NAME + " AS " + UnitDaoJdbc.TABLE_NAME_PREFIX
					+ " JOIN " + UnitDaoJdbc.TABLE_NAME_RENTER_LINK + " AS " + UnitDaoJdbc.TABLE_NAME_PREFIX_RENTER_LINK
					+ " ON " + UnitDaoJdbc.TABLE_NAME_PREFIX + ".id = "
					+ " WHERE " + TABLE_NAME_PREFIX + ".id = ?;";
		
		
		try( Connection connection = dataSource.getConnection();
			 PreparedStatement stmt = connection.prepareStatement(sql);){
			
			stmt.setInt(1, renterId);
			
			ResultSet resultSet = stmt.executeQuery(sql);
			
			Set<Unit> units = new HashSet<Unit>();
			
//			for(Unit unit : units) {
//				unit.setId(id);
//				unit.set
		}catch( SQLException e) {
			e.printStackTrace();
			logger.log( Level.WARNING, e.getMessage() );
			throw new DataAccessException("Unable to read data in DB");
		}
		
		return null;
	}

	
	
	@Override
	public Optional<Ownership> getOwnership(Unit unit, BuildingOwner buildingOwner) {
		
		String sql = "SELECT "+COLUMNS_OWNER_LINK+" from "+TABLE_NAME_OWNER_LINK+" AS "+TABLE_NAME_PREFIX_OWNER_LINK+" "
				+ "WHERE "+TABLE_NAME_PREFIX_OWNER_LINK+".unitId = ? AND "+TABLE_NAME_PREFIX_OWNER_LINK+".buildingOwnerId = ?";
		try( Connection connection = dataSource.getConnection();
				 PreparedStatement stmt = connection.prepareStatement(sql);){
			stmt.setInt(1, unit.getId());
			stmt.setInt(2, buildingOwner.getId());
			
			ResultSet resultSet = stmt.executeQuery(sql);
			
			if ( resultSet.next() ) {
				java.sql.Date shareStart = resultSet.getDate( TABLE_NAME_PREFIX_OWNER_LINK + ".shareStart" );
				java.sql.Date shareEnd = resultSet.getDate( TABLE_NAME_PREFIX_OWNER_LINK + ".shareEnd" );
				
				
				Ownership ownership = new Ownership( unit, 
													buildingOwner, 
													resultSet.getDouble( TABLE_NAME_PREFIX_OWNER_LINK + ".buildingShare" ),
													shareStart == null ? null : shareStart.toLocalDate(),
													shareEnd == null ? null : shareEnd.toLocalDate()													
													);	
				return Optional.of(ownership);
			}
			return Optional.empty();
		}
		catch( SQLException e) {
			e.printStackTrace();
			logger.log( Level.WARNING, e.getMessage() );
			throw new DataAccessException("Unable to read data from DB");
		}		
	}


	@Override
	public boolean saveOwnership(Ownership ownership) {
		
		//System.err.println("UnitDao saveOwnership() : currentOwnership = " + ownership);
		
		if (ownership.getId()==0) {
			return insertOwnership(ownership);
		}else {
			return updateOwnership(ownership);
		}
		
	}
	private boolean insertOwnership(Ownership ownership) {                       
		String sql = "INSERT INTO " + TABLE_NAME_OWNER_LINK 
				+" (id, unitId, buildingOwnerId, buildingShare, shareStart)"
				+ " VALUES " 
				+ "( null, ?,?,?,?);";
		
		try ( Connection connection = this.dataSource.getConnection(); 
			  PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); ){
			
			stmt.setInt(1, ownership.getUnit().getId());
			stmt.setInt(2, ownership.getBuildingOwner().getId());
			stmt.setDouble(3, ownership.getBuildingShare());
			stmt.setDate(4, Date.valueOf(ownership.getShareStart()));
			//stmt.setDate(5, null );
		
			if (stmt.executeUpdate() != 1) return false;
			else {
				ResultSet resultSet = stmt.getGeneratedKeys();	
				resultSet.next();
				ownership.setId(resultSet.getInt(1));
				
				return true;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());		
        }
		
	}
	
	private boolean updateOwnership (Ownership ownership) {
		
//		String sql = "UPDATE " + TABLE_NAME_OWNER_LINK + " SET unidId=?, buildingOwnerId=?, buildingShare=?, "
//					+ "	shareStart=?, shareEnd=?;";

		String sql = "UPDATE " + TABLE_NAME_OWNER_LINK + " SET unitId=?, buildingOwnerId=?, buildingShare=?, "
		+ "	shareStart=?, shareEnd=? WHERE id = ?;";		
		
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql); ){
			
			stmt.setInt(1, ownership.getUnit().getId());
			stmt.setInt(2, ownership.getBuildingOwner().getId());
			stmt.setDouble(3, ownership.getBuildingShare());
			stmt.setDate(4, Date.valueOf(ownership.getShareStart()));
			stmt.setDate(5, ownership.getShareEnd() == null ? null :  Date.valueOf(ownership.getShareEnd()) );
			stmt.setInt(6, ownership.getId() );
			
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
	@Override
	public boolean saveUnitRental(UnitRental unitRental) {
		if(unitRental.getId()!=0) {
			return updateUnitRental(unitRental);
		}else {
			return insertUnitRental(unitRental);
		}
	}


	private boolean updateUnitRental(UnitRental unitRental) {
		String sql = "UPDATE " + TABLE_NAME_RENTER_LINK + " SET unitId=?, renterId=?, moveIn=?, moveOut=? WHERE id=?;";
		
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql); ){
		
		
		stmt.setInt(1, unitRental.getUnit().getId());
		stmt.setInt(2, unitRental.getRenter().getId());
		stmt.setDate(3, Date.valueOf(unitRental.getMoveIn()));
		stmt.setDate(4,(unitRental.getMoveOut()==null?null:Date.valueOf(unitRental.getMoveOut())));
		stmt.setInt(5, unitRental.getId());
		
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
	@Override
	public List<UnitRental> unitRentals(Unit unit) {
		
		List<UnitRental> unitRentals=new ArrayList<>();
		
		String sql="SELECT "+ COLUMNS_RENTER_LINK +" , " + COLUMNS_RENTER +
				" FROM " + TABLE_NAME_RENTER + 
				" AS " + TABLE_NAME_PREFIX_RENTER + 
				" INNER JOIN " + TABLE_NAME_RENTER_LINK + 
				" AS " + TABLE_NAME_PREFIX_RENTER_LINK +
				" WHERE " +TABLE_NAME_PREFIX_RENTER_LINK +".id = ?;";
		
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql); ){
			
			stmt.setInt(1, unit.getId());
			
			ResultSet resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				UnitRental unitRental = new UnitRental(unit, renterDaoJdbc.mapRowToRenter(resultSet), 
								resultSet.getDate(TABLE_NAME_PREFIX_RENTER+".moveIn").toLocalDate(),
								resultSet.getDate(TABLE_NAME_PREFIX_RENTER+".moveOut")
								==null?null:resultSet.getDate(TABLE_NAME_PREFIX_RENTER+".moveOut").toLocalDate());
				
				unitRentals.add(unitRental);
			}
			
			return unitRentals;
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());		
	    }
	}
	
	@Override
	public Optional<UnitRental> getUnitRental(Unit unit, Renter renter) {
		String sql = "SELECT " + COLUMNS_RENTER_LINK + " FROM " +
					TABLE_NAME_RENTER_LINK +
					" AS " + TABLE_NAME_PREFIX_RENTER_LINK +
					" WHERE " +TABLE_NAME_PREFIX_RENTER_LINK +".unitId = ? "+
					" AND " + TABLE_NAME_PREFIX_RENTER_LINK + ".renterId=?;";
		
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql); ){
			
			stmt.setInt(1, unit.getId());
			stmt.setInt(2, renter.getId());
			
			ResultSet resultSet = stmt.executeQuery(sql);
				if(resultSet.next()) {
				UnitRental unitRental = new UnitRental(unit, renter, 
						resultSet.getDate(TABLE_NAME_PREFIX_RENTER+".moveIn").toLocalDate(),
						resultSet.getDate(TABLE_NAME_PREFIX_RENTER+".moveOut")
						==null?null:resultSet.getDate(TABLE_NAME_PREFIX_RENTER+".moveOut").toLocalDate());
			
				return Optional.of(unitRental);
				}
				return Optional.empty();
				
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());		
	    }
	}
}

