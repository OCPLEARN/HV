package de.ocplearn.hv.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.service.UserServiceImpl;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;
	
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
		return "/signin/signin";
	}
	
	@ModelAttribute
	public void addAttributes(Model model) {

		Authentication authentication = SecurityContextHolder	.getContext()
				.getAuthentication();		
		
		model.addAttribute("loginUserName", authentication.getName() == null ? "X" : authentication.getName());
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

