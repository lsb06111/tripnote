package edu.example.tripnote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.example.tripnote.dao.BoardDAO;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	BoardDAO boardDAO;
	
	@RequestMapping({"","/"})
	public String index() {
		return "board/index";
	}

}
