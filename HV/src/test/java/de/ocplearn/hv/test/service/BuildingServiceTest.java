package de.ocplearn.hv.test.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.mapper.BuildingMapper;
import de.ocplearn.hv.model.Building;

@SpringBootTest
public class BuildingServiceTest {

	

	private BuildingMapper buildingMapper;
	
	@Autowired
	public BuildingServiceTest(BuildingMapper buildingMapper) {
		super();
		this.buildingMapper = buildingMapper;
	}
	
	@Test
	public void testCreateBuilding() {
		Building b = new Building();
		b.setName("new Building!");
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA"+b);
		BuildingDto bD = buildingMapper.buildingToBuildingDto(b);
		
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA"+bD);
	}
	
}
