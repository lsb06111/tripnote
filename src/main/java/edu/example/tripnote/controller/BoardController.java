package edu.example.tripnote.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardVO;
import edu.example.tripnote.service.BoardService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;
	
	@RequestMapping({"","/"})
	public String index(Model model) {
		List<BoardDTO> list =  boardService.listAll();
		model.addAttribute("list", list);
		return "board/index";
	}

}
