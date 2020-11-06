# Reihenfolge in der SQL Befehle von der Datenbak ausgeführt werden
# Erklärung dazu:
# https://riptutorial.com/de/sql/example/12645/logische-reihenfolge-der-abfrageverarbeitung-in-sql
# Inner LEFT und RIGHT JOINs:
# https://www.w3schools.com/sql/sql_join.asp



SELECT * FROM loginUser, propertyManagement;

SELECT * FROM propertymanagementloginuserlink AS pmul;

SELECT * FROM propertymanagementloginuserlink AS pmul
JOIN loginUser;


SELECT * FROM propertymanagementloginuserlink AS pmul
JOIN loginUser AS lu
ON pmul.loginUserId = lu.id;

SELECT * FROM propertymanagement AS pm
LEFT JOIN propertymanagementloginuserlink AS pmul
ON pm.id = pmul.propertyManagementId;

SELECT * FROM propertymanagement AS pm
LEFT JOIN propertymanagementloginuserlink AS pmul
ON pm.id = pmul.propertyManagementId
WHERE pmul.propertyManagementId < 75;

SELECT pmul.loginUserId FROM propertymanagement AS pm
LEFT JOIN propertymanagementloginuserlink AS pmul
ON pm.id = pmul.propertyManagementId
WHERE pmul.propertyManagementId < 75;

# wegen dier Reihenfolge des Abarbeitens kann pmul.loginUserId und  pm.paymentType schon eingesetzt werden, bevor es deklariert wird

SELECT pmul.loginUserId, pm.paymentType FROM propertymanagement AS pm
LEFT JOIN propertymanagementloginuserlink AS pmul
ON pm.id = pmul.propertyManagementId
WHERE pmul.propertyManagementId < 75
ORDER BY pmul.propertyManagementId DESC
LIMIT 2,3;

# LIMIT 3 => die ersten drei Einträge
# Limit 2, 3 => offset und dann drei Einträge