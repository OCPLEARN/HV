package de.ocplearn.hv.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

//@Controller
//public class LoginController {
//	
//	@GetMapping("/signin")
//	public String signIn() {
//		return "/signin/signin";
//	}
//	
//	@ModelAttribute
//	public void addAttributes(Model model) {
//
//		Authentication authentication = SecurityContextHolder	.getContext()
//				.getAuthentication();		
//		
//		model.addAttribute("loginUserName", authentication.getName() == null ? "X" : authentication.getName());
//	}
//}
