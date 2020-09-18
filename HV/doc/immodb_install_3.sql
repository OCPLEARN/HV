#CREATE USER 'immodb'@'localhost' IDENTIFIED BY 'Pa$$w0rd';
#CREATE USER 'immodb'@'127.0.0.1' IDENTIFIED BY 'Pa$$w0rd';

#GRANT ALL ON immodb.* TO 'immodb'@'localhost';
#GRANT ALL ON immodb.* TO 'immodb'@'127.0.0.1';

#USE mysql;
#SELECT * FROM db;

DROP DATABASE IF EXISTS immodb;

CREATE DATABASE immodb CHARACTER SET utf8 COLLATE utf8_general_ci; 

#GRANT ALL PRIVILEGES ON immodb.* TO 'immodb'@'127.0.0.1' IDENTIFIED BY 'Pa$$w0rd';
#immodb = user , immodb.* = alle Tabellen in DB


USE immodb;

CREATE TABLE loginUser (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    loginUserName varchar(50) UNIQUE,
    passwHash varbinary(512),
    salt varbinary(512),
    loginUserRole varchar(50),
    locale varchar(20)
);

CREATE TABLE propertyManager (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    loginUserId INT NOT NULL,
    CONSTRAINT fk_propertyManager_loginUserId
    FOREIGN KEY ( loginUserId )
    REFERENCES loginUser ( id )
);

CREATE TABLE contact (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    propertyManagerId INT NOT NULL,
    sex varchar(30),
    firstName varchar(50),
    lastName varchar(50),
    isCompany bit,
    companyName varchar(50),
    phone varchar(50),
    mobilePhone varchar(50),
    fax varchar(50),
    website varchar(50),
    email varchar(50),
    
	CONSTRAINT fk_contact_propertyManagerId 					/* fk = foreignKey*/
    FOREIGN KEY ( propertyManagerId )					/* Spaltenname des ForeignKey der o.g. Tabelle (contact)*/
    REFERENCES propertyManager ( id ) 	/*Referenztabelle*/
);

CREATE TABLE renter (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    loginUserId INT DEFAULT 0,
    contactId INT NOT NULL,
	propertyManagerId INT NOT NULL,
    
	CONSTRAINT fk_renter_propertyManagerId 					/* fk = foreignKey*/
    FOREIGN KEY ( propertyManagerId )					/* Spaltenname des ForeignKey der o.g. Tabelle (address)*/
    REFERENCES propertyManager ( id ), 	/*Referenztabelle*/
    
    CONSTRAINT fk_renter_contactId 							/* fk = foreignKey*/
    FOREIGN KEY (contactId)										/* Spaltenname des ForeignKey */
    REFERENCES contact ( id ), 							/*Referenztabelle*/
    
    CONSTRAINT fk_renter_loginUserId
    FOREIGN KEY ( loginUserId )
    REFERENCES loginUser ( id )
);

CREATE TABLE address (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    street varchar(50),
    houseNumber varchar(20),
    adrLine1 varchar(50),
    adrLine2 varchar(50),
    city varchar(50),
    zip varchar(20),
    province varchar(50),
    country varchar(3),
    coordinate POINT
);

CREATE TABLE contactAddressLink (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    contactId INT NOT NULL,
    addressId INT NOT NULL,
    adrType varchar (50),
    
    CONSTRAINT fk_contactAddressLink_contactId
    FOREIGN KEY ( contactId )
    REFERENCES contact ( id ),
    
	CONSTRAINT fk_contactAddressLink_addressId
    FOREIGN KEY ( addressId )
    REFERENCES address ( id )    
);

CREATE TABLE buildingOwner (
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		contactId INT NOT NULL,
		propertyManagerId INT NOT NULL,
        loginUserId INT DEFAULT 0,
		
		CONSTRAINT fk_buildingOwner_propertyManagerId 					/* fk = foreignKey*/
		FOREIGN KEY ( propertyManagerId )					/* Spaltenname des ForeignKey der o.g. Tabelle (address)*/
		REFERENCES propertyManager ( id ), 	/*Referenztabelle*/
		
		CONSTRAINT fk_buildingOwner_contactId 						/* fk = foreignKey*/
		FOREIGN KEY (contactId)										/* Spaltenname des ForeignKey */
		REFERENCES contact ( id ), 							/*Referenztabelle*/
            
		CONSTRAINT fk_buildingOwner_loginUserId
		FOREIGN KEY ( loginUserId )
		REFERENCES loginUser ( id )
        
	);

