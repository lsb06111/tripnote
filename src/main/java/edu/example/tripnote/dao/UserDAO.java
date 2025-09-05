package edu.example.tripnote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.example.tripnote.domain.UserDTO;

@Mapper
public interface UserDAO {
	public UserDTO login(@Param("username") String username,
						@Param("password") String password);
	
	public UserDTO searchId(String email);
	
	public UserDTO searchPassword(@Param("username") String username,
								@Param("email") String email);
	
	public UserDTO createUser (UserDTO user);
	
	public int countByUsername(String username); 
}
