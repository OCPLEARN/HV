package de.ocplearn.hv.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import de.ocplearn.hv.controller.command.PropertyManagementRegistrationFormCommand;
import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.AddressType;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.service.UserServiceImpl;
import de.ocplearn.hv.util.CountryList;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.RegistrationObject;
import de.ocplearn.hv.util.StaticHelpers;

@Controller
public class HomeController {

	private UserService userService;
	
	private ApplicationContext applicationContext;
	
	private PropertyManagementService propertyManagementService;
	
	private Logger logger;
	
	@Autowired
	public HomeController(UserService userService,
			ApplicationContext applicationContext,
			PropertyManagementService propertyManagementService,
			@Autowired LoggerBuilder loggerBuilder) {
		this.userService = userService;
		this.applicationContext = applicationContext;
		this.propertyManagementService = propertyManagementService;
		this.logger = loggerBuilder.build("de.ocplearn.hv");
	}
	
	
	@GetMapping("/")
	public String home(Model model) {
		return "public/home";
	}
	
	
	@GetMapping("/page2")
	public String page2() {
		return "public/page2";
	}
	
	
	@GetMapping("/signin")
	public String signIn() {
		return "public/signin";
	}
	
	@ModelAttribute
	public void addAttributes(Model model) {

		Authentication authentication = SecurityContextHolder	.getContext()
				.getAuthentication();		
		
		Collection<? extends GrantedAuthority > granted =  authentication.getAuthorities();
		// Collection<? extends GrantedAuthority>
		
		model.addAttribute("loginUserName", authentication.getName() == null ? "X" : authentication.getName());
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		
		PropertyManagementRegistrationFormCommand propertyManagementRegistrationFormCommand = new PropertyManagementRegistrationFormCommand();
		
		
		CountryList countryList = this.applicationContext.getBean(CountryList.class);
		model.addAttribute("countryList", countryList.getCountryNames());
		model.addAttribute("PropertyManagementRegistrationFormCommand", propertyManagementRegistrationFormCommand);
		
		return "public/register";
	}
	
	@PostMapping("/register")
	public String createLoginUserDto(
					@Valid @ModelAttribute ("PropertyManagementRegistrationFormCommand") PropertyManagementRegistrationFormCommand propertyManagementRegistrationFormCommand,
					BindingResult bindingResult,
					Model model) {
		
		if(userService.loginUserExists(propertyManagementRegistrationFormCommand.getLoginUserName())) {
			bindingResult.rejectValue("loginUserName", "register.username.validation.usernameexists", "Username is already registered.");
			return "public/register";
		}
		
		if ( !( propertyManagementRegistrationFormCommand.getInitialPassword().equals(propertyManagementRegistrationFormCommand.getRepeatedPassword())) ) {
			bindingResult.rejectValue("repeatedPassword", "register.password.validation.repeaterror", "Repeated password doesn´t match first password");
		
			return "public/register";
		}
		
		if(bindingResult.hasErrors()) {
			return 	"public/register";
				
		} 	else {
					createPropertyManagement(propertyManagementRegistrationFormCommand);
				return "public/signin";
		}
	}
	
