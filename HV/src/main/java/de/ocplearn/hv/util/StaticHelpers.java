package de.ocplearn.hv.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.List;

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
        HashMap<String,byte[]> passwordHashMap = new HashMap<>();

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
        
        passwordHashMap.put("salt", salt);
        passwordHashMap.put("hash", hash);
        
        return passwordHashMap;
    }    
    
    
    // Verteilungsschlüssel - allocationKey
    
//    public static double allocationKey(List<? extends Countable> listOfAll, List<? extends Countable> listOfPart) {
//    	
//    	for(<? extends Countable> all : listOfAll)
//    }
//  
    // get the ratio of two numbers 
    // numbers can be amount of objects (Building, Owner, Unit, Renter) or Double, Integer
    // T becomes 100%, U becomes partial
    // e.g. T all Renters of a building, U one or more specific Renters of a Building
    // e.g. T all sqm of a building, U specific amount of sqm of a Renter / Owner
    // or work with interface Allocatable:
    // 1 default method -> allocationKey(List<? extends Allocatable> allList, List<? extends Allocatable> partialList)
    // 2 method for incoming numbers: values need to be added and type is not safe (double vs Double) -> allocationKey(Double denominator, Double counter) {sum all usage / sum partial usage}
    
    // denominator entspricht 100 %
    // counter entspricht Anteil
    // Rückgabewert double entspricht Faktor
    
    // TODO method allocationKey
    
//    public static <T, U > double allocationKey( List<U> uList, List<T> tList) {
//    	Double counter = 0.0;
//    	Double denominator = 0.0;
//    	
//    	for(U u : uList) {
//    		if( u instanceof Number) {
//    			counter += (Double)u; 
//    			System.out.println(u);
//    			}									// alternativ: new Double(u);
//    		else counter += 1;
//    	}
//    	
//    	for(T t : tList) {
//    		 if (t instanceof Number) {
//    			 denominator += (Double) t;
//    			 System.out.println(denominator);
//    			 } 									// alternativ: new Double(t);
//    		 else denominator +=1;
//    	}
//    	
//    	
//    	System.out.println("====================");
//    	System.out.println(counter);
//    	System.out.println(denominator);
//    	return ( (double) ( counter / denominator) );
//    	
//    }
    

}
