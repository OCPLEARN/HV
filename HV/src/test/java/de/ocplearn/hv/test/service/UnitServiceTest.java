package de.ocplearn.hv.test.service;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ser.std.ClassSerializer;

import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.dto.OwnershipDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.dto.RenterDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.dto.UnitRentalDto;
import de.ocplearn.hv.model.BuildingType;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.model.UnitType;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.test.TestObjectSupplier;

@SpringBootTest
public class UnitServiceTest {
	
	PropertyManagementService propertyManagementService; 
	
	TestObjectSupplier testObjectSupplier;
	
	@Autowired
	public UnitServiceTest( 
			PropertyManagementService propertyManagementService,
			TestObjectSupplier testObjectSupplier
			) {
		this.propertyManagementService = propertyManagementService;
		this.testObjectSupplier = testObjectSupplier;
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
		
		// get Modle2
		PropertyManagementDto propertyManagementDto = TestObjectSupplier.getInstance().getModel("Model3");
		
		// Model 3 HALL owned by one sister -> add the other sister
		
		List<BuildingDto> buildingsDto = propertyManagementService.findBuildingsByPropertyManagement(propertyManagementDto.getId());
		
		BuildingOwnerDto buildingOwnerDto = 
				new BuildingOwnerDto(  
						TestObjectSupplier.getInstance()
						.createContactDto( false, "Schwester", "Drei" ), 
						TestObjectSupplier.getInstance()
						.createLoginUserDto( Role.OWNER ),
						new ArrayList<>(),
						propertyManagementDto
						);
		
		Assertions.assertTrue(this.propertyManagementService.createBuildingOwner(buildingOwnerDto));
		
		// find hall building in Model3
		BuildingDto hallBuilding = null;
		for( BuildingDto buildingDto : buildingsDto ) {	
			if ( buildingDto.getBuildingType() == BuildingType.HALL ) {
				hallBuilding = buildingDto;
				break;
			}
		}
		
		// get units from hall
		Set<UnitDto> unitsDto = hallBuilding.getUnits();
		UnitDto buildingUnitDto = null;
		for( UnitDto u : unitsDto ) {
			if ( u.getUnitType() == UnitType.BUILDING_UNIT  ) {
				buildingUnitDto = u;
				break;
			}
		}
		
		LocalDate today = LocalDate.now();
		

		
		// adjust old entry
		BuildingOwnerDto oldBuildingOwnerDto = (hallBuilding.getOwnerships().get(0)).getBuildingOwner();
		//OwnershipDto ownership1 = hallBuilding.getOwnerships().get(0);	// 1 owner
		OwnershipDto ownership1 = new OwnershipDto(buildingUnitDto,oldBuildingOwnerDto, 0.6, today,null);
		//ownership1.setBuildingShare(0.6);
		//ownership1.setShareStart(LocalDate.of(2021, 1, 1));
		Assertions.assertTrue(this.propertyManagementService.setOwnership(ownership1, hallBuilding, false));						
		
		// new owner	
		OwnershipDto ownership2 = new OwnershipDto(buildingUnitDto, buildingOwnerDto, 0.4, today, null);
		Assertions.assertTrue(this.propertyManagementService.setOwnership(ownership2, hallBuilding, false));			
		
		Assertions.assertTrue(this.propertyManagementService.validateOwnerships(hallBuilding));
		Assertions.assertEquals(1.0, this.propertyManagementService.getTotalValueOfOwnerships(hallBuilding));
		
		
		// Change Date
		LocalDate tommorrow = today.plusYears(1);
		// remove ownership2
		ownership2.setShareStart( tommorrow );
		Assertions.assertTrue( this.propertyManagementService.setOwnership(ownership2, hallBuilding, true) );			

		// set owenrship1 to 1.00
		ownership1.setShareStart( tommorrow );
		ownership1.setBuildingShare(1.00);
		
		Assertions.assertTrue( this.propertyManagementService.setOwnership(ownership1, hallBuilding, false) );			

		
		}
		
	
	
	/**
	 * (1) Assigns a renter to model2
	 * (2) Renter rents building 1, unit 1st, floor right (OG right)
	 * (3) Rental ends
	 * (4) Renter is removed from model 2 
	 *  
	 * */
	@Test
	public void testAssignUnitRenterToUnit_givenModel2_returnBooleanTrue() {
		
		// get model 1
		PropertyManagementDto propertyManagementDto = TestObjectSupplier.getInstance().getModel("Model2");
		
		List<BuildingDto> buildings = this.propertyManagementService.findBuildingsByPropertyManagement(propertyManagementDto.getId());
		BuildingDto buildingDto = null;
		for( BuildingDto build : buildings ) {
			if (build.getName().equals("House Model2")) {
				buildingDto = build;
				break;
			}		// 'House Model2'
		}
		// get Building unit
		UnitDto buildingUnit = null;
		UnitDto unitOGRight = null;
		for ( UnitDto unit : buildingDto.getUnits() ) {
			if ( unit.getUnitType().equals(UnitType.BUILDING_UNIT) ) {
				buildingUnit = unit;
			}else if ( unit.getUnitName().equals("OG right") ) {
				unitOGRight = unit;
			}
		}
		
		Assertions.assertNotNull(buildingUnit);
		Assertions.assertNotNull(unitOGRight);
		
		// House 2 found
		Assertions.assertTrue(buildingDto!= null);
		// find unit OG right
//		buildingDto.getUnits().stream()
//			.filter(un -> un.getUnitName().equals("OG right"))
//			.reduce()
		
		// create renter A
		RenterDto renterDto = new RenterDto();
		renterDto.setPropertyManagement(propertyManagementDto);
		// contact
		renterDto.setContact( this.testObjectSupplier.createContactDto(false, "Max", "Mustermann") );
		// loginuser
		renterDto.setLoginUser(null);
		// save renter
		Assertions.assertTrue(this.propertyManagementService.saveRenter(renterDto));
		
		//this.propertyManagementService.assignRenterToUnit(renterDto, );
		UnitRentalDto unitRental = new UnitRentalDto();
		unitRental.setRenter(renterDto);	
		unitRental.setUnit( unitOGRight );
		unitRental.setMoveIn(LocalDate.now());
		
		// set unitRental: renter A rents OG right
		Assertions.assertTrue(this.propertyManagementService.setUnitRental(unitRental)); 
		
		// rental ends
		unitRental.setMoveOut((LocalDate.now()).plusMonths(6));
		Assertions.assertTrue(this.propertyManagementService.setUnitRental(unitRental)); 
		
		// create renter B
		RenterDto renterBDto = new RenterDto();
		renterBDto.setPropertyManagement(propertyManagementDto);
		// renterBDto
		renterBDto.setContact(this.testObjectSupplier.createContactDto(false, "Peter", "Parker"));
		// loginuser
		renterBDto.setLoginUser(null);
		// save renter
		Assertions.assertTrue(this.propertyManagementService.saveRenter(renterBDto));				
		
		// rental for renter B one week after renter A moves out
		UnitRentalDto unitRentalB = new UnitRentalDto();
		unitRentalB.setRenter(renterBDto);	
		unitRentalB.setUnit( unitOGRight );
		unitRentalB.setMoveIn( (LocalDate.now()).plusMonths(6).plusWeeks(1) );		
		
		// save rental
		Assertions.assertTrue(this.propertyManagementService.setUnitRental(unitRentalB));	
	}	
	
}










