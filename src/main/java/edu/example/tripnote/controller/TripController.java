package edu.example.tripnote.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.example.tripnote.dao.AttractionsDAO;
import edu.example.tripnote.domain.AttractionDTO;
import edu.example.tripnote.domain.TripInfoDTO;
import edu.example.tripnote.util.Attractions;

@Controller
@RequestMapping("/trip")
public class TripController {

	@Autowired
	private AttractionsDAO attDao;
	
	@RequestMapping("")
	public String goTrip() {
		return "trip/index";
	}
	
	@RequestMapping("/plan")
	public String goPlan(TripInfoDTO tripInfoDTO, Model model) {
		
		model.addAttribute("tripInfoDTO", tripInfoDTO);
		List<AttractionDTO> list = attDao.getAttractionList(tripInfoDTO.getTripDest()); //Attractions.getAttractions(tripInfoDTO.getTripDest());
		model.addAttribute("attList", list);
		System.out.println(list.size());
		return "trip/index2";
	}
	
	@RequestMapping("/testtrip")
	public String test() {
		System.out.println(Attractions.getAvailableDestinations());
		//List<String> attractionList = Attractions.getAvailableDestinations();
		//after getting list
		/*
		for(String att : attractionList) {
			List<AttractionDTO> dtos = Attractions.getAttractions(att);
			
			//for each dto, insert data
			for(AttractionDTO dto : dtos) {
				attDao.insert(dto);
			}
				
		}
		*/
		return "trip/index";
	}
	
	
}