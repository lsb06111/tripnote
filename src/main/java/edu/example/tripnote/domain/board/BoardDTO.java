package edu.example.tripnote.domain.board;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDTO {
	private int id;
	private String title;
	private String boardContent;
	private int userId;
	private String username;
	private String nickname;
	private String profileImage;
	private String createdAt;
	private String modifiedAt;
	private String areaName;
	private String startDate;
	private String endDate;
	private int likes;
	private String thumbnail;
}
