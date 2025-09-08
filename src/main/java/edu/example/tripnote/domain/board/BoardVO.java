package edu.example.tripnote.domain.board;

import java.util.Date;
import lombok.Getter;

@Getter
public class BoardVO {
	private int id;
	private String title;
	private String boardContent;
	private Boolean isPrivate;
	private Boolean isVisible;
	private Date createdAt;
	private Date modifiedAt;
	private int userId;
	private int courseId;
}
