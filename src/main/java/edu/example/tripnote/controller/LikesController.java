package edu.example.tripnote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.example.tripnote.dao.LikesDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/like")
@Controller
public class LikesController {
	private final LikesDAO likesDAO;
	
	@ResponseBody
	@GetMapping
	public void updateLike(int boardId, int userId) {
		if(likesDAO.isLike(boardId, userId)>0) {
			likesDAO.delete(boardId, userId);
		}else {
			likesDAO.save(boardId, userId);
		}
	}
	
	@ResponseBody
	@GetMapping("/count")
	public int getLikeCountByBoardId(int boardId) {
		return likesDAO.getLikeCountByBoardId(boardId);
	}

}
