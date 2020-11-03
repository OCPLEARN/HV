package de.ocplearn.hv.test.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.model.Building;

@SpringBootTest
public class BuildingServiceTest {

	@Test
	public void testCreateBuilding() {
		Building b = new Building();
		b.setName("new Building!");
		System.out.println(b);
	}
	
}
