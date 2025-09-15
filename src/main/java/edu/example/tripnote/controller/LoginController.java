package edu.example.tripnote.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.example.tripnote.dao.UserDAO;
import edu.example.tripnote.domain.UserDTO;

@Controller
public class LoginController {

    @Autowired
    private UserDAO dao;
 
    @PostMapping("/login")
    public String login(@RequestParam("login_username") String username,
                        @RequestParam("login_password") String password,
                        HttpSession session,
                        RedirectAttributes ra) {

        UserDTO dto = dao.login(username, password);
        
        if(dto == null) {
        	ra.addFlashAttribute("loginStatus", "fail");
        	return "redirect:/";
        }

        dto.setPassword(null);
        session.setAttribute("loginUser", dto);
        ra.addFlashAttribute("loginStatus", "success");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
