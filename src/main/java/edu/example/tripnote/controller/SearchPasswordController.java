package edu.example.tripnote.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import edu.example.tripnote.service.PasswordResetService;

@Controller
@RequiredArgsConstructor
public class SearchPasswordController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/searchpassword")
    public String searchPassword(@RequestParam("find_username") String username,
                                 @RequestParam("find_email") String email,
                                 RedirectAttributes ra) {
        try {
            boolean result = passwordResetService.resetPasswordAndNotifyPlain(username, email);
            ra.addFlashAttribute("searchPassword", result ? "success" : "not_found");
        } catch (Exception e) {
            ra.addFlashAttribute("searchPassword", "error");
        }
        return "redirect:/";
    }
}
