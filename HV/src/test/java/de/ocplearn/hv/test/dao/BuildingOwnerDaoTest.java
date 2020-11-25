package de.ocplearn.hv.test.dao;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dao.BuildingOwnerDao;
import de.ocplearn.hv.mapper.BuildingOwnerMapper;
import de.ocplearn.hv.model.BuildingOwner;

/**
 * Unit test BuildingOwner
 *
 */
@SpringBootTest
public class BuildingOwnerDaoTest {

	@Autowired
	@Qualifier("BuildingOwnerDaoJdbc")	
	private BuildingOwnerDao buildingOwnerDao;	
	
	@Autowired
	public BuildingOwnerMapper buildingOwnerMapper;			
	
	/**
	 * 
	 * */
	
//	//TODO	 BuildingOwnerDaoTest 
//	@Test
//	public void testFindBuildingOwner_Id_OwnerFound() {
//		
//		Optional<BuildingOwner> opt = this.buildingOwnerDao.findByIdFull(1);
//		Assertions.assertTrue(opt.isPresent());
//		System.out.println( "testFindBuildingOwner_Id_OwnerFound() : " + opt.get() );
//	}
	
	
}
