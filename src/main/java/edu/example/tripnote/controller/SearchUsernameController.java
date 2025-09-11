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
public class SearchUsernameController {
	@Autowired
	UserDAO dao;
	
	@ResponseBody
	@RequestMapping("/SearchUsername")
	public Map<String, Object> search1(@RequestParam("find_email") String email) {
		Map<String, Object> result = new HashMap<>();
		String username = dao.searchUsername(email);
		if(username == null || username == "") {
			result.put("msg", "이메일을 다시 확인해주세요.");
			result.put("invalid",  true);
		}
		else {
			result.put("msg", "아이디 -> "+ username);
			result.put("invalid", false);
		}
		return result;
	}
}
