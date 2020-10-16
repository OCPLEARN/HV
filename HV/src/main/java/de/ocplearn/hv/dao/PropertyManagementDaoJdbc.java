package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;

import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.util.DBConnectionPool;
import de.ocplearn.hv.util.LoggerBuilder;

public class PropertyManagementDaoJdbc implements PropertyManagementDao{
	
	
	private Logger logger = LoggerBuilder.getInstance().build(PropertyManagementDaoJdbc.class);
	
	private static DBConnectionPool pool = DBConnectionPool.getInstance();
	
	private DataSource dataSource;
	
	public PropertyManagementDaoJdbc( @Qualifier("dataSource1") DataSource dataSource ) {
		
		this.dataSource = dataSource;
	}
	
	

	@Override
	public boolean save(PropertyManagement propertyManagement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<PropertyManagement> findPropertyManagementByPrimaryLoginUser(LoginUser primaryLoginUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyManagement findPropertyManagementByPrimaryContact(LoginUser primaryContact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<PropertyManagement> findPropertyManagementByLoginUser(LoginUser loginUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PropertyManagement> findPropertyManagementByPaymentType(PaymentType paymentType) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
