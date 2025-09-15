package edu.example.tripnote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TripnoteController {
	@RequestMapping("/profile")
	public String tripnote() {
		return "profile";
	}
}
