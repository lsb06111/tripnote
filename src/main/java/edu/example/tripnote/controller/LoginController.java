package edu.example.tripnote.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.example.tripnote.domain.UserDTO;
import edu.example.tripnote.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService passwordEncryptionService;
    
 
    @PostMapping("/login")
    public String login(@RequestParam("login_username") String username,
                        @RequestParam("login_password") String password,
                        @RequestHeader(value = "Referer", required = false) String referer,
                        HttpSession session,
                        RedirectAttributes ra) {

        UserDTO dto = passwordEncryptionService.login(username, password);
        
        if(dto == null) {
        	ra.addFlashAttribute("loginStatus", "fail");
        	return "redirect:/";
        }

        dto.setPassword(null);
        session.setAttribute("loginUser", dto);
        ra.addFlashAttribute("loginStatus", "success");
        if (referer == null || referer.trim().isEmpty()) {
            referer = "/";
        }
        return "redirect:" + referer;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}
