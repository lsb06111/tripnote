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
public class CountByUsername {
	@Autowired
	UserDAO dao;
	
	@ResponseBody
	@RequestMapping("/checkUsername")
	public Map<String, Object> check(@RequestParam String username) {
		Map<String, Object> result = new HashMap<>();
		// 아이디 중복 체크
		if (dao.countByUsername(username) > 0) {
			result.put("msg", "이미 사용 중인 아이디입니다.");
			result.put("invalid", true);
		}
		else {
			result.put("msg", "사용 가능한 아이디입니다.");
			result.put("invalid", false);
		}
		return result;
	}
}
