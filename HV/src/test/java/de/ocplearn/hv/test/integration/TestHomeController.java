package de.ocplearn.hv.test.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;



//@WebMvcTest(HomeController.class) // wertet nicht @Component, @Service und @Repository aus
@SpringBootTest						// nötig, weil @Security braucht @Component von CustomAuthenticationProvider, (Autowired in SpringBoot)
@AutoConfigureMockMvc
public class TestHomeController {
	
	@Autowired
	private MockMvc mockMvc;	
	
	@Test
	public void testHome_getRequest_returnStatus200() throws Exception {
				
		mockMvc.perform(get("/"))
		.andExpect(status().isOk());
		
	}

	
	
}
