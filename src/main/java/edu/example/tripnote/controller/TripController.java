package edu.example.tripnote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/trip")
public class TripController {

	@RequestMapping("")
	public String goTrip() {
		return "trip/index";
	}
	
	@RequestMapping("/plan")
	public String goPlan(@RequestParam String startDate,
						 @RequestParam String endDate,
						 @RequestParam String tripDest,
						 Model model) {
		model.addAttribute("startDate", startDate);
		
		return "trip/index2";
	}
}
