package edu.example.tripnote.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.example.tripnote.dao.CourseDAO;
import edu.example.tripnote.domain.PageResponseDTO;
import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardParamDTO;
import edu.example.tripnote.domain.board.CourseSelectRsps;
import edu.example.tripnote.service.AreaService;
import edu.example.tripnote.service.BoardService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;
	private final AreaService areaService;
	private final CourseDAO courseDAO;
	
	@GetMapping
	public String index(Model model,  BoardParamDTO boardParam) {
		model.addAttribute("locList" ,areaService.listParentLoc());
		PageResponseDTO<BoardDTO> response =  boardService.listAll(boardParam);
		model.addAttribute("response", response);
		return "board/index";
	}
	@ResponseBody
	@GetMapping("/mycourse")
	public List<CourseSelectRsps> getCourse() {
		int id = 1;
		List<CourseSelectRsps> response = courseDAO.getCourses(id);
		return response;
	}
	
	@GetMapping("/form")
	public String form() {
		
		return "board/form";
	}
	
	@GetMapping("/detail")
	public String detail(Model model) {
		
		return "board/view";
	}

}