CREATE TABLE building (
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		propertyManagerId INT NOT NULL,
		buildingName varchar(50),
		addressId INT NOT NULL,
		buildingType varchar(50),
		note varchar(1000),
		
		CONSTRAINT fk_building_propertyManagerId 					/* fk = foreignKey*/
		FOREIGN KEY ( propertyManagerId )						/* Spaltenname des ForeignKey der o.g. Tabelle (address)*/
		REFERENCES propertyManager ( id ), 		/*Referenztabelle*/
		
		CONSTRAINT fk_building_addressId 							/* fk = foreignKey*/
		FOREIGN KEY (addressId)										/* Spaltenname des ForeignKey */
		REFERENCES address ( id ) 							/*Referenztabelle*/
	);
    
    CREATE TABLE buildingOwnerLink (
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		buildingId INT NOT NULL,
		buildingOwnerId INT NOT NULL,
		
		CONSTRAINT fk_buildingOwnerLink_buildingId 					/* fk = foreignKey Benennung des Constraint*/
		FOREIGN KEY ( buildingId )									/* Spaltenname des ForeignKey */
		REFERENCES building ( id), 							/*Referenztabelle*/
		
		CONSTRAINT fk_buildingOwnerLink_buildingOwnerId 			/* fk = foreignKey*/
		FOREIGN KEY ( buildingOwnerId )								/* Spaltenname des ForeignKey */
		REFERENCES buildingOwner ( id ) 				/*Referenztabelle*/
	);
    
    CREATE TABLE unit (
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		buildingId INT NOT NULL,
		unitName varchar (50),
		addressId INT NOT NULL,
		usableFloorSpace DOUBLE(6,2),
		constructionYear INT,
		note varchar (1000),
		
		CONSTRAINT fk_unit_buildingId 							
		FOREIGN KEY (buildingId)										
		REFERENCES building ( id ), 	
		
		CONSTRAINT fk_unit_addressId 					
		FOREIGN KEY ( addressId )						
		REFERENCES address ( id ) 

	);
    
    CREATE TABLE unitRenterLink (
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		unitId INT NOT NULL,
		renterId INT NOT NULL,
		moveIn Date,
		moveOut Date,
		
		CONSTRAINT fk_unitRenterLink_unitId 					
		FOREIGN KEY ( unitId )									
		REFERENCES unit ( id ),  
		
		CONSTRAINT fk_unitRenterLink_tenantId 					
		FOREIGN KEY ( renterId )									
		REFERENCES renter ( id ) 
	);
    
    CREATE TABLE ressourceUsage (
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		unitId INT NOT NULL,
		usageOf varchar (50),
		usageAmount DOUBLE (7,3),
		accountingYear INT,
		
		CONSTRAINT fk_ressourceUsage_unitId 					
		FOREIGN KEY ( unitId )					
		REFERENCES unit ( id )
	);
    
 CREATE TABLE transactions(
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		propertyManagerId INT NOT NULL, 
        billingAccount varchar(50),
        buildingId INT NOT NULL,
		/* link to unit */	
        bookingYear INT,
        nameRecipient varchar(50),
        amountNet DOUBLE,
        vatRate INT,
        paymentTarget DATE,
        invoicePosition varchar(50),
        
		CONSTRAINT fk_transactions_propertyManagerId 					
		FOREIGN KEY ( propertyManagerId )					
		REFERENCES propertyManager ( id )         
 );   
 
 CREATE TABLE transcationsUnitLink(
 		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        transactionsId INT NOT NULL,
        untiId INT NOT NULL,
    
		CONSTRAINT fk_transcationsUnitLink_transactionId 					
		FOREIGN KEY ( transactionsId )					
		REFERENCES transactions ( id )   ,
        
		CONSTRAINT fk_transcationsUnitLink_unitId 					
		FOREIGN KEY ( untiId )					
		REFERENCES unit ( id )          
 );
 
 INSERT INTO loginUser (id,loginUserName,passwHash,salt,loginUserRole,locale) VALUE (null,'admin','40, -103, -54, -81, 71, 54, 60, 99, 80, -111, -77, -74, -32, -8, 54, 5','65, 120, 14, -58, -105, -71, -10, -58, 75, 72, 122, -42, 112, 93, -112, -74, 12, -9, 10, 87, 95, -57, 61, -2, -9, 20, 22, -54, 68, -31, -33, 58','ADMIN','en_US');
 