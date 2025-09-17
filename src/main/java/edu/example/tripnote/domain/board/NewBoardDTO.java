package edu.example.tripnote.domain.board;

import lombok.Data;

@Data
public class NewBoardDTO {
	private Long id;
	private String title;
	private String intro;
	private int userId;
	private int courseId;
	private String thumbnail;

}
