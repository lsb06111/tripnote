package edu.example.tripnote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.example.tripnote.dao.AttractionsDAO;
import edu.example.tripnote.dao.TripDAO;
import edu.example.tripnote.domain.UserDTO;
import edu.example.tripnote.domain.trip.AttractionDTO;
import edu.example.tripnote.domain.trip.CourseDTO;
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
		return new UserDTO(2, "qwer", "qwer", "qwer@w.com", "이수빈", "짱짱맨", false);
	}
	
	@RequestMapping("")
	public String goTrip() {
		return "trip/index";
	}
	
	@RequestMapping("/plan")
	public String goPlan(TripInfoDTO tripInfoDTO,
						 @ModelAttribute CourseDTO courseDTO, @ModelAttribute("user") UserDTO user,
						 Model model) {
		
		
		courseDTO.setTitle("제목 없음");
		courseDTO.setStartDate(tripInfoDTO.getStartDate());
		courseDTO.setEndDate(tripInfoDTO.getEndDate());
		courseDTO.setUserId(user.getId());
		courseDTO.setAreaId(tripDao.getAreaId(tripInfoDTO.getTripDest())); // need to be changed.
		
		boolean isCreated = tripDao.insertCourse(courseDTO);
		if(!isCreated)
			System.out.println("insertion error 발생~~");
		
		model.addAttribute("tripInfoDTO", tripInfoDTO);
		List<AttractionDTO> list = attDao.getAttractionList(tripInfoDTO.getTripDest()); //Attractions.getAttractions(tripInfoDTO.getTripDest());
		List<AttractionDTO> list2 = attDao.getRestaurantList(tripInfoDTO.getTripDest()); //Attractions.getAttractions(tripInfoDTO.getTripDest());
		//List<RestaurantDTO> list3 = attDao.getCafeList(tripInfoDTO.getTripDest()); //Attractions.getAttractions(tripInfoDTO.getTripDest());
		
		model.addAttribute("attList", list);
		model.addAttribute("restaurantList", list2);
		model.addAttribute("friendRecList", list2);
		
		System.out.println(list.size());
		return "trip/index2";
	}
	
	@ResponseBody
	@RequestMapping("/saveTour")
	public TourLocDTO saveTour(TourLocDTO tourLocDTO) {
		tourLocDTO.setStartTime("09:00am");
		tourLocDTO.setEndTime("10:00am");
		tourLocDTO.setTourOrder(tourLocDTO.getTourOrder());
		tourLocDTO.setTourNth(tourLocDTO.getTourNth());
		System.out.println(tourLocDTO.getImgSrc());
		tripDao.insertTourLoc(tourLocDTO);
		return tourLocDTO;
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