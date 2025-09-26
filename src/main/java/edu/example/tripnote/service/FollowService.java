package edu.example.tripnote.service;

import org.springframework.stereotype.Service;

import edu.example.tripnote.dao.FollowDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowService {
	private final FollowDAO followDAO;
	public boolean isFollowing(int followerId, int followeeId) {
		int result = followDAO.isFollowing(followerId, followeeId);
		return (result > 0) ? true:false;
	}
	
	public boolean follow(int followerId, int followeeId) {
		// 팔로우 중이면 언팔, 아니면 팔로우 수행
		if(isFollowing(followerId, followeeId)) {
			return followDAO.unFollow(followerId, followeeId);
		}else {
			return followDAO.follow(followerId, followeeId);
		}
	}
}
