package de.ocplearn.hv.model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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
import java.util.Objects;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.sql.DataSource;

//import de.ocplearn.immo.password.DBConnection;
import de.ocplearn.hv.util.Config;
import de.ocplearn.hv.util.DBConnectionPool;
import de.ocplearn.hv.util.MySQLDataSourceFactory;
import de.ocplearn.hv.util.StaticHelpers;
import sun.security.rsa.RSACore;

/**
 
 */
public class LoginUser implements Comparable<LoginUser> {
    
	// jdbc 
	private static DBConnectionPool pool = DBConnectionPool.getInstance();
//	// sql data source
	private static DataSource ds;
//	
//	static {
//		MySQLDataSourceFactory.initDS();
//		MySQLDataSourceFactory.getMySQLDataSource();
//	}
	
    private int id;
    private String loginUserName;
    private Role role;
    private byte [] passwHash;
    private byte [] salt;
    private Locale locale;

    public LoginUser(){}
    
    public LoginUser(String loginUserName, Role role, byte[] passwHash, byte[] salt, Locale locale) {
        this.loginUserName = loginUserName;
        this.role = role;
        this.passwHash = passwHash;
        this.salt = salt;
        this.locale = locale;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public byte[] getPasswHash() {
        return passwHash;
    }

    public void setPasswHash(byte[] passwHash) {
        this.passwHash = passwHash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

 
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.loginUserName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LoginUser other = (LoginUser) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.loginUserName, other.loginUserName)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(LoginUser o) {
        return this.getLoginUserName().compareTo(o.getLoginUserName());
    }
    
    /**
     * Stores LoginUser in database.
     * 
     * @return true, if successful
     */
    public boolean save(){
        if (this.id == 0){
            return this.insert();
        }else{
            return this.update();
        }
    }   
    
    private boolean insert(){
        
        String sql = "INSERT INTO loginUser (id,loginUserName,passwHash,salt,loginUserRole,locale) VALUE (null,?,?,?,?,?);";
        
        try(Connection con = getConnection() ) {
                PreparedStatement stmt = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS );
                stmt.setString(1, this.getLoginUserName()  );
                stmt.setBytes(2, this.getPasswHash() );
                stmt.setBytes(3, this.getSalt() );
                stmt.setString(4, this.getRole().name()) ;
                stmt.setString(5, this.getLocale().toString()) ;

                // get generated key
                int r = stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                this.setId(rs.getInt(1));

                //System.out.println("r = " + r);
                returnConnection(con);
            } catch (SQLException e) {
                e.printStackTrace(); 
                return false;
            }         
        
        return true;
    }
    
    private boolean update(){
        
        // ? (id,loginUserName,passwHash,salt,loginUserRole,locale) VALUE (null,?,?,?,?,?)
        String sql = "UPDATE loginUser SET loginUserName = ?, passwHash = ?, salt = ?, loginUserRole = ?, locale = ? WHERE id = ?;";
        
        try(Connection con = getConnection() ) {
                PreparedStatement stmt = con.prepareStatement(sql);

                stmt.setString(1, this.getLoginUserName()  );
                stmt.setBytes(2, this.getPasswHash() );
                stmt.setBytes(3, this.getSalt() );
                stmt.setString(4, this.getRole().name()) ;
                stmt.setString(5, this.getLocale().toString()) ;
                stmt.setInt(6, this.getId() ) ;
                
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
    
    private static List<LoginUser> findByColumnName (String columnName, Object value){
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
    
    /**
     * Finds user by given id
     *
     * @return LoginUser instance
     */
    public static LoginUser findUserById(int id){
//        LoginUser u = new LoginUser();
//        
//        try(    Connection con = DBConnection.getConnection();
//                Statement stmt = con.createStatement();
//                ){
//            
//            ResultSet result =  stmt.executeQuery("SELECT * FROM loginUser WHERE id = " + id);
//            result.next();  // move to first row
//            u.setId( result.getInt("id") );
//            u.setLoginUserName(result.getString("loginUserName"));
//            u.setPasswHash(result.getBytes("passwHash"));
//            u.setSalt(result.getBytes("salt"));
//            u.setRole( Role.valueOf( result.getString("loginUserRole") )  );
//            u.setLocale(new Locale(result.getString("locale"))  );
//            
//        }catch( SQLException e ){
//            e.printStackTrace();
//            return null;
//        }        
//        
//        return u;

        return findByColumnName("id", id).get(0);

    }
    
     /**
     * Finds user by given loginUserName
     *
     * @return LoginUser instance
     */
    public static LoginUser findUserByLoginUserName(String loginUserName) {
        return findByColumnName("loginUserName", loginUserName).get(0);
    }

     /**
     * Finds user by given loginUserName
     *
     * @return LoginUser instance
     */
    public static List<LoginUser> findAllByRole(Role role) {
        return findByColumnName("loginUserRole", role.name());
    }   
    
 
    /**
     * Validates credentials 
     * 
     * @return
     * */
    public static boolean validateUser( String loginUserName, String password ){

        HashMap<String,byte[]> userMap = null;
        byte[] hash = null;
        byte[] salt = null;
    
        // read user from database
        try(Connection con = getConnection(); ) {
                PreparedStatement stmt = con.prepareStatement( "SELECT * FROM loginUser WHERE loginUserName = ?;" );
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
                
                returnConnection(con);
                
            } catch (SQLException e) {
                e.printStackTrace(); System.exit(-1);            
            }
        return  Arrays.equals(userMap.get("hash"), hash );
    }    
    
    /**
     *  Checks user exists
     * @return true, if user is already in db
     */
    public static boolean userAlreadyExists( String loginUserName ){
        
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

