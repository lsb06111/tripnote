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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.example.tripnote.dao.ProfileListDAO;
import edu.example.tripnote.domain.UserDTO;
import edu.example.tripnote.domain.trip.AreaVO;
import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.CourseIconDTO;

@Controller
public class ProfileListController {
	
	@Autowired
	ProfileListDAO dao;
	
	
	@RequestMapping("/profile")
	public String profile(@RequestParam("username") String username,
							Model model) {
		UserDTO userDTO = dao.getUser(username);
		List<CourseDTO> courses = dao.getAllCourse(userDTO.getId());
		List<String> nBaks = new ArrayList<>();
		List<AreaVO> areas = new ArrayList<>();
		List<String> icons = new ArrayList<>();
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
	        icons.add(dao.getIcon(c.getId()));
		}
		
		model.addAttribute("courses", courses);
		model.addAttribute("areas", areas);
		model.addAttribute("nBaks", nBaks);
		model.addAttribute("icons", icons);
		model.addAttribute("profileUser", userDTO);
		return "profile";
	}
	
	@RequestMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
		UserDTO userDTO = (UserDTO) session.getAttribute("loginUser");
		List<CourseDTO> courses = dao.getAllCourse(userDTO.getId());
		List<String> nBaks = new ArrayList<>();
		List<AreaVO> areas = new ArrayList<>();
		List<String> icons = new ArrayList<>();
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
	        icons.add(dao.getIcon(c.getId()));
		}
		
		model.addAttribute("courses", courses);
		model.addAttribute("areas", areas);
		model.addAttribute("nBaks", nBaks);
		model.addAttribute("icons", icons);
		
		return "profile";
	}
	
	@ResponseBody
	@RequestMapping("/saveIcon")
	public CourseIconDTO saveIcon(CourseIconDTO courseIconDTO) {
		String iconName = dao.getIcon(courseIconDTO.getCourseId());
		
		if(iconName == null)
			dao.insertIcon(courseIconDTO);
		else
			dao.updateIcon(courseIconDTO);
		
		return courseIconDTO;
	}
	
	@ResponseBody
	@RequestMapping("updateTitle")
	public String updateTitle(CourseDTO courseDTO) {
		dao.updateTitle(courseDTO);
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("updateInfo")
	public String updateInfo(UserDTO userDTO, HttpSession session) {
		UserDTO loginUser = (UserDTO)session.getAttribute("loginUser");
		if(!userDTO.getPassword().isEmpty()) {
			dao.updatePassword(userDTO);
			loginUser.setPassword(userDTO.getPassword());
		}
		if(!userDTO.getEmail().isEmpty()) {
			dao.updateEmail(userDTO);
			loginUser.setEmail(userDTO.getEmail());
		}
		if(!userDTO.getNickname().isEmpty()) {
			dao.updateNickname(userDTO);
			loginUser.setNickname(userDTO.getNickname());
		}
		
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("findUser")
	public List<UserDTO> findUser(String target, HttpSession session) {
		int curUserId = ((UserDTO)session.getAttribute("loginUser")).getId();
		return dao.findUsers(target, curUserId);
	}

}
