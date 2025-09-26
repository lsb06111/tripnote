package edu.example.tripnote.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.example.tripnote.dao.UserDAO;

@Controller
public class CountByEmailController {
	@Autowired
	UserDAO dao;
	
	@ResponseBody
	@RequestMapping("/Emailcheck")
	public Map<String, Object> checkemail(@RequestParam String email) {
		Map<String, Object> result = new HashMap<>();
		
		if(dao.countByEmail(email) > 0) {
			result.put("msg", "이미 사용 중인 이메일입니다.");
			result.put("invalid", true);
		}
		else {
			result.put("msg", "이메일 전송 중 입니다.");
			result.put("invalid", false);
		}
		
		return result;
	}
}
