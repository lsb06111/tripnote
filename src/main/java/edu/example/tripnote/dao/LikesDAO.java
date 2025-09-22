package edu.example.tripnote.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface LikesDAO {
	public boolean save(@Param(value="boardId") int boardId, @Param(value="userId") int userId);
	public boolean delete(@Param(value="boardId") int boardId, @Param(value="userId") int userId);
	public int getLikeCountByBoardId(int id);
	public int isLike(@Param(value="boardId") int boardId, @Param(value="userId") int userId);

}
