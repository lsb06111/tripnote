package edu.example.tripnote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.example.tripnote.domain.UserDTO;

@Mapper
public interface UserDAO {
	public UserDTO login(@Param("username") String username,
						@Param("password") String password);
	
	public String searchId(@Param("email") String email);
	
	public String searchPassword(@Param("username") String username,
								@Param("email") String email);
	
	public int insertUser (UserDTO dto);
	
	public int countByUsername (@Param("username") String username);
}
