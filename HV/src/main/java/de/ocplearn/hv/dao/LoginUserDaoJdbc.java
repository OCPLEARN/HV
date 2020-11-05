package de.ocplearn.hv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.configuration.DataSourceConfig;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.exceptions.DataAccessException;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.util.Config;
import de.ocplearn.hv.util.DBConnectionPool;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.StaticHelpers;


/**
 * LoginUserDao implementation for jdbc access
 * 
 * */
@Component("LoginUserDaoJdbc")
public class LoginUserDaoJdbc implements LoginUserDao {

	private Logger logger = LoggerBuilder.getInstance().build(LoginUserDaoJdbc.class);
	
	// jdbc 
	private static DBConnectionPool pool = DBConnectionPool.getInstance();
	
    //@Qualifier("datasource1")
    //@Autowired
	private DataSource datasource;	
	
    public LoginUserDaoJdbc(@Qualifier("datasource1") DataSource datasource ) {
    	this.datasource = datasource;
    }
    
	@Override
	public boolean save(LoginUser loginUser) {
        if (loginUser.getId() == 0){
            return this.insert(loginUser);
        }else{
            return this.update(loginUser);
        }
	}

    private boolean insert(LoginUser loginUser){
        
        String sql = "INSERT INTO loginUser (id,loginUserName,passwHash,salt,loginUserRole,locale) VALUE (null,?,?,?,?,?);";
        
        try(Connection con = getConnection();  
        		PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS ); ) {
               
                stmt.setString(1, loginUser.getLoginUserName()  );
                stmt.setBytes(2, loginUser.getPasswHash() );
                stmt.setBytes(3, loginUser.getSalt() );
                stmt.setString(4, loginUser.getRole().name()) ;
                stmt.setString(5, loginUser.getLocale().toString()) ;

                // get generated key
                int r = stmt.executeUpdate();
                if ( r != 1 ) {return false;}
                
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                loginUser.setId(rs.getInt(1));

                //System.out.println("r = " + r);
                returnConnection(con);
            } catch (SQLException e) {
            	 e.printStackTrace(); 
                 logger.log(Level.WARNING, e.getMessage());
                 throw new DataAccessException("Unable to get Data from DB.");
            }         
        
