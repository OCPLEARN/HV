package de.ocplearn.hv.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.service.UserServiceImpl;
import de.ocplearn.hv.util.CountryList;
import de.ocplearn.hv.util.RegistrationObject;

@Controller
public class HomeController {

	private UserService userService;
	
	private ApplicationContext applicationContext;
	
	@Autowired
	public HomeController(UserService userService, ApplicationContext applicationContext) {
		this.userService = userService;
		this.applicationContext = applicationContext;
	}
	
	
	@GetMapping("/")
	public String home(Model model) {
		return "public/home";
	}
	
	
	@GetMapping("/page2")
	public String page2() {
		return "/public/page2";
	}
	
	
	@GetMapping("/signin")
	public String signIn() {
		return "/public/signin";
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
		
		return "/public/register";
	}
	
	@PostMapping("/register")
	public String createLoginUserDto(
					@Valid @ModelAttribute ("PropertyManagementRegistrationFormCommand") PropertyManagementRegistrationFormCommand propertyManagementRegistrationFormCommand,
					BindingResult bindingResult,
					Model model) {
				
		if ( !( propertyManagementRegistrationFormCommand.getInitialPassword().equals(propertyManagementRegistrationFormCommand.getRepeatedPassword())) ) 
			bindingResult.rejectValue("repeatedPassword", "register.password.validation.repeaterror", "Repeated password doesn´t match first password.");
		
		//return "/public/register";
		
		if(bindingResult.hasErrors()) {
				return 	"/public/register";
				
		} 	else {
					createPropertyManagement(propertyManagementRegistrationFormCommand);
				return "/public/signin";
		}
	}
	
	private void createPropertyManagement(PropertyManagementRegistrationFormCommand propertyManagementRegistrationFormCommand) {
		LoginUserDto loginUserDto = new LoginUserDto();
		loginUserDto.setLoginUserName(propertyManagementRegistrationFormCommand.getLoginUserName());
		userService.createUser(loginUserDto, propertyManagementRegistrationFormCommand.getInitialPassword());
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

