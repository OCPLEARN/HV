package de.ocplearn.hv.model2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.ocplearn.hv.model2.LoginUser;
import de.ocplearn.hv.model2.Role;
import de.ocplearn.hv.util.Config;
import de.ocplearn.hv.util.DBConnectionPool;

/**
 * LoginUserDao implementation for jdbc access
 * 
 * */
@Component("LoginUserDaoJdbc")
public class LoginUserDaoJdbc implements LoginUserDao {

	// jdbc 
	private static DBConnectionPool pool = DBConnectionPool.getInstance();
	// sql data source
	private static DataSource ds;	
	
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
        
        try(Connection con = getConnection() ) {
                PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS );
                stmt.setString(1, loginUser.getLoginUserName()  );
                stmt.setBytes(2, loginUser.getPasswHash() );
                stmt.setBytes(3, loginUser.getSalt() );
                stmt.setString(4, loginUser.getRole().name()) ;
                stmt.setString(5, loginUser.getLocale().toString()) ;

                // get generated key
                int r = stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                loginUser.setId(rs.getInt(1));

                //System.out.println("r = " + r);
                returnConnection(con);
            } catch (SQLException e) {
                e.printStackTrace(); 
                return false;
            }         
        
        return true;
    }
    
    private boolean update(LoginUser loginUser){
        
        // ? (id,loginUserName,passwHash,salt,loginUserRole,locale) VALUE (null,?,?,?,?,?)
        String sql = "UPDATE loginUser SET loginUserName = ?, passwHash = ?, salt = ?, loginUserRole = ?, locale = ? WHERE id = ?;";
        
        try(Connection con = getConnection() ) {
                PreparedStatement stmt = con.prepareStatement(sql);

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
                return false;
            }            
        
        return true;
    }	
	
	@Override
	public boolean delete(LoginUser loginUser) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(String loginUserName) {
		// TODO Auto-generated method stub
		return false;
	}

    private static List<de.ocplearn.hv.model2.LoginUser> findByColumnName (String columnName, Object value){
        List<LoginUser> list = new ArrayList<>();
        
//        Object o;
//        Class t = o.getClass();
//        switch( t.getCanonicalName() ){
//            case "String":;
//            case "Integer":;
//        }
        
        try(    Connection con = getConnection();
                Statement stmt = con.createStatement();
                ){
            
            String sql = "SELECT * FROM loginUser WHERE "+ columnName +" = '" + value + "';";
            // WHERE col1 = v1 AND col2 = v2;
            System.out.println("findByColumnName() sql = " + sql);
            ResultSet result =  stmt.executeQuery( sql );
            
            while( result.next() ){
                LoginUser u = new LoginUser();

                u.setId( result.getInt("id") );
                u.setLoginUserName(result.getString("loginUserName"));
                u.setPasswHash(result.getBytes("passwHash"));
                u.setSalt(result.getBytes("salt"));
                u.setRole( Role.valueOf( result.getString("loginUserRole") )  );
                u.setLocale(new Locale(result.getString("locale"))  );   
                list.add(u);
            }
            
            returnConnection(con);
            
        }catch( SQLException e ){
            e.printStackTrace();
            return null;
        }         
        
        if (list.isEmpty()) {
        	list.add(null);
        }
        
        return list;
    }	
	
	@Override
	public Optional<de.ocplearn.hv.model2.LoginUser> findUserById(int id) {
		return Optional.of( findByColumnName("id", id).get(0) );
	}

	@Override
	public Optional<de.ocplearn.hv.model2.LoginUser> findUserByLoginUserName(String loginUserName) {
		return Optional.of(findByColumnName("loginUserName", loginUserName).get(0));
	}

	@Override
	public List<de.ocplearn.hv.model2.LoginUser> findAllByRole(Role role) {
		return findByColumnName("loginUserRole", role.name());
	}

	@Override
	public boolean userAlreadyExists(String loginUserName) {
		// read user from database
        try(Connection con = getConnection(); ) {
                PreparedStatement stmt = con.prepareStatement( "SELECT * FROM loginUser WHERE loginUserName = ?;" );
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
                e.printStackTrace(); System.exit(-1);            
            }            
        
        return false;
	}

	
    /* get a connection from pool */
    private static Connection getConnection() {
    	if ( Config.useDBConnectionPool() ) {
    		return pool.getConnection();	
    	}else {
    		try {
				return ds.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
    	}
    }
    
    /* return connection to poo */
    private static void returnConnection( Connection connection ) {
    	if ( Config.useDBConnectionPool() ) {
    		pool.returnConnection(connection);	
    	}
    	
    }   	
}
