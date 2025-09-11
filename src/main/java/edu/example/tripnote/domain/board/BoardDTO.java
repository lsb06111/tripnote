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
	private String username;
	private String nickname;
	private String createdAt;
	private String modifiedAt;
	private String areaName;
	private int likes;
	private String thumbnail;
}
