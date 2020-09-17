package de.ocplearn.hv.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class StaticHelpers {
	
	 /**
     * Creates password hash
     * 
     * @param password clear text password
     * @param salt set it to null, for new hash
     */
    public static HashMap<String,byte[]> createHash( String password, byte [] salt ){
        HashMap<String,byte[]> r = new HashMap<>();

        byte[] hash = null;
        if ( salt == null ){
            SecureRandom random = new SecureRandom();
            salt = new byte[32];
            random.nextBytes(salt);
        }
        //byte[] salt = null;

        // KeySpec - A (transparent) specification of the key material that constitutes a cryptographic key.
        // PBEKeySpec - password-based encryption (PBE)
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);	

        SecretKeyFactory factory = null;
        try {
                // Returns a SecretKeyFactory object that converts secret keys of the specified algorithm.
                factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");	
        }catch( NoSuchAlgorithmException e ) {
                e.printStackTrace();
        }

        try {
                hash = factory.generateSecret(spec).getEncoded();	
        }
        catch(InvalidKeySpecException e) {
                e.printStackTrace();
        }

//        System.out.println( "password : " + password );
//        System.out.println( "hash : " + Arrays.toString(hash) );
//        System.out.println( "salt : " + Arrays.toString(salt) );            
        
        r.put("salt", salt);
        r.put("hash", hash);
        
        return r;
    }    
    

}
