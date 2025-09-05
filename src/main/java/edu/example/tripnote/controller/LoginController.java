package edu.example.tripnote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@RequestMapping("/board")
	public String test( ) {
		return "board/index";
	}
}
