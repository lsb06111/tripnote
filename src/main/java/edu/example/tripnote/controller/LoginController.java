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
public class LoginController {

    @Autowired
    private UserDAO dao;

    @RequestMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam String username,
    								@RequestParam String password) {
    	Map<String, Object> result = new HashMap<>();
    	
    	if(dao.login(username, password) == null) {
    		result.put("msg", "아이디 또는 비밀번호가 불일치합니다.");
    		result.put("ingvalid", true);
    	}
    	else {
    		result.put("msg", "로그인이 완료되었습니다.");
    		result.put("invalid", false);
    	}
    	return result;
    }
}
