package de.ocplearn.hv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String home(Model model) {
		
		List<LoginUser> allLoginUsers = userService.findAllByRole(Role.ADMIN);
		model.addAttribute("LoginUserNamen", allLoginUsers);

		return "home";
	}
	

}
