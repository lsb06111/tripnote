package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.example.tripnote.domain.UserDTO;
import edu.example.tripnote.domain.trip.AreaVO;
import edu.example.tripnote.domain.trip.CourseDTO;
import edu.example.tripnote.domain.trip.CourseIconDTO;

@Mapper
public interface ProfileListDAO {
	
	public UserDTO getUser(String username);
	
	public String title(CourseDTO coursedto);
	
	public String day(CourseDTO coursedto);
	
	public String area(AreaVO areavo);
	
	public AreaVO getArea(int areaId);
	
	public List<CourseDTO> getAllCourse(int userId);
	
	public String getIcon(Long courseId);
	
	public int insertIcon(CourseIconDTO courseIconDTO);
	
	public int updateIcon(CourseIconDTO courseIconDTO);
	
	public int updateTitle(CourseDTO courseDTO);
	
	public int updatePassword(UserDTO userDTO);
	
	public int updateEmail(UserDTO userDTO);
	
	public int updateNickname(UserDTO userDTO);
	
	public List<UserDTO> findUsers(@Param("target") String target,
								   @Param("curUserId") int curUserId);
	
}
