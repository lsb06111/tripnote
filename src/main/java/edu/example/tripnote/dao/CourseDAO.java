package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.board.CourseSelectRsps;

@Mapper
public interface CourseDAO {
	public List<CourseSelectRsps> getCourses(int id);
}
