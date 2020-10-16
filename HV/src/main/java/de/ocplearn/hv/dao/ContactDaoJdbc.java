package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.util.Config;
import de.ocplearn.hv.util.DBConnectionPool;
import de.ocplearn.hv.util.LoggerBuilder;

public class ContactDaoJdbc implements ContactDao {

	private Logger logger = LoggerBuilder.getInstance().build(ContactDaoJdbc.class);
	
	// jdbc 
	private static DBConnectionPool pool = DBConnectionPool.getInstance();
	
    //@Qualifier("datasource1")
    //@Autowired
	private DataSource datasource;	
	
	public ContactDaoJdbc(@Qualifier("datasource1") DataSource datasource ) {
	    	this.datasource = datasource;
	    }
	
	
	@Override
	public boolean save(Contact contact) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private boolean insert(Contact contact) {
		String sql = "Insert into contact(id, propertyManagerId, sex, firstName, lastName, isCompany, companyName, phone, mobilePhone, fax, website, email"
				+ " value 				(null, null, ?,?,?,?,?,?,?,?,?,?);";
		  try(Connection con = getConnection();  
	        		PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS ); ) {
			  
			  stmt.setString(1, contact.getSex());
			  stmt.setString(2, contact.getFirstName());
			  stmt.setString(3, contact.getLastName());
			  stmt.setBoolean(4, contact.isCompany());
			  stmt.setString(5, contact.getCompayName());
			  stmt.setString(6, contact.getPhone());
			  stmt.setString(7, contact.getMobilePhone());
			  stmt.setString(8, contact.getFax());
			  stmt.setString(9, contact.getWebsite());
			  stmt.setString(10, contact.getEmail());
			  
			  int key= stmt.executeUpdate();
			  ResultSet rs = stmt.getGeneratedKeys();
			  rs.next();
			  contact.setId(rs.getInt(1));
		
			     returnConnection(con);
          } catch (SQLException e) {
          	 e.printStackTrace(); 
               logger.log(Level.WARNING, e.getMessage());
               throw new DataAccessException("Unable to get Data from DB.");
          }         
      
      return true;
  }
		
		
	

	@Override
	public boolean delete(Contact contact) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteContactById(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Contact contact) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<Contact> findContactById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contact> findContactsByLastName(String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contact> findContactsIsCompany(boolean isCompany) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contact> findContactsByCompanyName(String companyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contact> getAllContacts() {
		// TODO Auto-generated method stub
		return null;
	}


	
	// STATIC METHODS
	
    /* get a connection from pool */
    private Connection getConnection()  {
    	
    	if ( Config.useDBConnectionPool() ) {
    		return pool.getConnection();	
    	}else {
    		try {
				return datasource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
    	}
		//return null;
    }
    
    /* return connection to poo */
    private void returnConnection( Connection connection ) {
    	if ( Config.useDBConnectionPool() ) {
    		pool.returnConnection(connection);	
    	}
    	
    }	
}
