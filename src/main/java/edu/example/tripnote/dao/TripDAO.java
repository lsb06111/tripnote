package edu.example.tripnote.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;

@Mapper
public interface TripDAO {

	public boolean insertCourse(CourseDTO courseDTO);
	
	public int getAreaId(String areaName);
	
	public boolean insertTourLoc(TourLocDTO tourLocDTO);
}
