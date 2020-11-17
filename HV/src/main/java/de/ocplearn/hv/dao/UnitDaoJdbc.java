package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Unit;
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
	
	private DataSource dataSource;
	
	public Logger logger = LoggerBuilder.getInstance().build( PropertyManagementDaoJdbc.class );


	@Override
	public Unit getBuildingUnit(int buildingId) {
		String sql = "select " + COLUMNS + " from unit where buildingId=? and unitType=?;";
		
		try ( Connection connection = this.dataSource.getConnection(); 
				  PreparedStatement stmt = connection.prepareStatement(sql); ){
			
			stmt.setInt(1, buildingId);
			stmt.setString(2, "BUILDING_UNIT");
			
			ResultSet resultSet = stmt.executeQuery();
			Unit unit = mapRowToUnit(resultSet);
			return unit;
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());	
		}
	}


	private Unit mapRowToUnit(ResultSet resultSet) {
		return mapRowToUnit(resultSet, new Unit());
	}


	private Unit mapRowToUnit(ResultSet resultSet, Unit unit) {
		return null;
	}
	
}
