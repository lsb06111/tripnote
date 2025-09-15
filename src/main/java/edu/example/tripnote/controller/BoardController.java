package edu.example.tripnote.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.example.tripnote.dao.CourseDAO;
import edu.example.tripnote.dao.TripDAO;
import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardFormReqDTO;
import edu.example.tripnote.domain.board.BoardParamDTO;
import edu.example.tripnote.domain.board.BoardSaveReqDTO;
import edu.example.tripnote.domain.board.BoardTemplateDTO;
import edu.example.tripnote.domain.board.CourseSelectResDTO;
import edu.example.tripnote.domain.board.NewBoardDTO;
import edu.example.tripnote.domain.board.PageResponseDTO;
import edu.example.tripnote.domain.board.ReviewContentDTO;
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
		locTemplate.setCourse(courseDAO.getCourseById(reqDTO.getCourseId()));
		
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
		//boardService.save(req);
		boardService.saveTest(req);
		return ResponseEntity.ok("success");
	}
	
	@GetMapping("/detail")
	public String detail(Model model) {
		return "board/view";
	}

}
