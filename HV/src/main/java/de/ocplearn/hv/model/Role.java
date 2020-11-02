package de.ocplearn.hv.model;

/**
 * Roles of LoginUser.
 *
 */
public enum Role {
    
    SUPER_ADMIN( "Developer" ), 
    ADMIN( "Admin of operation team" ), 
    PROPERTY_MANAGER( "Default user" ) , 
    TENANT( "Inhabits a unit" ), 
    OWNER( "Owns a building" ),
    EMPLOYEE ( "Works for a Property Manager and is not a specifird contact (not a human)")
    ;
    
    private String description;
    
    public String getDescription(){
        return description;
    }
    
    Role(String description){
        this.description = description;
    }
    
    
    
    
}
