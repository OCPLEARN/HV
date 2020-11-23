# DELETE FROM TABLE WITH CONSTRAINT
# the inner SELECT has to be named (as th)
delete from unitownerlink 
where id IN (
select * from(
	select uol.id from unitownerlink as uol
	 join buildingowner as bo 
	 on uol.buildingOwnerId=bo.id 
	 where bo.propertyManagementId=0 
     ) as th
     );