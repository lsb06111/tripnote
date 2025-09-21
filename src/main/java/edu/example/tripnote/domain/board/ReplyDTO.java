package edu.example.tripnote.domain.board;

import lombok.Data;

@Data
public class ReplyDTO {
	private Long id;
	private String content;
	private String createdAt;
	private String modifiedAt;
	private int boardId;
	private int userId;
	private Integer replyId;
}
