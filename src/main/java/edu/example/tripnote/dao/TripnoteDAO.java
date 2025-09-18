package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;

@Mapper
public interface TripnoteDAO {
	public List<TourLocDTO> getAllTourLoc(@Param("courseId") Long courseId);
	public CourseDTO getCourse(int id);
}
