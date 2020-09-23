package de.ocplearn.hv.model2;

/**
 * Roles of LoginUser.
 *
 */
public enum Role {
    
    SUPER_ADMIN( "Developer" ), 
    ADMIN( "Admin of operation team" ), 
    PROPERTY_MANAGER( "Default user" ) , 
    TENANT( "Inhabits a unit" ), 
    OWNER( "Owns a building" );
    
    private String description;
    
    public String getDescription(){
        return description;
    }
    
    Role(String description){
        this.description = description;
    }
    
    
    
    
}
