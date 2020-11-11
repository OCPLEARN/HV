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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.Config;
import de.ocplearn.hv.util.DBConnectionPool;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.SQLUtils;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * Contact dao implementation for jdbc
 * */
@Repository
public class ContactDaoJdbc implements ContactDao {
	
	public static final String TABLE_NAME = "contact";
	public static final String TABLE_NAME_PREFIX = "co";
	public static final String COLUMNS = SQLUtils.createSQLString(
			TABLE_NAME_PREFIX, 
			Arrays.asList( "id", "timeStmpAdd", "timeStmpEdit", "sex", "firstName",
							"lastName", "isCompany", "companyName", "phone", "mobilePhone", "fax", "website", "email" ),
			new ArrayList<String>()
			);	

	// logger
	private Logger logger = LoggerBuilder.getInstance().build(ContactDaoJdbc.class);
	
	// jdbc 
	private static DBConnectionPool pool = DBConnectionPool.getInstance();
	
    //@Qualifier("datasource1")
    //@Autowired
	private DataSource datasource;	
	
	private AddressDao addressDao;
	
	@Autowired
	public ContactDaoJdbc(@Qualifier("datasource1") DataSource datasource, AddressDao addressDao ) {
	    	this.datasource = datasource;
	    	this.addressDao = addressDao;
	    }
	
	@Override
	public boolean save(Contact contact) {
		if (contact.getId()==0) {
			return insert(contact);
		}else{
			return update(contact);
		}
	}
	
	private boolean insert(Contact contact) {
		String sql = "Insert into contact(id, sex, firstName, lastName, isCompany, companyName, phone, mobilePhone, fax, website, email) value (null, ?,?,?,?,?,?,?,?,?,?);";
		  try(Connection con = getConnection();  
	        		PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS ); ) {
			  
			  stmt.setString(1, contact.getSex());
			  stmt.setString(2, contact.getFirstName());
			  stmt.setString(3, contact.getLastName());
			  stmt.setBoolean(4, contact.isCompany());
			  stmt.setString(5, contact.getCompanyName());
			  stmt.setString(6, contact.getPhone());
			  stmt.setString(7, contact.getMobilePhone());
			  stmt.setString(8, contact.getFax());
			  stmt.setString(9, contact.getWebsite());
			  stmt.setString(10, contact.getEmail());
			  
			  int key= stmt.executeUpdate();
			  if(key ==0) {
				  return false;
			  }
			  ResultSet rs = stmt.getGeneratedKeys();
			  rs.next();
			  contact.setId(rs.getInt(1));
		
			     returnConnection(con);
          } catch (SQLException e) {
          	 e.printStackTrace(); 
               logger.log(Level.WARNING, e.getMessage());
               throw new DataAccessException("Unable to get Data from DB. " + e.getMessage());	
          }         
      
