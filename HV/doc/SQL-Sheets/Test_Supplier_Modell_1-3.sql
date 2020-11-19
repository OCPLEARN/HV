################################################################################

# Model1
SELECT * FROM loginUser WHERE loginUserName LIKE 'userModel1';
SELECT * FROM propertymanagement WHERE primaryLoginUserId = 640;
SELECT * FROM building WHERE propertyManagementId = 149;
SELECT * FROM unit WHERE buildingId = 33;

SELECT uol.buildingOwnerId, un.buildingId FROM unit AS un 
JOIN unitownerlink AS uol
ON uol.unitId = un.id
WHERE un.buildingId = 33 AND un.unitType = 'BUILDING_UNIT';


# Model2
SELECT * FROM loginUser WHERE loginUserName LIKE 'userModel2';
SELECT * FROM propertymanagement WHERE primaryLoginUserId = 641;
SELECT * FROM building WHERE propertyManagementId = 150;
SELECT * FROM unit WHERE buildingId = 34;	# House
SELECT * FROM unit WHERE buildingId = 35;	# House 2 HALL

SELECT uol.buildingOwnerId, un.buildingId FROM unit AS un 
JOIN unitownerlink AS uol
ON uol.unitId = un.id
WHERE un.buildingId = 34 OR un.buildingId = 35 AND un.unitType = 'BUILDING_UNIT';


# Model3
SELECT * FROM loginUser WHERE loginUserName LIKE 'userModel3';
SELECT * FROM propertymanagement WHERE primaryLoginUserId = 642;
SELECT * FROM building WHERE propertyManagementId = 151;
SELECT * FROM unit WHERE buildingId = 36;	# House
SELECT * FROM unit WHERE buildingId = 37;	# House 3 
SELECT * FROM unit WHERE buildingId = 38;	# House 2 HALL

SELECT uol.buildingOwnerId, un.buildingId FROM unit AS un 
JOIN unitownerlink AS uol
ON uol.unitId = un.id
WHERE un.buildingId = 36 
OR un.buildingId = 37 
OR un.buildingId = 38
AND un.unitType = 'BUILDING_UNIT';

################################################################################