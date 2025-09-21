package edu.example.tripnote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.example.tripnote.service.FollowService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FollowController {
	private final FollowService followService;
	
	@ResponseBody
	@GetMapping("/follow")
	public void follow(@RequestParam("follower")int followerId, @RequestParam("followee")int followeeId) {
		followService.follow(followerId, followeeId);
	}
	
	@ResponseBody
	@GetMapping("/isfollowing")
	public boolean isfollowing(int followerId, int followeeId) {
		return followService.isFollowing(followerId, followeeId);
	}
	
	

}
