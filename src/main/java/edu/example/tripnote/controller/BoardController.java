package edu.example.tripnote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board2")
public class BoardController {
	
	@RequestMapping({"","/"})
	public String m() {
		return "board/index";
	}

}
