package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.example.tripnote.domain.trip.AttractionDTO;

@Mapper
public interface AttractionsDAO {

	public boolean insert(AttractionDTO dto);
	
	public boolean insertRestaurant(AttractionDTO dto);
	
	public boolean insertHotel(AttractionDTO dto);
	
	public List<AttractionDTO> getAttractionList(String tripDest);
	
	public List<AttractionDTO> getRestaurantList(String tripDest);
	
	public List<AttractionDTO> getHotelList(String tripDest);
	
	public List<AttractionDTO> getAttRecList(@Param("tripDest") String tripDest, @Param("id") int id);
	public List<AttractionDTO> getResRecList(@Param("tripDest") String tripDest, @Param("id") int id);
	public List<AttractionDTO> getHotRecList(@Param("tripDest") String tripDest, @Param("id") int id);
	
	//...
	public List<AttractionDTO> getAtt();
	public List<AttractionDTO> getRes();
	public List<AttractionDTO> getHot();
	
	public int updateAtt(AttractionDTO dto);
	public int updateRes(AttractionDTO dto);
	public int updateHot(AttractionDTO dto);
	
}