        return true;
    }
    
    private boolean update(LoginUser loginUser){
        
        // ? (id,loginUserName,passwHash,salt,loginUserRole,locale) VALUE (null,?,?,?,?,?)
        String sql = "UPDATE loginUser SET loginUserName = ?, passwHash = ?, salt = ?, loginUserRole = ?, locale = ? WHERE id = ?;";
        
        try(Connection con = getConnection();
        		 PreparedStatement stmt = con.prepareStatement(sql);) {
               

                stmt.setString(1, loginUser.getLoginUserName()  );
                stmt.setBytes(2, loginUser.getPasswHash() );
                stmt.setBytes(3, loginUser.getSalt() );
                stmt.setString(4, loginUser.getRole().name()) ;
                stmt.setString(5, loginUser.getLocale().toString()) ;
                stmt.setInt(6, loginUser.getId() ) ;
                
                int r = stmt.executeUpdate();
                
                if (r != 1){
                    return false;
                }

                returnConnection(con);
            } catch (SQLException e) {
            	 e.printStackTrace(); 
                 logger.log(Level.WARNING, e.getMessage());
                 throw new DataAccessException("Unable to get Data from DB.");
            }            
        
        return true;
    }	
	
	@Override
	public boolean delete(LoginUser loginUser) {
		
		return delete(loginUser.getLoginUserName());
	}

	@Override
	public boolean delete(String loginUserName) {
		
		if (userAlreadyExists(loginUserName)) {
		
	        try(Connection con = getConnection();
	        		PreparedStatement stmt = con.prepareStatement( "DELETE FROM loginUser WHERE loginUserName = ?;" );) {
	                
	                stmt.setString(1, loginUserName  );
	                System.out.println("loginUserName" + loginUserName);
	            return (stmt.executeUpdate()==1)?true:false;
	               
	                
	            } catch (SQLException e) {
	            	 e.printStackTrace(); 
	                 logger.log(Level.WARNING, e.getMessage());
	                 throw new DataAccessException("Unable to get Data from DB.");            
	            }            
		}

		return false;
	}

    private List<LoginUser> findByColumnName (String columnName, Object value){
        List<LoginUser> list = new ArrayList<>();
        
//        Object o;
//        Class t = o.getClass();
//        switch( t.getCanonicalName() ){
//            case "String":;
//            case "Integer":;
//        }
        
        try(    Connection connection = getConnection();
                Statement stmt = connection.createStatement();
                ){
            
            String sql = "SELECT * FROM loginUser WHERE "+ columnName +" = '" + value + "';";
            // WHERE col1 = v1 AND col2 = v2;
            System.out.println("findByColumnName() sql = " + sql);
            ResultSet result =  stmt.executeQuery( sql );
            
            while( result.next() ){
               
                list.add(mapRowToLoginUser(result));
            }
            
            returnConnection(connection);
            
        }catch( SQLException e ){
        	 e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
        }         
        
        if (list.isEmpty()) {
        	list.add(null);
        }
        
        return list;
    }	
	
    public static LoginUser mapRowToLoginUser(ResultSet resultSet) throws SQLException {
    	 LoginUser loginUser = new LoginUser();
         
         loginUser.setId( resultSet.getInt("id") );
         loginUser.setLoginUserName(resultSet.getString("loginUserName"));
         loginUser.setPasswHash(resultSet.getBytes("passwHash"));
         loginUser.setSalt(resultSet.getBytes("salt"));
         loginUser.setRole( Role.valueOf( resultSet.getString("loginUserRole") )  );
         loginUser.setLocale(new Locale(resultSet.getString("locale"))  );   
         return loginUser;
    }
    
	@Override
	public Optional<LoginUser> findUserById(int id) {
		return Optional.ofNullable( findByColumnName("id", id).get(0) );
	}

	@Override
	public Optional<LoginUser> findUserByLoginUserName(String loginUserName) {
		return Optional.ofNullable( findByColumnName("loginUserName", loginUserName).get(0) );
	}

	@Override
	public List<LoginUser> findAllByRole(Role role) {
		return findByColumnName("loginUserRole", role.name());
	}

	@Override
	public boolean userAlreadyExists(String loginUserName) {
		// read user from database
        try(Connection con = getConnection();
        		PreparedStatement stmt = con.prepareStatement( "SELECT * FROM loginUser WHERE loginUserName = ?;" );) {
                
                stmt.setString(1, loginUserName  );
                ResultSet rs = stmt.executeQuery();
                if (rs.next()){
                	returnConnection(con);
                	return true;
                }else{
                	returnConnection(con);
                    return false;
                }
                
            } catch (SQLException e) {
            	 e.printStackTrace(); 
                 logger.log(Level.WARNING, e.getMessage());
                 throw new DataAccessException("Unable to get Data from DB.");        
            }            
        
        
	}

	
	
	/**
     * Validates credentials 
     * 
     * @return
     * */
    public boolean validateUser( String loginUserName, String password ){

        HashMap<String,byte[]> userMap = null;
        byte[] hash = null;
        byte[] salt = null;
    
        // read user from database
        try(Connection connection = getConnection();
        	PreparedStatement stmt = connection.prepareStatement( "SELECT * FROM loginUser WHERE loginUserName = ?;" );) {
               
                stmt.setString(1, loginUserName  );
                ResultSet rs = stmt.executeQuery();
                if (rs.next()){
                    hash = rs.getBytes("passwHash");
                    salt = rs.getBytes("salt");
                }else{
                    System.out.println("user not found!");
                    return false;
                }

                userMap = StaticHelpers.createHash( password, salt );
                
                returnConnection(connection);
                
            } catch (SQLException e) {
            	 e.printStackTrace(); 
                 logger.log(Level.WARNING, e.getMessage());
                 throw new DataAccessException("Unable to validate.");         
            }
        return  Arrays.equals(userMap.get("hash"), hash );
    }    
    
	


	@Override
	public List<LoginUser> findAllLoginUsers(int indexStart, int rowCount, String orderBy, String orderDirection) throws DataAccessException {
     
		 List<LoginUser> loginUserList = new ArrayList<>();

		 try(Connection connection = getConnection(); 
			 PreparedStatement stmt = connection.prepareStatement( "SELECT * FROM loginUser ORDER BY " 
					 + orderBy 
					 + " " 
					 + orderDirection 
					 + " LIMIT ?, ?;" );) {
             

             // SQL sorts different than Java 
             // Prepared Statements in SQL don´t accept ? on column name and sort order
             
//             stmt.setString(1, orderBy  );
//             stmt.setString(2, orderDirection);
             stmt.setInt(1, indexStart);
             stmt.setInt(2, rowCount);

             ResultSet resultSet = stmt.executeQuery();          
             
             while (resultSet.next()) {
            	 LoginUser loginUser = new LoginUser();
            	 loginUser.setId(resultSet.getInt("id"));
            	 loginUser.setLoginUserName(resultSet.getString("loginUserName"));
            	 loginUser.setRole(Role.valueOf(resultSet.getString("loginUserRole")));
            	 loginUser.setPasswHash(resultSet.getBytes("passwHash"));
            	 loginUser.setSalt(resultSet.getBytes("salt"));
            	 
            	 loginUserList.add(loginUser);
            	 //System.out.println(loginUser.getLoginUserName());

             }
          
         } catch (SQLException e) {
             e.printStackTrace(); 
             logger.log(Level.WARNING, e.getMessage());
             throw new DataAccessException("Unable to get Data from DB.");
         }            
     
		
		return loginUserList;
	}   	
    
	/**
     * 
     * @return int number of LoginUser objects in datastore
     * */
	public int getLoginUserCount() {
        try(Connection connection = getConnection();
            	PreparedStatement stmt = connection.prepareStatement( "SELECT COUNT(id) AS total FROM loginUser;" );) {

			ResultSet resultSet = stmt.executeQuery();
			resultSet.next();
			int count = resultSet.getInt("total");
			
			
	        returnConnection(connection);
	        return count;
	    } catch (SQLException e) {
	    	 e.printStackTrace(); 
	         logger.log(Level.WARNING, e.getMessage());
	         throw new DataAccessException("Unable to validate.");         
	    }		
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