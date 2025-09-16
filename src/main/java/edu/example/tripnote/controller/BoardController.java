package edu.example.tripnote.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.example.tripnote.dao.CourseDAO;
import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardFormReqDTO;
import edu.example.tripnote.domain.board.BoardParamDTO;
import edu.example.tripnote.domain.board.BoardSaveReqDTO;
import edu.example.tripnote.domain.board.BoardTemplateDTO;
import edu.example.tripnote.domain.board.CourseSelectResDTO;
import edu.example.tripnote.domain.board.PageResponseDTO;
import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;
import edu.example.tripnote.service.AreaService;
import edu.example.tripnote.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	public List<CourseSelectResDTO> getCourse() {
		int id = 1;
		List<CourseSelectResDTO> myCourses = courseDAO.getCourses(id);
		return myCourses;
	}
	
	@PostMapping("/form")
	public String form(BoardFormReqDTO reqDTO, Model model) {
		BoardTemplateDTO locTemplate = new BoardTemplateDTO();
		CourseDTO courseDTO = courseDAO.getCourseById(reqDTO.getCourseId());
		courseDTO.setStartDate(courseDTO.getStartDate().substring(0, 10));
		courseDTO.setEndDate(courseDTO.getEndDate().substring(0, 10));
		locTemplate.setCourse(courseDTO);
		
        List<List<TourLocDTO>> tourlocs = new ArrayList<>(
        		courseDAO.getTourLocsByCourseId(reqDTO.getCourseId()).stream()
                     .collect(Collectors.groupingBy(TourLocDTO::getTourNth))
                     .values()
            );
        
        locTemplate.setTourlocs(tourlocs);
        model.addAttribute("locTemplate",locTemplate);
		return "board/form";
	}
	
	@ResponseBody
	@PostMapping("/form/save")
	public ResponseEntity<String> save(@RequestBody BoardSaveReqDTO req) {
		boolean result = boardService.save(req);
		if (result) {
		    return ResponseEntity.ok("success");
		} else {
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
		}
	}
	
	@ResponseBody
	@PostMapping("/form/draft")
	public ResponseEntity<String> saveDraft(@RequestBody BoardSaveReqDTO req) {
		boolean result = boardService.saveDraft(req);
		if (result) {
			return ResponseEntity.ok("success");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
		}
	}
	
	@GetMapping("/detail")
	public String detail(Model model) {
		return "board/view";
	}

}