      return true;
  }

	private boolean update(Contact contact) {
		String sql = "UPDATE contact SET sex=?, firstName=?, lastName=?, isCompany=?, companyName=?, phone=?, mobilePhone=?, fax=?, website=?, email=? where id =?;";
		  try(Connection con = getConnection();  
	        		PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS ); ) {
			  
			  stmt.setString(1, contact.getSex());
			  stmt.setString(2, contact.getFirstName());
			  stmt.setString(3, contact.getLastName());
			  stmt.setBoolean(4, contact.isCompany());
			  stmt.setString(5, contact.getCompanyName());
			  stmt.setString(6, contact.getPhone());
			  stmt.setString(7, contact.getMobilePhone());
			  stmt.setString(8, contact.getFax());
			  stmt.setString(9, contact.getWebsite());
			  stmt.setString(10, contact.getEmail());
			  stmt.setInt(11, contact.getId());
			  
			  int key= stmt.executeUpdate();
			  if(key ==0) {
				  return false;
			  }else {
				  returnConnection(con);
				  return true;
			  }
			 		
			     
        } catch (SQLException e) {
        	 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
        }         
   	}	
	
	@Override
	public boolean delete(Contact contact) {
		return deleteContactById( contact.getId() ) ;
	}

	@Override
	public boolean deleteContactById(int id) {
		System.out.println("deleteContactById (Parameter Id= " + id + ")");
		try(Connection connection = datasource.getConnection();
				PreparedStatement stmt = connection.prepareStatement("DELETE FROM contact WHERE id = ?;");){
			stmt.setInt(1, id);
			int result = stmt.executeUpdate();
			if (result==0) {
				return false;
			}else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace(); 
            logger.log(Level.WARNING, e.getMessage());
            throw new DataAccessException("Unable to get Data from DB.");
		}
	}

	@Override
	public Optional<Contact> findContactById(int id) {
		
		try(Connection connection = datasource.getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT " + COLUMNS + " FROM contact AS " + TABLE_NAME_PREFIX + " WHERE id = ?;");
				){
			
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			resultSet.next();
			Contact contact = this.mapRowToContact(resultSet);
			return Optional.ofNullable(contact);
			
		} catch (SQLException e) {
			 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
		}
	}

	@Override
	public List<Contact> findContactsByLastName(String lastName, TablePageViewData tablePageViewData) {
		
		try(Connection connection = datasource.getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT " + COLUMNS + " FROM contact AS " + TABLE_NAME_PREFIX 
						+ " ORDER BY ? ?"
						+ " LIMIT ?,?"
						+ " WHERE lastName = ?;");){
			String i1 = tablePageViewData.getOrderBy();
			String i2 = tablePageViewData.getOrderByDirection();
			stmt.setString(1, i1);
			stmt.setString(2, i2);
			int i3 = tablePageViewData.getOffset();
			stmt.setInt(3, i3);
			int i4 = tablePageViewData.getRowCount();
			stmt.setInt(4, i4);	
			stmt.setString(5, lastName);
			ResultSet resultSet = stmt.executeQuery();
			List<Contact> contactList= new ArrayList<Contact>();
			while(resultSet.next()) {
				Contact contact = new Contact();

				contact = this.mapRowToContact(resultSet);
				
				contactList.add(contact);
			}
			
			return contactList;
			
		} catch (SQLException e) {
			 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
		}
	}

	@Override
	public List<Contact> findContactsOfUnit(Unit unit, TablePageViewData tablePageViewData) {
		throw new UnsupportedOperationException("needs to be implemented");
	}

	@Override
	public List<Contact> findContactsIsCompany(boolean isCompany, TablePageViewData tablePageViewData) {

		List<Contact> contacts = new ArrayList<>();

		String sql = "SELECT " + COLUMNS + " FROM contacts AS " + TABLE_NAME_PREFIX + " ORDER BY ? ? LIMIT ?, ?  WHERE isCompany = ?;";
		
		try (
			Connection connection = datasource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
			) {
			stmt.setString(1, tablePageViewData.getOrderBy()  );
			stmt.setString(2, tablePageViewData.getOrderByDirection()  );
			stmt.setInt(3, tablePageViewData.getOffset()  );
			stmt.setInt(4, tablePageViewData.getRowCount()  );
			stmt.setString(5, (isCompany ? "1" : "0"));
			
			
			ResultSet resultSet = stmt.executeQuery();
			while( resultSet.next() ) {
				contacts.add(this.mapRowToContact(resultSet));
			}
			
		}catch (SQLException e) {
			 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
		}
		
		return contacts;
	}

	@Override
	public List<Contact> findContactsByCompanyName(String companyName, TablePageViewData tablePageViewData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contact> getAllContacts(TablePageViewData tablePageViewData) {
		try(Connection connection = datasource.getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT "+COLUMNS+" FROM contact AS "+TABLE_NAME_PREFIX+" "
						+ "ORDER BY ? ?"
						+ "LIMIT ?,?;");){
			String i1 = tablePageViewData.getOrderByDirection();
			String i2 = tablePageViewData.getOrderByDirection();
			stmt.setString(1, i1);
			stmt.setString(2, i2);
			int i3 = tablePageViewData.getOffset();
			stmt.setInt(3, i3);
			int i4 = tablePageViewData.getRowCount();
			stmt.setInt(4, i4);	
		
			ResultSet resultSet = stmt.executeQuery();
			List<Contact> contactList= new ArrayList<Contact>();
			while(resultSet.next()) {
				Contact contact = new Contact();
				contact = this.mapRowToContact(resultSet);
				contactList.add(contact);
			}
			
			return contactList;
		} catch (SQLException e) {
			 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
		}
	}

	@Override
	public boolean assignAddress(Contact contact, Address address) {		
		return assignAddress(contact.getId(), address);
	}

	@Override
	public boolean assignAddress(int contactId, Address address) {

		String sql = "INSERT INTO contactAddressLink ( contactId, addressId, addressType ) VALUE ( ?, ?, ? );";
		
		try ( Connection connection = datasource.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql); ) {
				
			stmt.setInt(1, contactId);
			stmt.setInt(2, address.getId());
			stmt.setString(3, address.getAddressType().toString());
			return ( stmt.executeUpdate() == 1 )? true : false;
			
		} catch (SQLException e) {
			 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
		}
	}
	
	@Override
	public boolean deleteAddressFromContact(int addressId) {
		
		String sql ="DELETE FROM contactAddressLink WHERE addressId = ?;";
		
		try ( Connection connection = datasource.getConnection();
			  PreparedStatement stmt = connection.prepareStatement(sql)	) {
			
			stmt.setInt(1, addressId);
			return (stmt.executeUpdate() == 1 ) ? true : false;
			
		} catch (SQLException e) {
			 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
		}	
	}
	
	@Override
	public List<Address> findAddressesByContactId(int id, TablePageViewData tablePageViewData) {

		List<Address> addresses = new ArrayList<Address>(); 
		
		String sql = "SELECT * FROM contactaddresslink WHERE contactId = ? ORDER BY ? ? LIMIT ?, ?;";
				
		try(Connection connection = datasource.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);
				){
			
			stmt.setInt(1, id);
			stmt.setString(2, "addressId");
			stmt.setString(3, "ASC");
			stmt.setInt(4, tablePageViewData.getOffset());
			stmt.setInt(5, tablePageViewData.getRowCount());
			
			ResultSet resultSet = stmt.executeQuery();
			
			while( resultSet.next() ) {
				int addressId = resultSet.getInt("addressId");
				
				Optional<Address> optionalAddress = addressDao.findById(addressId);
				if( optionalAddress.isPresent() ) {
					 addresses.add(optionalAddress.get());
				}
			}
		} catch (SQLException e) {
			 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
		}		
		return addresses;
	}

	private List<Contact> findByColumnNameValue( String columnName, String value ,TablePageViewData tablePageViewData ){
		
		List<Contact> contacts = new ArrayList<>();
		
		String sql ="SELECT "+COLUMNS+" FROM contact AS "+TABLE_NAME_PREFIX+" "
					+ "WHERE " + columnName + " LIKE "+ value +" "
					+ "ORDER BY "+ tablePageViewData.getOrderBy() +" " + tablePageViewData.getOrderByDirection() + " "
					+ "LIMIT "+tablePageViewData.getOffset()+","+ tablePageViewData.getRowCount() +" ;";
		
		try(
			Connection connection = this.datasource.getConnection();
			Statement stmt = connection.createStatement();
			){
			
			ResultSet resultSet = stmt.executeQuery(sql);
			while(resultSet.next()) {
				contacts.add( this.mapRowToContact(resultSet) );
			}
			
		}catch (SQLException e) {
			 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
		}	
		
		return contacts;
	}
	
	public Contact mapRowToContact( ResultSet resultSet, Contact contact ) throws SQLException {

		contact.setId(resultSet.getInt(TABLE_NAME_PREFIX + ".id"));
		contact.setSex(resultSet.getString(TABLE_NAME_PREFIX + ".sex"));
		contact.setFirstName(resultSet.getString(TABLE_NAME_PREFIX + ".firstName"));
		contact.setLastName(resultSet.getString(TABLE_NAME_PREFIX + ".lastName"));
		contact.setCompany(resultSet.getBoolean(TABLE_NAME_PREFIX + ".isCompany"));
		contact.setCompanyName(resultSet.getString(TABLE_NAME_PREFIX + ".companyName"));
		contact.setPhone(resultSet.getString(TABLE_NAME_PREFIX + ".phone"));
		contact.setMobilePhone(resultSet.getString(TABLE_NAME_PREFIX + ".mobilePhone"));
		contact.setFax(resultSet.getString(TABLE_NAME_PREFIX + ".fax"));
		contact.setWebsite(resultSet.getString(TABLE_NAME_PREFIX + ".website"));
		contact.setEmail(resultSet.getString(TABLE_NAME_PREFIX + ".email"));
		
		contact.setAddresses( this.findAddressesByContactId(contact.getId(), AddressDao.tablePageViewData ) );		
		
		return contact;		
	}
	
	public Contact mapRowToContact( ResultSet resultSet ) throws SQLException {
		Contact contact = new Contact();
		return this.mapRowToContact(resultSet, contact);
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
    
    /* return connection to pool */
    private void returnConnection( Connection connection ) {
    	if ( Config.useDBConnectionPool() ) {
    		pool.returnConnection(connection);	
    	}
    }

}
