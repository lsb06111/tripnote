package edu.example.tripnote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.example.tripnote.dao.UserDAO;

@Controller
public class SearchUsernameController {
	@Autowired
	UserDAO dao;
	
	@PostMapping("/searchusername")
	public String searchusername(@RequestParam("find_email") String email,
								RedirectAttributes ra) {
		
		String result = dao.searchUsername(email);
		
		if(result == null) {
			ra.addFlashAttribute("searchUsername", "fail");
			return "redirect:/";
		}
		ra.addFlashAttribute("username", result);
		ra.addFlashAttribute("searchUsername", "success");
		return "redirect:/";
	}
}
