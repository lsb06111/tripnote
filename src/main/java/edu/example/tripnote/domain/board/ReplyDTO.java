package edu.example.tripnote.domain.board;

import java.sql.Timestamp;
import java.util.Date;

import lombok.Data;

@Data
public class ReplyDTO {
	private Long id;
	private String replyContent;
	private String createdAt;
	private String modifiedAt;
	private int boardId;
	private int userId;
	private Integer replyId;
	
	//users
	private String username;
	private String usersName;
	private String nickname;
	private String profileImage;
}
