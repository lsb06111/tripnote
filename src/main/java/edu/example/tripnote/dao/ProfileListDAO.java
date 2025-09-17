package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.trip.AreaVO;
import edu.example.tripnote.domain.trip.CourseDTO;

@Mapper
public interface ProfileListDAO {
	public String title(CourseDTO coursedto);
	
	public String day(CourseDTO coursedto);
	
	public String area(AreaVO areavo);
	
	public AreaVO getArea(int areaId);
	
	public List<CourseDTO> getAllCourse(int userId);
}
