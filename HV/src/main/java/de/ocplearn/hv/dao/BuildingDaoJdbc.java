package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.TablePageViewData;

@Repository
public class BuildingDaoJdbc implements BuildingDao{

	private DataSource dataSource;
	
	public Logger logger = LoggerBuilder.getInstance().build( PropertyManagementDaoJdbc.class );
		
	
	@Autowired // DB einbinden über Autowire mit Qualifier
	public BuildingDaoJdbc( @Qualifier ("datasource1") DataSource dataSource ) {
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Building building) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<Building> findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> findBuildingOwnerIdsByBuildingId(int buildingId, TablePageViewData tablePageViewData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Building> getAllBuildings() {
		// TODO Auto-generated method stub
		return null;
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

}
