package edu.example.tripnote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.example.tripnote.domain.UserDTO;

@Mapper
public interface UserDAO {
	public UserDTO login (@Param("username") String username);
	
	public String searchUsername(@Param("email") String email);
	
	public UserDTO searchPassword(@Param("username") String username,
								@Param("email") String email);
	
	public int updatePassword(@Param("id") int id,
							@Param("password") String password);
	
	public int insertUser (UserDTO dto);
	
	public int countByUsername (@Param("username") String username);
	
	public int countByEmail (@Param("email") String email);
}
