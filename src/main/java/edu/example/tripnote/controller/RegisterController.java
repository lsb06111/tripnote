package edu.example.tripnote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.example.tripnote.dao.UserDAO;
import edu.example.tripnote.domain.UserDTO;

@Controller
public class RegisterController {
	@Autowired
	public UserDAO dao;
	
	@ResponseBody
	@RequestMapping(value="/register", produces="application/json; charset=utf-8")
	public UserDTO Register(UserDTO dto) {
		
		dao.insertUser(dto);
		
		return dto;
	}

}
