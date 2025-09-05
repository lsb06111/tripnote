package edu.example.tripnote.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.UserDTO;

@Mapper
public interface LoginDAO {
	public UserDTO Login();
}
