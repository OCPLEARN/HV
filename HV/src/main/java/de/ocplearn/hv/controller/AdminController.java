package de.ocplearn.hv.controller;

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

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.UserService;


@Controller
@RequestMapping("/admin/")
public class AdminController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public String home(HttpServletRequest request, Model model) {
		
		HttpSession session = request.getSession();
		
    	// #####################################################################
		Map<String, String[]> map = request.getParameterMap();
		String beginIndex = "0";
		String rowCount = "20";
		if ( map.containsKey("beginIndex") ) { 
			beginIndex = map.get("beginIndex")[0];
		}else {
			if ( session.getAttribute("users.beginIndex") != null ) { beginIndex = (String)session.getAttribute("users.beginIndex"); } else { beginIndex = "0"; }
			
		}
		session.setAttribute("users.beginIndex", beginIndex);
		
		if ( map.containsKey("rowCount") ) {
			rowCount = map.get("rowCount")[0];
		}else {
			if ( session.getAttribute("users.rowCount") != null ) { rowCount = (String)session.getAttribute("users.rowCount"); } else { rowCount = "20"; }
		}
		session.setAttribute("users.rowCount", rowCount);
		
		
		// /home?vUsers=Next
		String vUsers = "next";
		if (  map.containsKey("vUsers") && map.get("vUsers")[0] != null ) {
			vUsers = map.get("vUsers")[0];
		}
    	
		System.out.println("-- nav -- vUsers = " + vUsers);
		
		int rowCountInt = Integer.valueOf(rowCount);
		
		switch( vUsers ) {
			case "next": {
			}
			case "next1": {
				int beginIndexInt = Integer.valueOf(beginIndex); 
				beginIndex = "" + (beginIndexInt + rowCountInt);
				System.out.println("-- nav -- beginIndex NEW = " + beginIndex);
				break;
			}	
			case "next2": {
				int beginIndexInt = Integer.valueOf(beginIndex); 
				beginIndex = "" + (beginIndexInt +  2 * rowCountInt);
				System.out.println("-- nav -- beginIndex NEW = " + beginIndex);
				break;
			}	
			case "next3": {
				int beginIndexInt = Integer.valueOf(beginIndex); 
				beginIndex = "" + (beginIndexInt + 3 * rowCountInt);
				System.out.println("-- nav -- beginIndex NEW = " + beginIndex);
				break;
			}			
			case "prev": {
				int beginIndexInt = Integer.valueOf(beginIndex); 
				if ( (beginIndexInt - rowCountInt) < 0 ) {
					beginIndexInt = 0; beginIndex = "0";
				} 
				else {
					beginIndex = "" + (beginIndexInt - rowCountInt);	
				}
				System.out.println("-- nav -- prev, beginIndex NEW = " + beginIndex + ", rowCount = " + rowCount);
				break;
			}
			case "selectRCount":{
				
				if ( map.containsKey("inlineFormCustomSelect") ) { 
					rowCount = map.get("inlineFormCustomSelect")[0];
				}
				System.out.println("-- nav -- selectRCount = " + rowCount);
				session.setAttribute("users.rowCount", rowCount);
				break;
			}
		}
		session.setAttribute("users.beginIndex", beginIndex);
		System.out.println("-- nav -- beginIndex = " + beginIndex + " ");
		
    	//DBConnectionPool pool = DBConnectionPool.getInstance();
    	//Connection con = pool.getConnection();
    	List<LoginUserDto> users = userService.findAllByRole(Role.ADMIN) ;
    			//userService.getAllLoginUsers();

    	
    	
    	// #####################################################################		

        model.addAttribute("msgInfo","-");
        model.addAttribute("beginIndex",beginIndex);
        model.addAttribute("rowCount",rowCount);
        model.addAttribute("ld",LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        model.addAttribute("loginUsers",users);
        
		return "admin/adminhome";
	}	
	
}
