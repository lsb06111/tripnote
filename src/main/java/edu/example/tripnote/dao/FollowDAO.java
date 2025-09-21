package edu.example.tripnote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowDAO {
	public int isFollowing(@Param("follower") int followerId, @Param("followee") int followeeId);
	public boolean follow(@Param("follower") int followerId, @Param("followee") int followeeId);
	public boolean unFollow(@Param("follower") int followerId, @Param("followee") int followeeId);
}
