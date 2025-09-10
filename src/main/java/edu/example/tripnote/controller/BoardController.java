package edu.example.tripnote.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.example.tripnote.domain.PageResponseDTO;
import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardParamDTO;
import edu.example.tripnote.service.BoardService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;
	
	@GetMapping
	public String index(Model model,  BoardParamDTO boardParam) {
		PageResponseDTO<BoardDTO> response =  boardService.listAll(boardParam);
		model.addAttribute("response", response);
		
		return "board/index";
	}
	
	@GetMapping("/form")
	public String form() {
		
		return "board/write";
	}
	
	@GetMapping("/detail")
	public String detail(Model model) {
		
		return "board/view";
	}

}
