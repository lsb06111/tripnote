package edu.example.tripnote.domain.board;

import java.util.List;

import lombok.Data;

@Data
public class BoardDetailResDTO {
	//게시물, 게시물 내용, 댓글 정보 -> 뷰 전달용
	private BoardDTO boardDTO;
	private List<List<BoardDetailDTO>> contents;
}
