package de.ocplearn.hv.controller;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//import de.ocplearn.hv.service.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@GetMapping
	public String adminHome(Model model) {
		return "/admin/adminhome";
	}
	
	@GetMapping("/page3")
	public String adminPage3(Model model){
		return "/admin/page3";
	}
	

}
