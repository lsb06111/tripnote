package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.board.CourseSelectResDTO;

@Mapper
public interface CourseDAO {
	public List<CourseSelectResDTO> getCourses(int id);
	public void getTourLocByCourse(int id);
}
