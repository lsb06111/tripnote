package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.board.CourseSelectResDTO;
import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.TourLocDTO;

@Mapper
public interface CourseDAO {
	public List<CourseSelectResDTO> getCourses(int id);
	public CourseDTO getCourseById(int id);
	public List<TourLocDTO> getTourLocsByCourseId(int id);
}
