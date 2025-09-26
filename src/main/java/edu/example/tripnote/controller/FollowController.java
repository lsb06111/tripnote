package edu.example.tripnote.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Void> follow(@RequestParam("follower")int followerId, @RequestParam("followee")int followeeId) {
		boolean isSucceeded = followService.follow(followerId, followeeId);
		if (isSucceeded) {
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@ResponseBody
	@GetMapping("/isfollowing")
	public boolean isfollowing(int followerId, int followeeId) {
		return followService.isFollowing(followerId, followeeId);
	}
	
	

}
