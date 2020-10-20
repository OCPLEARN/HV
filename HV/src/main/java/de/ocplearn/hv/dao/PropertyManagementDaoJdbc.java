package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

import javax.annotation.sql.DataSourceDefinition;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.service.PropertyManagementDao;
import de.ocplearn.hv.util.LoggerBuilder;


@Repository
public class PropertyManagementDaoJdbc implements PropertyManagementDao {
	
	private DataSource dataSource;
	
	public Logger logger = LoggerBuilder.getInstance().build( PropertyManagementDaoJdbc.class );
		
	
	@Autowired // DB einbinden über Autowire mit Qualifier
	public PropertyManagementDaoJdbc( @Qualifier ("datasource1") DataSource dataSource ) {
		this.dataSource = dataSource;
	}
	

	
	@Override
	public boolean save( PropertyManagement propertyManagement ) {
		
		
		if (propertyManagement.getId() == 0) {
			this.insert(propertyManagement);
		}
		
		
		return false;
	}

	@Override
	public boolean delete( PropertyManagement propertyManagement ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<PropertyManagement> findById( PropertyManagement propertyManagement ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<PropertyManagement> findByPrimaryContact( PropertyManagement propertyManagement ) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	private boolean insert (PropertyManagement propertyManagement ) {
		
		String sql = "INSERT INTO propertymanagement (id, primaryLoginUserId, paymentType, primaryContactId) VALUES (null, ?, ?, ?)";
		
		try ( Connection connection = this.dataSource.getConnection(); 
			  PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); ){
			
			stmt.setInt( 1, propertyManagement.getPrimaryLoginUser().getId() );
			stmt.setString( 2, propertyManagement.getPaymentType().toString() );
			stmt.setInt( 3, propertyManagement.getPrimaryContact().getId() );

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	

}
