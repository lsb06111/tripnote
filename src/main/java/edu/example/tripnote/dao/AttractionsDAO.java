package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.trip.AttractionDTO;
import edu.example.tripnote.domain.trip.RestaurantDTO;

@Mapper
public interface AttractionsDAO {

	public boolean insert(AttractionDTO dto);
	
	public boolean insertRestaurant(AttractionDTO dto);
	
	public boolean insertHotel(AttractionDTO dto);
	
	public List<AttractionDTO> getAttractionList(String tripDest);
	
	public List<AttractionDTO> getRestaurantList(String tripDest);
	
	public List<AttractionDTO> getHotelList(String tripDest);
	
	
	
}
