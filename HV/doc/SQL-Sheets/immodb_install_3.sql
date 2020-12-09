#################################################################################
##### Befehl zum Export des DB-Schemas ohne Daten
##### mysqldump --no-data -u someuser -p mydatabase


##### Datenbank mit Daten exportieren  (vorher Datenbank stoppen) // backup
##### mysqldump -u root -p[root_password] [database_name] > dumpfilename.sql
##### mysqldump -u root -pPa$$w0rd  immodb > c:\tmp\immodb_20201022.sql

##### Datenbank mit Daten importieren
##### mysql -u root -p[root_password] [database_name] < dumpfilename.sql
##### mysql -u root -pPa$$w0rd immodb < c:\tmp\immodb_20201022.sql
#################################################################################




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
 
   CREATE TABLE propertyManagementLoginUserLink (
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		propertyManagementId INT NOT NULL,
		loginUserId INT NOT NULL,
		
		
		CONSTRAINT fk_propertyManagementLoginUserLink_propertyManagementId					/* fk = foreignKey Benennung des Constraint*/
		FOREIGN KEY ( propertyManagementId )												/* Spaltenname des ForeignKey */
		REFERENCES propertyManagement ( id), 												/*aus der Referenztabelle*/
		
		CONSTRAINT fk_propertyManagementLoginUserLink_loginUserId 			/* fk = foreignKey*/
		FOREIGN KEY ( loginUserId )										/* Spaltenname des ForeignKey */
		REFERENCES loginUser ( id ) 										/*Referenztabelle*/
	);
 
 INSERT INTO loginUser (id,loginUserName,passwHash,salt,loginUserRole,locale) VALUE (null,'admin',x'DAABCAD4BF38345219D82F6ABBB48527',x'54531AECA828837A01839EF38D52F1466A363A3F09FB634FD5B461D2BB638E22','ADMIN','de_DE');
 
 #CHANGE:
ALTER TABLE contact DROP FOREIGN KEY fk_contact_propertyManagerId;
ALTER TABLE contact DROP COLUMN propertyManagerId;
ALTER TABLE propertyManagement ADD COLUMN companyContactId int not null AFTER paymentType;
ALTER TABLE propertyManagement ADD CONSTRAINT fk_propertyManagement_companyContactId FOREIGN KEY (companyContactId) REFERENCES contact (id) ;
ALTER TABLE propertyManagement MODIFY primaryLoginUserId int not null unique;
ALTER TABLE immodb.contactAddressLink CHANGE COLUMN adrType  addressType varchar(50);

ALTER TABLE immodb.building CHANGE COLUMN propertyManagerId  propertyManagementId INT NOT NULL;
ALTER TABLE `immodb`.`building` DROP FOREIGN KEY `fk_building_propertyManagerId`;
ALTER TABLE `immodb`. `building`
ADD CONSTRAINT `fk_building_propertyManagementId`
	FOREIGN KEY (`propertyManagementId`)
	REFERENCES `immodb`. `propertymanagement` (`id`);

ALTER TABLE `immodb`. `buildingowner` DROP FOREIGN KEY fk_buildingOwner_propertyManagerId;
ALTER TABLE `immodb`.`buildingowner` DROP COLUMN propertyManagerId;
CREATE TABLE unitOwnerLink (
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
		timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		unitId INT NOT NULL,
		buildingOwnerId INT NOT NULL,
		
		CONSTRAINT fk_unitOwnerLink_unitId 					/* fk = foreignKey Benennung des Constraint*/
		FOREIGN KEY ( unitId )									/* Spaltenname des ForeignKey */
		REFERENCES unit ( id), 							/*Referenztabelle*/
		
		CONSTRAINT fk_unitOwnerLink_buildingOwnerId 			/* fk = foreignKey*/
		FOREIGN KEY ( buildingOwnerId )								/* Spaltenname des ForeignKey */
		REFERENCES buildingOwner ( id ) 				/*Referenztabelle*/
	);
	   CREATE TABLE unitFeatures (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    featureName VARCHAR (255),
    featureValueBoolean char (1),
    APARTMENT_UNIT char(1),
	COMMERCIAL_UNIT char(1),
	PARKING_UNIT char(1),
    BUILDING_UNIT char(1),
    REAL_ESTATE_UNIT char(1),
    OTHER_UNIT char(1)
    );
    
    CREATE TABLE unitFeaturesLink (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	timeStmpAdd timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	timeStmpEdit timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    unitId INT NOT NULL,
    unitFeaturesId INT NOT NULL,
    featureValue VARCHAR(255),
    
    CONSTRAINT fk_unitFeaturesLink_unitId
    FOREIGN KEY (unitId)
    REFERENCES unit (id),
    
    CONSTRAINT fk_unitFeaturesLink_unitFeaturesId
    FOREIGN KEY (unitFeaturesId)
    REFERENCES unitFeatures (id)
    );
DROP TABLE buildingOwnerLink;

ALTER TABLE renter DROP FOREIGN KEY fk_renter_propertyManagerId;
ALTER TABLE renter DROP COLUMN propertyManagerId;
ALTER TABLE unit ADD COLUMN unitType varchar(50);
#18.11.2020
ALTER TABLE buildingOwner DROP FOREIGN KEY fk_buildingOwner_loginUserId;  
ALTER TABLE unit MODIFY usableFloorSpace DOUBLE(12,2); 

# 2020 11 20
CREATE VIEW `buildingowners_not_assiged` AS 
SELECT buildingowner.id AS 'buildingowner.id'  FROM buildingOwner WHERE buildingowner.id NOT IN (
SELECT DISTINCT bo.id FROM propertymanagement AS pm
JOIN building AS bu
	ON bu.propertyManagementId = pm.id
JOIN unit un 
	ON bu.id = un.buildingId
JOIN unitownerlink uol
	ON un.id = uol.unitId
JOIN buildingOwner bo
	ON uol.buildingOwnerId = bo.id
WHERE un.unitType = 'BUILDING_UNIT'
);

# 2020 11 24
ALTER TABLE buildingowner ADD COLUMN propertyManagementId INT NOT NULL;
# 20201204
ALTER TABLE renter ADD COLUMN propertyManagementId INT NOT NULL;
# 20201204
ALTER TABLE `immodb`.`renter` 
ADD CONSTRAINT `fk_renter_propertyManagementId`
  FOREIGN KEY (`propertyManagementId`)
  REFERENCES `immodb`.`propertymanagement` (`id`);
# 20201204
ALTER TABLE `immodb`.`buildingowner` 
ADD CONSTRAINT `fk_buildingowner_propertyManagementId`
  FOREIGN KEY (`propertyManagementId`)
  REFERENCES `immodb`.`propertymanagement` (`id`);

# 20201209 
ALTER TABLE renter DROP FOREIGN KEY fk_renter_loginUserId;
