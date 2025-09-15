package edu.example.tripnote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.example.tripnote.dao.AttractionsDAO;
import edu.example.tripnote.domain.trip.AttractionDTO;
import edu.example.tripnote.util.Attractions;

@Controller
public class TestDBController {

	@Autowired
	AttractionsDAO attDao;
	
	@RequestMapping("/tripdb")
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
