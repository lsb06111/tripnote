package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.NoteDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;

@Mapper
public interface TripDAO {

	public boolean insertCourse(CourseDTO courseDTO);
	
	public int getAreaId(String areaName);
	
	public List<TourLocDTO> getAllTourLoc(int courseId);
	
	public boolean insertTourLoc(TourLocDTO tourLocDTO);
	
	public int updateTourLoc(TourLocDTO tourLocDTO);
	
	public int deleteTourLoc(int id);
	
	public int insertNote(NoteDTO noteDTO);
	
	public int updateNote(NoteDTO noteDTO);
	
	public NoteDTO getNote(int tourLocId);
}
