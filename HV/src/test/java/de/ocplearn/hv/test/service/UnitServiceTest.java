package de.ocplearn.hv.test.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.model.UnitType;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.test.TestObjectSupplier;

@SpringBootTest
public class UnitServiceTest {
	
	PropertyManagementService propertyManagementService; 
	
	@Autowired
	public UnitServiceTest( PropertyManagementService propertyManagementService ) {
		this.propertyManagementService = propertyManagementService;
	}

	
	@Test
	public void testPMServiceCreateUnit_givenModel2_booleanTrue() {
	
		PropertyManagementDto propertyManagementDto = TestObjectSupplier.getInstance().getModel("Model2");		
		
		List<BuildingDto> buildingsDto = propertyManagementService.findBuildingsByPropertyManagement(propertyManagementDto.getId());
		
		
		for (BuildingDto buildingDto : buildingsDto) {
			
			if(buildingDto.getName().equals("House 2 Model2")) {
				
				UnitDto unitDto = new UnitDto();
	
				unitDto.setAddress(buildingDto.getAddress());
				unitDto.setUnitName("Neue Garage");
				unitDto.setUnitType(UnitType.PARKING_UNIT);
				unitDto.setBuilding(buildingDto);
				unitDto.setUsableFloorSpace(1.0);
				unitDto.setConstructionYear(Integer.MAX_VALUE);
				unitDto.setNote("PopupGarage");	
				
				Assertions.assertTrue(propertyManagementService.createUnit(unitDto));		
				Assertions.assertTrue(propertyManagementService.deleteUnit(unitDto));
				
				
			}	
		}

	}
	
}
