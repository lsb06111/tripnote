package edu.example.tripnote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.example.tripnote.dao.UserDAO;
import edu.example.tripnote.domain.UserDTO;

@Controller
public class RegisterController {

    @Autowired
    private UserDAO dao;

    @PostMapping("/register")
    public String register(UserDTO dto, RedirectAttributes ra) {
        try {
            int rows = dao.insertUser(dto);   // 여기서 제약 위반 시 예외 발생 가능

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
