package de.ocplearn.hv.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.model.BuildingType;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.model.Unit;
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
	
	// 	public boolean assignUnitOwnerToUnit (BuildingOwnerDto buildingOwnerDto, UnitDto unitDto);

	
	@Test
	public void testAssignUnitOwnerToUnit_givenModel3_returnBooleanTrue() {
		
		PropertyManagementDto propertyManagementDto = TestObjectSupplier.getInstance().getModel("Model3");
		
		// Model 3 HALL owned by one sister -> add the other sister
		
		List<BuildingDto> buildingsDto = propertyManagementService.findBuildingsByPropertyManagement(propertyManagementDto.getId());
		
		BuildingOwnerDto buildingOwnerDto = 
				new BuildingOwnerDto(  
						TestObjectSupplier.getInstance()
						.createContactDto( false, "Schwester", "Drei" ), 
						TestObjectSupplier.getInstance()
						.createLoginUserDto( Role.OWNER ),
						new ArrayList<>()
						);
		
		Assertions.assertTrue(this.propertyManagementService.createBuildingOwner(buildingOwnerDto));
		
		for( BuildingDto buildingDto : buildingsDto ) {	
			
			if ( buildingDto.getBuildingType() == BuildingType.HALL ) {
				
				Set<UnitDto> unitsDto = buildingDto.getUnits();
					unitsDto.forEach( unitDto -> {
						if( unitDto.getUnitType() == UnitType.BUILDING_UNIT ) { 
							
						Assertions.assertTrue( propertyManagementService.assignUnitOwnerToUnit( buildingOwnerDto, unitDto ) );
						System.out.println( "buildings of BuildingOwner: " + buildingOwnerDto.getBuildings() );
						Assertions.assertTrue( propertyManagementService.removeUnitOwnerFromUnit( buildingOwnerDto, unitDto ) );
						}
						
					});
					}
					
			}
			
		}
		
	
	
}










