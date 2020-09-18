package de.ocplearn.hv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.service.UserServiceImpl;

@Controller
public class HomeController {

	//private UserService userService = new UserServiceImpl();
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String home(Model model) {
		
		Authentication auth = SecurityContextHolder	.getContext()
													.getAuthentication();
		model.addAttribute("loginUserName", auth.getName());
		
		List<LoginUserDto> allLoginUsers = userService.findAllByRole(Role.ADMIN);
		model.addAttribute("LoginUserNamen", allLoginUsers);

		return "home";
	}
	

}
