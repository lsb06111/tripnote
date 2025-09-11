package edu.example.tripnote.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.example.tripnote.dao.AttractionsDAO;
import edu.example.tripnote.dao.TripDAO;
import edu.example.tripnote.domain.UserDTO;
import edu.example.tripnote.domain.trip.AttractionDTO;
import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.NoteDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;
import edu.example.tripnote.domain.trip.TripInfoDTO;
import edu.example.tripnote.util.Attractions;

@Controller
@RequestMapping("/trip")
@SessionAttributes("user")
public class TripController {

	@Autowired
	private AttractionsDAO attDao;
	
	@Autowired
	private TripDAO tripDao;
	
	@ModelAttribute("user")
	public UserDTO makeUser() {
		return new UserDTO(1, "qwer", "qwer", "qwer@w.com", "이수빈", "짱짱맨", false);
	}
	
	@RequestMapping("")
	public String goTrip(@ModelAttribute("user") UserDTO userDTO) {
		return "trip/index";
	}
	
	@RequestMapping("/plan")
	public String goPlan(TripInfoDTO tripInfoDTO,
						 @ModelAttribute CourseDTO courseDTO, int userId,
						 String courseTitle,
						 RedirectAttributes redirectAttributes,
						 HttpSession session) {
		
		if(session.getAttribute("courseDTO") != null)
			session.removeAttribute("courseDTO");
		courseDTO.setTitle(courseTitle);
		courseDTO.setStartDate(tripInfoDTO.getStartDate());
		courseDTO.setEndDate(tripInfoDTO.getEndDate());
		courseDTO.setUserId(userId);
		System.out.println(userId);
		courseDTO.setAreaId(tripDao.getAreaId(tripInfoDTO.getTripDest())); // need to be changed.
		
		boolean isCreated = tripDao.insertCourse(courseDTO);
		if(!isCreated)
			System.out.println("insertion error 발생~~");
		redirectAttributes.addAttribute("tripDest", tripInfoDTO.getTripDest());
	    redirectAttributes.addAttribute("startDate", tripInfoDTO.getStartDate());
	    redirectAttributes.addAttribute("endDate", tripInfoDTO.getEndDate());
	    redirectAttributes.addFlashAttribute("courseDTO", courseDTO);
		return "redirect:/trip/plan/result";
	}
	
	@GetMapping("/plan/result")
	public String showPlanResult(@RequestParam String tripDest,
	                             @RequestParam String startDate,
	                             @RequestParam String endDate,
	                             CourseDTO courseDTOFromFlash,
	                             HttpSession session,
	                             Model model) {

	    TripInfoDTO tripInfoDTO = new TripInfoDTO();
		tripInfoDTO.setTripDest(tripDest);
		tripInfoDTO.setStartDate(startDate);
		tripInfoDTO.setEndDate(endDate);

	    CourseDTO courseInSession = (CourseDTO) session.getAttribute("courseDTO");
	    
	    CourseDTO finalCourseDTO;

	    if (courseInSession != null) {
	        finalCourseDTO = courseInSession;
	    } else {
	        finalCourseDTO = courseDTOFromFlash;
	        session.setAttribute("courseDTO", finalCourseDTO);
        }
	    
	    List<AttractionDTO> list = attDao.getAttractionList(tripDest);
		List<AttractionDTO> list2 = attDao.getRestaurantList(tripDest);

	    model.addAttribute("tripInfoDTO", tripInfoDTO);
	    model.addAttribute("attList", list);
		model.addAttribute("restaurantList", list2);
		model.addAttribute("friendRecList", list2);
	    model.addAttribute("courseDTO", finalCourseDTO); 

	    return "trip/index2";
	}
	
	@ResponseBody
	@RequestMapping("/getAllTour")
	public List<TourLocDTO> getAllTour(int courseId){
		return tripDao.getAllTourLoc(courseId);
	}
	
	
	@ResponseBody
	@RequestMapping("/saveTour")
	public TourLocDTO saveTour(TourLocDTO tourLocDTO) {
		System.out.println("courseId "+tourLocDTO.getCourseId());
		tourLocDTO.setStartTime("09:00am");
		tourLocDTO.setEndTime("10:00am");
		tourLocDTO.setTourOrder(tourLocDTO.getTourOrder());
		tourLocDTO.setTourNth(tourLocDTO.getTourNth());
		System.out.println(tourLocDTO.getImgSrc());
		tripDao.insertTourLoc(tourLocDTO);
		tripDao.insertTourType(tourLocDTO);
		
		NoteDTO noteDTO = new NoteDTO();
		noteDTO.setTourLocId(tourLocDTO.getId());
		tripDao.insertNote(noteDTO);
		return tourLocDTO;
	}
	
	@ResponseBody
	@RequestMapping("/saveNote")
	public String saveNote(int tourLocId, String noteContent) {
		NoteDTO noteDTO = tripDao.getNote(tourLocId);
		noteDTO.setNoteContent(noteContent);
		tripDao.updateNote(noteDTO);
		return "ok";
	}
	
	
	@PostMapping("/updateTour")
	@ResponseBody
	public String updateTour(@RequestBody List<TourLocDTO> dtoList) {
		for(TourLocDTO dto : dtoList) {
			tripDao.updateTourLoc(dto);
		}
		
		return "ok";
	}
	
	@GetMapping("/deleteTour")
	@ResponseBody
	public String deleteTour(int id) {
		tripDao.deleteTourLoc(id);
		return "ok";
	}
	
	@RequestMapping("/testtrip")
	public String test() {
		System.out.println(Attractions.getAvailableDestinations());
		List<String> attractionList = Attractions.getAvailableDestinations();
		//after getting list
		String cCode = "39"; // 12 관광지 / 39 음식점
		for(String att : attractionList) {
			List<AttractionDTO> dtos = Attractions.getAttractions(att, cCode);
			
			//for each dto, insert data
			for(AttractionDTO dto : dtos) {
				if(cCode.equals("12"))
					attDao.insert(dto);
				else
					attDao.insertRestaurant(dto);
			}
				
		}
		
		return "trip/index";
	}
	
	
}