package edu.example.tripnote.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.example.tripnote.dao.TripnoteDAO;
import edu.example.tripnote.domain.trip.TourLocDTO;

@Controller
public class TripnoteController {
	@Autowired
	TripnoteDAO dao;

	@RequestMapping("/details")
	public String tripnote(HttpSession session, Model model, Long courseId) {
	    List<TourLocDTO> getAllList = dao.getAllTourLoc(courseId);
	    model.addAttribute("getAllList", getAllList);
	    
	    
	    return "details";
	}
}

