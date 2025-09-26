package edu.example.tripnote.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import edu.example.tripnote.dao.ProfileDAO;
import edu.example.tripnote.domain.UserDTO;
import edu.example.tripnote.domain.profile.BoardCourseDTO;
import edu.example.tripnote.domain.trip.AreaVO;
import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.CourseIconDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;

@Controller
public class ProfileController {
	
	@Autowired
	ProfileDAO dao;
	
	
	@RequestMapping("/profile")
	public String profile(@RequestParam("username") String username,
							@SessionAttribute("loginUser") UserDTO loginUser,
							Model model) {
		UserDTO userDTO = dao.getUser(username);
		List<CourseDTO> courses = dao.getAllCourse(userDTO.getId());
		List<String> nBaks = new ArrayList<>();
		List<AreaVO> areas = new ArrayList<>();
		List<String> icons = new ArrayList<>();
		DateTimeFormatter formatter;
		List<UserDTO> followers = dao.getFollowers(userDTO.getId());
		List<UserDTO> followings = dao.getFollowings(userDTO.getId());
		List<UserDTO> myFollowings = dao.getFollowings(loginUser.getId());
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
		model.addAttribute("followers", followers);
		model.addAttribute("followings", followings);
		model.addAttribute("myFollowings", myFollowings);
		return "profile";
	}
	
	@RequestMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
		UserDTO userDTO = (UserDTO) session.getAttribute("loginUser");
		List<CourseDTO> courses = dao.getAllCourse(userDTO.getId());
		List<String> nBaks = new ArrayList<>();
		List<AreaVO> areas = new ArrayList<>();
		List<String> icons = new ArrayList<>();
		List<BoardCourseDTO> boardCourseIds = dao.getCourseBoardIds(userDTO.getId());
		HashMap<Integer, Integer> bc = new HashMap<>();
		List<Integer> boardIds = new ArrayList<>();
		List<Boolean> writtens = new ArrayList<>();
		List<UserDTO> followers = dao.getFollowers(userDTO.getId());
		List<UserDTO> followings = dao.getFollowings(userDTO.getId());
		System.out.println(followings);
		DateTimeFormatter formatter;
		List<UserDTO> myFollowings = dao.getFollowings(userDTO.getId());
		for(BoardCourseDTO bcDto : boardCourseIds)
			bc.put(bcDto.getCourseId(), bcDto.getId());

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
		        if(bc.keySet().contains(c.getId().intValue())) {
		        	writtens.add(true);
		        	boardIds.add(bc.get(c.getId().intValue()));
		        }else {
		        	writtens.add(false);
		        	boardIds.add(0);
		        }
		}
		
		model.addAttribute("courses", courses);
		model.addAttribute("areas", areas);
		model.addAttribute("nBaks", nBaks);
		model.addAttribute("icons", icons);
		model.addAttribute("writtens", writtens);
		model.addAttribute("boardIds", boardIds);
		model.addAttribute("followers", followers);
		model.addAttribute("followings", followings);
		model.addAttribute("myFollowings", myFollowings);
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
	@RequestMapping("/updateInfo")
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
	@RequestMapping("/findUser")
	public List<UserDTO> findUser(String target, HttpSession session) {
		int curUserId = ((UserDTO)session.getAttribute("loginUser")).getId();
		return dao.findUsers(target, curUserId);
	}
	
	@ResponseBody
	@RequestMapping("/saveProfileImg")
	public ResponseEntity<String> saveProfileImg(@SessionAttribute("loginUser") UserDTO loginUser,
												@RequestParam MultipartFile imgFile){
		
		String savedPath = saveImgToFolder(imgFile);
		dao.updateProfileImg(loginUser.getId(), savedPath);
		loginUser.setProfileImage(savedPath);
		return ResponseEntity.ok("success");
	}
	
	private String saveImgToFolder(MultipartFile file) {
		String path = "c:/tripnote_resource/profile_imgs/";
		File isDir = new File(path);
		if (!isDir.isDirectory()) { // 폴더 없으면 생성
			isDir.mkdirs();
		}
		String filename = UUID.randomUUID() + file.getOriginalFilename();
		File dest = new File(path + filename);
		try {
			file.transferTo(dest);
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}
		return "/tripnote_resource/profile_imgs/" + filename; // DB에 저장할 접근 경로
	}
	
	@ResponseBody
	@RequestMapping("/getAllTourList")
	public List<TourLocDTO> getAllTourList(int id){
		return dao.getAllTourLoc(id);
	}
	
	@ResponseBody
	@RequestMapping("/updateRecommend")
	public String updateRecommend(int id) {
		TourLocDTO dto = dao.getTourLoc(id);
		if(dto.isRecommend())
			dto.setRecommend(false);
		else
			dto.setRecommend(true);
		dao.updateRecommend(dto);
		
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/deleteCourse")
	public String deleteCourse(int id) {
		dao.deleteCourse(id);
		
		return "ok";
	}
	
	
	

}