	private void createPropertyManagement(PropertyManagementRegistrationFormCommand propertyManagementRegistrationFormCommand) {
		HashMap<String, byte[]> passwordHashMap = StaticHelpers.createHash(propertyManagementRegistrationFormCommand.getInitialPassword(), null);
		
		// Company Contact
		ContactDto companyContactDto = new ContactDto();
		// Primary Contact
		ContactDto primaryContactDto = new ContactDto();
		
		// Company Address
		
		AddressDto companyAddressDto = new AddressDto();
		companyAddressDto.setStreet(propertyManagementRegistrationFormCommand.getCompanyStreet());
		companyAddressDto.setHouseNumber(propertyManagementRegistrationFormCommand.getCompanyHouseNumber());
		companyAddressDto.setApartment(propertyManagementRegistrationFormCommand.getCompanyApartment());
		companyAddressDto.setCity(propertyManagementRegistrationFormCommand.getCompanyCity());
		companyAddressDto.setProvince(propertyManagementRegistrationFormCommand.getCompanyProvince());
		companyAddressDto.setCountry(propertyManagementRegistrationFormCommand.getCompanyCountry());
		companyAddressDto.setZipCode(propertyManagementRegistrationFormCommand.getCompanyZipCode());
		companyAddressDto.setAddressType(AddressType.SECONDARY_BUSINESS_ADDRESS);

		
			
		// Primary Contact Address 
		// replaced by the following copy constructor
//		AddressDto secondaryAddressDto = new AddressDto();
//		secondaryAddressDto.setStreet(propertyManagementRegistrationFormCommand.getCompanyStreet());
//		secondaryAddressDto.setHouseNumber(propertyManagementRegistrationFormCommand.getCompanyHouseNumber());
//		secondaryAddressDto.setApartment(propertyManagementRegistrationFormCommand.getCompanyApartment());
//		secondaryAddressDto.setCity(propertyManagementRegistrationFormCommand.getCompanyCity());
//		secondaryAddressDto.setProvince(propertyManagementRegistrationFormCommand.getCompanyProvince());
//		secondaryAddressDto.setCountry(propertyManagementRegistrationFormCommand.getCompanyCountry());
//		secondaryAddressDto.setZipCode(propertyManagementRegistrationFormCommand.getCompanyZipCode());
//		secondaryAddressDto.setAddressType(AddressType.SECONDARY_BUSINESS_ADDRESS);

		
		// Primary Contact Address with copy constructor - may only be used before first writing to DB
		AddressDto secondaryAddressDto = new AddressDto(companyAddressDto);

		
		
		
		// Address Lists for both DTOs
		List<AddressDto> companyAddresses = new ArrayList<AddressDto>();
		companyAddresses.add(companyAddressDto);
		companyContactDto.setAddresses(companyAddresses);
		
		List<AddressDto> secondaryAddresses = new ArrayList<AddressDto>();
		secondaryAddresses.add(secondaryAddressDto);
		primaryContactDto.setAddresses(secondaryAddresses);
		
		
		// LoginUser
		
		LoginUserDto loginUserDto = new LoginUserDto();
		loginUserDto.setLoginUserName(propertyManagementRegistrationFormCommand.getLoginUserName());
		loginUserDto.setRole(Role.PROPERTY_MANAGER);
		loginUserDto.setLocale(LocaleContextHolder.getLocale());
		loginUserDto.setPasswHash(passwordHashMap.get("hash"));
		loginUserDto.setSalt(passwordHashMap.get("salt"));
		
		
	
	
		if(companyContactDto.isCompany()) {
			companyContactDto.setCompanyName(propertyManagementRegistrationFormCommand.getCompanyName());
			companyContactDto.setPhone(propertyManagementRegistrationFormCommand.getCompanyPhone());
			companyContactDto.setWebsite(propertyManagementRegistrationFormCommand.getCompanyWebsite());
			companyContactDto.setEmail(propertyManagementRegistrationFormCommand.getCompanyEmail());
			companyAddressDto.setAddressType(AddressType.PRIMARY_BUSINESS_ADDRESS);
			
		} else {
			companyContactDto.setCompanyName(propertyManagementRegistrationFormCommand.getPrimaryFirstName() 
										   + propertyManagementRegistrationFormCommand.getPrimaryLastName());
			companyContactDto.setPhone(propertyManagementRegistrationFormCommand.getPrimaryPhone());
			companyContactDto.setEmail(propertyManagementRegistrationFormCommand.getPrimaryEmail());
			secondaryAddressDto.setAddressType(AddressType.PRIMARY_BUSINESS_ADDRESS);
		}
		
		companyAddressDto.setAddressType(AddressType.BUILDING_ADDRESS);

	
			
		primaryContactDto.setFirstName(propertyManagementRegistrationFormCommand.getPrimaryFirstName());
		primaryContactDto.setLastName(propertyManagementRegistrationFormCommand.getPrimaryLastName());
		
		if( propertyManagementRegistrationFormCommand.getCompanyPhone().isEmpty() ) { 
						primaryContactDto.setPhone(propertyManagementRegistrationFormCommand.getPrimaryPhone());
					} else {
						primaryContactDto.setPhone(propertyManagementRegistrationFormCommand.getCompanyPhone());
					}
		if( propertyManagementRegistrationFormCommand.getCompanyEmail().isEmpty() ) { 
			primaryContactDto.setEmail(propertyManagementRegistrationFormCommand.getPrimaryEmail());
		} else {
			primaryContactDto.setEmail(propertyManagementRegistrationFormCommand.getCompanyEmail());
		}

	
		
		
		
		
		PropertyManagementDto propertyManagementDto = new PropertyManagementDto();
		
		propertyManagementDto.setPrimaryLoginUser(loginUserDto);
		propertyManagementDto.setPrimaryContact(primaryContactDto);
		propertyManagementDto.setCompanyContact(companyContactDto);
		// TODO
		//Change PaymentType
		propertyManagementDto.setPaymentType(PaymentType.PRO);
		// TODO
		//propertyManagementDto.setLoginUsers(loginUsers);

		if (! this.propertyManagementService.createPropertyManagement(propertyManagementDto)) {
			this.logger.log(Level.SEVERE, "pm registreation failed");
			new RuntimeException("Registration of PropertyManagement failed!");
		}
		
		this.logger.log(Level.INFO, "pm registered, id : " + propertyManagementDto.getId());
		
	}
	
}

	









	
	
//	@GetMapping("/logout")
//	public String logout() {
//		
//		Authentication auth = SecurityContextHolder	.getContext()
//				.getAuthentication();		
//		
//		auth.setAuthenticated(false);
//		
//		return "login";
//	}
	
	//Spring geht zuerst in addAttributes siehe Link
	// https://www.baeldung.com/spring-mvc-and-the-modelattribute-annotation
	
//	@ModelAttribute
//	public void addAttributes(Model model) {
//
//		Authentication authentication = SecurityContextHolder	.getContext()
//				.getAuthentication();		
//		
//		model.addAttribute("loginUserName", authentication.getName() == null ? "X" : authentication.getName());
//	}

