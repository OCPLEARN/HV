package de.ocplearn.hv.controller;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//import de.ocplearn.hv.service.UserService;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.WebContext;

import de.ocplearn.hv.dao.LoginUserDao;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {

	private UserService userService;
	
	@Autowired
	public AdminController(UserService userService) {
		this.userService = userService;
	}
		
	@GetMapping("/users")
	public String startUserList(Model model) {
		
		return pagingUserList(model, 1, "loginUserName", "ASC");
	}
	
	@GetMapping("/users/{pageNumber}")
	public String pagingUserList(Model model,
							@PathVariable(value = "pageNumber") int pageNumber, 
							@RequestParam("sortField") String sortField,
							@RequestParam("sortDir") String sortDir) {
		
		int pageSize = 10;
		int totalCountLoginUser = this.userService.getLoginUserCount();
		int totalPages = ((totalCountLoginUser % pageSize) == 0)? (totalCountLoginUser / pageSize): (totalCountLoginUser / pageSize)+1;
		int offset = (pageNumber * pageSize) - pageSize;

		List<LoginUserDto> loginUserDtos = this.userService.findAllLoginUsers(offset, pageSize, sortField, sortDir);
		
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", totalCountLoginUser);
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc" );
		
		model.addAttribute("loginUsers", loginUserDtos);		
		
		return "/admin/users";
	}
	
	@GetMapping("/page3")
	public String adminPage3(){
		return "/admin/page3";
	}
	
	@GetMapping("/users/delete")
	public String deleteUserFromList( @RequestParam("loginusername") String loginUserName) {
		
		String msg;
//		Infos und Warnmeldungen für die Infoschaltfläche in der Navbar	
//		msg = "msg=neutrale Info";
//		msg = "msgWarn=Warnung";
		
		if (userService.deleteUser(loginUserName)) {
			msg = "msgSuccess=User gelöscht!";
		} else {
			msg = "msgFail=User konnte nicht gelöscht werden.";
		}
		return "redirect:/admin/users?" + msg; 
	}
	
	@GetMapping("/users/edit")
	public String editUserFromList( @RequestParam("loginusername") String loginUserName, Model model) {
		
		LoginUserDto loginUserDto = userService.findUserByLoginUserName(loginUserName);
		model.addAttribute("loginUser", loginUserDto); //Definition of variable for Thymeleaf
		
		
		return "/admin/usersedit";
		
	}
	
	@PostMapping("/users/save")
	public String saveUserFromList(@ModelAttribute("loginUser") LoginUserDto loginUserDto) {
		
		System.out.println(loginUserDto.getRole());
		
		
		String msg;
//		Infos und Warnmeldungen für die Infoschaltfläche in der Navbar	
//		msg = "msg=neutrale Info";
//		msg = "msgWarn=Warnung";
		
		if (userService.updateUser(loginUserDto)) {
			msg = "msgSuccess=User wurde aktualisiert!";
		} else {
			msg = "msgFail=User konnte nicht aktualisiert werden.";
		}
		return "redirect:/admin/users?" + msg; 
		
	
	}
	
	
	
}
