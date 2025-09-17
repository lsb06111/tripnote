package edu.example.tripnote.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.example.tripnote.dao.ProfileListDAO;
import edu.example.tripnote.domain.UserDTO;
import edu.example.tripnote.domain.trip.AreaVO;
import edu.example.tripnote.domain.trip.CourseDTO;

@Controller
public class ProfileListController {
	
	@Autowired
	ProfileListDAO dao;
	
	
	@RequestMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
		UserDTO userDTO = (UserDTO) session.getAttribute("loginUser");
		List<CourseDTO> courses = dao.getAllCourse(userDTO.getId());
		List<String> nBaks = new ArrayList<>();
		List<AreaVO> areas = new ArrayList<>();
		
		DateTimeFormatter formatter;

		for(CourseDTO c : courses) {
			areas.add(dao.getArea(c.getAreaId()));
			
			formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String startStr = c.getStartDate();
		    String endStr = c.getEndDate();

		    LocalDate start = LocalDate.parse(startStr, formatter);
		    LocalDate end = LocalDate.parse(endStr, formatter);

	        int totalDays = (int)ChronoUnit.DAYS.between(start, end)+1;
	        nBaks.add((totalDays-1)+"박 "+totalDays+"일");
			
		}
		
		model.addAttribute("courses", courses);
		model.addAttribute("areas", areas);
		model.addAttribute("nBaks", nBaks);
		
		return "profile";
	}
	

}
