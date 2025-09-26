package edu.example.tripnote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.example.tripnote.domain.UserDTO;
import edu.example.tripnote.service.UserService;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(UserDTO dto, RedirectAttributes ra) {
        try {
            int rows = userService.encryptInsert(dto);
            
            if (rows > 0) {
                ra.addFlashAttribute("registerStatus", "success");
            } else {
                ra.addFlashAttribute("registerStatus", "fail");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("registerStatus", "fail");
        }
        return "redirect:/";
    }

}
